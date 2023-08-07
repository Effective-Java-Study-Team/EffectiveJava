### 아이템 6 - 불필요한 객체 생성을 피하라

- **똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다.**
    - 물론, 아이템 5에서 살펴본 것과 같이 사용하는 자원에 따라서 동작이 달라지는 객체와 같은 예외는 있다.
      ```java
      String s = new String("bikini"); // 따라 하지 말 것!
      ```

      이 문장은 실행될 때마다 String 인스턴스를 새로 만든다.<br>
      생성자에 넘겨진 “bikini” 자체가 이 **생성자로 만들어내려는 String과 기능적으로 완전히 똑같다.**

      String은 new 연산자를 이용해 객체를 생성할 수도 있고, 값을 할당해 사용할 수도 있다.

      String은 new 연산자를 이용해서 객체를 생성하면, Java Heap 영역에 메모리를 할당받아, 생성되게 된다.<br>
      new 연산자를 사용하지 않고 문자열 리터럴을 할당하면, Java Heap 영역 내부의 **String Constant Pool에 저장**되게 되어<br>
      같은 문자열 리터럴을 사용한다면 **객체를 생성하지 않고 재사용**하게 된다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/07cbd4ea-094f-4620-8ac7-08eb3adf8fb5)

      이 코드를 실행하면 apple은 단 한번 생성되고 String Constant Pool에 이미 apple 이란 값이 있으므로<br>
      이 반복문이 약 21억번 정도 반복하게 되는데 apple을 저장하기 위한 String 인스턴스는 단 한번 생성된다.

      ```java
      for (int i=0; i<INTEGER.MAX_VALUE; i++) {
	       String s = "apple";
      }
      ```

      이 코드를 실행하면 반복하는 회수만큼 String의 객체가 생성되게 된다.<br>
      즉 Integer의 최대 표현값인 **약 21억개 이상의 String 인스턴스가 생성**되게 되는것이다.

      ```java
      for (int i=0; i<INTEGER.MAX_VALUE; i++) {
	       String s = new String("apple");
      }
      ```

      String Constant Pool에 문자열 리터럴을 새 인스턴스로 할당할지, 기존에 이미 있는 값이라 찾아서 반환할지는 **String 클래스의 native 메서드인 intern 메서드를 이용해 수행한다.**<br>
      Native 메서드는 JNI(Java Native Interface)를 사용하여 C/C++ 또는 어셈블리어와 상호 작용할 수 있는 방법을 정의한 메서드이다.

      따라서 Java의 Native 메서드는 Java 이외의 언어로 작성된 코드를 이용해 상호 작용이 필요한 경우에 작성된다.

      ```c
      // string.c
      
      JNIEXPORT jobject JNICALL
      Java_java_lang_String_intern(JNIEnv *env, jobject this){
          return JVM_InternString(env, this);
      }
      ```

      ```c++
      // jvm.cpp
      
      JVM_ENTRY(jstring, JVM_InternString(JNIEnv *env, jstring str))
        JVMWrapper("JVM_InternString");
        JvmtiVMObjectAllocEventCollector oam;
        if (str == NULL) return NULL;
        oop string = JNIHandles::resolve_non_null(str);
        oop result = StringTable::intern(string, CHECK_NULL);
        return (jstring) JNIHandles::make_local(env, result);
      JVM_END

      // StringTable.cpp

      oop StringTable::intern(oop string, TRAPS){
        if (string == NULL) return NULL;
        ResourceMark rm(THREAD);
        int length;
        Handle h_string (THREAD, string);
        jchar* chars = java_lang_String::as_unicode_string(string, length, CHECK_NULL);
        oop result = intern(h_string, chars, length, CHECK_NULL);
        return result;
      }

      oop StringTable::intern(Handle string_or_null, jchar* name,
                        int len, TRAPS) {
      // shared table always uses java_lang_String::hash_code
        unsigned int hashValue = java_lang_String::hash_code(name, len);
        oop found_string = lookup_shared(name, len, hashValue);
        if (found_string != NULL) {
          return found_string;
        }
        if (use_alternate_hashcode()) {
          hashValue = alt_hash_string(name, len);
        }
        int index = the_table()->hash_to_index(hashValue);
        found_string = the_table()->lookup_in_main_table(index, name, len, hashValue);
      
        // Found
        if (found_string != NULL) {
          if (found_string != string_or_null()) {
            ensure_string_alive(found_string);
          }
          return found_string;
        }
      
        debug_only(StableMemoryChecker smc(name, len * sizeof(name[0])));
        assert(!Universe::heap()->is_in_reserved(name),
               "proposed name of symbol must be stable");
      
        Handle string;
        // try to reuse the string if possible
        if (!string_or_null.is_null()) {
          string = string_or_null;
        } else {
          string = java_lang_String::create_from_unicode(name, len, CHECK_NULL);
        }
      
      #if INCLUDE_ALL_GCS
        if (G1StringDedup::is_enabled()) {
          // Deduplicate the string before it is interned. Note that we should never
          // deduplicate a string after it has been interned. Doing so will counteract
          // compiler optimizations done on e.g. interned string literals.
          G1StringDedup::deduplicate(string());
        }
      #endif
      
        // Grab the StringTable_lock before getting the_table() because it could
        // change at safepoint.
        oop added_or_found;
        {
          MutexLocker ml(StringTable_lock, THREAD);
          // Otherwise, add to symbol to table
          added_or_found = the_table()->basic_add(index, string, name, len,
                                        hashValue, CHECK_NULL);
        }
      
        if (added_or_found != string()) {
          ensure_string_alive(added_or_found);
        }
      
        return added_or_found;
      }
      ```

      Java의 String 클래스의 네이티브 메서드인 intern 메서드는 String.c의 **Java_java_lang_String_intern 메서드를 호출하게 되고,**<br>
      이 메서드는 Jvm.cpp의 JVM_InternString 메서드를 호출한다.
      
      JVM_InteringString 메서드는 StringTable.cpp의 intern 메서드를 호출하고,<br>
      StringTable 클래스는 내부 정적 변수인 **StringTable인 theTable**과 **CompactHashTable인 sharedTable** 필드를 활용해<br>
      String Constant Pool에 해당 문자 리터럴이 있는지 확인한다.
      
      **CompactHashTable**을 이용해 Java의 CDS 기능을 활용할 수 있는데,<br>
      **CDS란 여러 JVM 프로세스가 공용으로 상용하는 메모리 공간에 로드된 클래스들을 모아놓고 공유하기 위한 목적으로 활용하는 공간**이다.

      - **생성자 대신 정적 팩토리 메서드를 활용해 불필요한 객체 생성을 막을 수 있다.**
        - Boolean(String) 생성자 대신 Boolean.valueOf(String)을 사용하는 것이 좋다.
        - Boolean 클래스는 static 변수로 TRUE와 FALSE 두개의 인스턴스를 유지하고 있어, valueOf 메서드 호출시 **들어오는 값에 따라서 알맞은 인스턴스를 반환**해준다.

      - **생성 비용이 아주 비싼 객체가 반복해서 필요하다면 캐싱하여 재사용하길 권한다.**
        ```java
        static boolean isValid(String sentence) {
          return sentence.matches("(\\W|^) stock\\s{0,3}tip(s){0,1}(\\W|$)");
        }
        ```

        String.matches 메서드는 정규표현식으로 문자열 형태를 확인하는 가장 쉬운 방법이다.<br>
        하지만 **성능이 중요한 상황이라면 반복해서 사용하기에는 적합하지 않다.**

        대부분의 Regex Engine은 유한 상태 기계를 활용해 문자열이 유효한지 판단하게 되는데,<br>
        이 **유한 상태 기계를 정규 표현식으로 부터 도출하는 과정**을 String.matches 메서드를 매번 호출할때마다 수행하게 되면 그 비용이 커질 수 밖에 없다.
        
        대부분 패턴에 따라 다르지만 선형 시간안에 실행되고, Java 또한 정규식을 처리하기 위해 어느정도 최적화가 되어있기 때문에<br>
        매우 염려할 수준의 성능을 보이지는 않지만 **특정 패턴이나 문자열의 길이에 따라서 꽤 많은 비용을 지불해야 할 가능성이 있다.**
        
        정규식을 TCA(Thompson's Construction Algorithm)을 활용해 비결정적 - 유한 상태 기계로 변환하고,<br>
        Subset Construction Algorithm을 활용해 NFA → DFA(결정적 - 유한상태 기계)로 변환한다.
        
        NFA를 구했다는 것은 정답을 찾기 위해서 필요한 경우의 수를 추려놓은 정도이기 때문에<br>
        확실한 정답을 선별하기 위해서는 DFA로 바꿀 필요가 있다.
        
        이런 복잡한 과정을 거치기 때문에 결론적으로 자주 문자열을 형태를 검사하는 matches 메서드를 자주 호출한다면 아래와 같이 코드를 변경하여<br>
        **캐싱해서 한번만 객체를 생성하는 비용을 지불하자는 것이다.**

        ```java
        private static final Pattern CHECK_SENTENCE = Pattren.compile("(\W|^) stock\s{0,3}tip(s){0,1}(\W|$)");

        static boolean isValid(String sentence) {
	        return CHECK_SENTENCE.matcher(sentence).matches();
        }
        ```

        물론 클래스가 초기화 된 이후 isValid 메서드를 한 번도 호출하지 않는다면 쓸데없는 낭비라고 생각할 수 있다.
        
        메서드가 처음 호출 될 때 필드를 초기화 하는 지연 초기화 방법도 있지만, **지연 초기화로 인해 코드가 복잡해지는 패널티에 비해 성능은 크게 개선되지는 않을 때가 많다.**

        - **Map 인터페이스의 KeySet 메서드는 매번 같은 객체를 반환할까?**
          ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/7ff50f75-95f8-416e-9119-41ab02986b03)

          HashMap 클래스의 keySet 메서드는 부모 클래스의 AbstractMap의 keySet 변수를 Set에 할당한다.<br>
          keySet 메서드를 처음 호출해서 **keySet이 Null 인 경우에만 KeySet 객체를 생성**해서 넘겨준다.

          ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a2a4ece9-f1c5-4359-8817-e68b99b7c6e5)

          보통 Set은 Iterator로 순회하는 경우가 많기 때문에 Iterator를 보면, KeyIterator를 생성해서 반환하는 것을 볼 수 있다.
          ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f2b81ca7-2b7e-4809-a61a-c541b69e583c)

          KeyIterator는 HashIterator의 구현체이면서 next 메서드를 정의하고 있다.
          ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/8731de2f-81ac-4aa2-82ad-489f5b633622)

          HashIterator의 nextNode 메서드를 보면, **table 배열에서 Node<K, V>를 찾아 key 값을 반환한다.**<br>
          HashMap의 데이터를 **Key - Value 형식으로 저장하는 저장소는 Node<K, V> [] 배열이다.**
          
          따라서 **HashMap의 내부 데이터가 변동되면 table 배열이 변동**되게 되고, <br>
          **table의 레퍼런스를 사용하고 참조하고 있는 HashIterator 역시 데이터의 변동**이 일어나게 된다.
          
          따라서 keySet 메서드를 호출할때마다 **새로운 객체를 생성하지 않고 같은 인스턴스를 반환할 수 있는 것이다.**

          - **불필요한 객체를 만들어내는 오토박싱을 주의하자**
            - 오토박싱은 기본 타입(primitive type)과 박싱된 기본 타입을 섞어 쓸때 자동으로 상호 변환해주는 기술이다.
    
            오토박싱은 기본 타입과 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.

            ```java
            private static long sum() {
		          Long sum = 0L;

		          for (long i = 0; i <= Integer.MAX_VALUE; i++) {
				        sum += i;
		          }
            }
            ```

            이 코드는 문자 하나로 성능이 굉장히 나쁘다. 어떤 부분이 잘못되었을까?<br>
            바로 **기본 타입 long 이 아닌 박싱된 기본 타입 Long 을 사용**하고 있기 때문이다.

            sum 변수를 Long 으로 선언해서, 불필요한 **Long 인스턴스가 약 2의 31제곱 만큼 만들어진 것이다.**<br>
            박싱된 기본 타입 보다는 기본 타입을 사용하고, **의도치 않은 오토박싱이 숨어들지 않도록 주의하자.**
            
            물론 요즘의 JVM에서는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다.<br>
            아주 무거운 객체가 아니라면 단순히 객체 생성을 피하고자 객체 pool을 만드는 것은 좋지 않다.
            
            물론, 데이터베이스 연결 같은 경우 생성 비용이 워낙 비싸기 때문에 재사용을 위해 pool을 만드는 게 좋지만 <br>
            그렇지 않은 경우에는 **JVM의 가비지 컬렉터가 최적화되어서 가벼운 객체는 직접 만든 객체 pool 보다 훨씬 빠르다.**
            
            이 아이템은 방어적 복사와 대조적이다. <br>
            기존 객체를 재사용해야 한다면 새로운 객체를 만들지 마라 vs **새로운 객체를 만들어야 한다면 기존 객체를 사용하지 마라(방어적 복사)**
            
            방어적 복사가 필요한 상황에서 <br>
            객체를 재사용했을 때의 피해는 필요 없는 객체를 반복 생성했을 때의 피해보다 훨씬 크다는 사실을 기억해야 한다.
            
            **방어적 복사의 실패는 언제 터져 나올지 모르는 버그와 보안 구멍으로 이어지지만,** <br>
            불필요한 객체 생성은 그저 코드의 형태와 성능에만 영향을 줄 뿐이다.




        
