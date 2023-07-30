### 아이템 7 - 다 쓴 객체 참조를 해제하라

- **GC를 사용하는 가비지 컬렉터를 갖춘 언어를 사용한다고 해서 메모리 관리에 더이상 신경을 쓰지 않아도 되는 것은 아니다.**
  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/6b0d0a28-9782-46ff-aa8f-fd2acd09e69a)
  
  간단히 작성한 Stack 클래스의 코드다. 이 코드에서 어떤 부분이 메모리 누수를 일으킬까?
  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/682b8bbc-da26-4de6-bee9-a71338873687)

  원소를 꺼내서 반환하는 pop 메서드 부분이다. <br>
  스택이 커졌다가 줄어들었을 때 **스택에서 꺼내진 객체**들을 가비지 컬렉터가 회수하지 않기 때문이다.
  
  스택이 그 객체들의 다 쓴 참조를 여전히 가지고 있기 때문이다. <br>
  다 쓴 참조란 문자 그대로 앞으로 다시 쓰지 않을 참조를 뜻한다. 스택이 내부에 가지고 있는 배열은<br>
  **활성 영역과 비활성 영역**으로 나눌 수 있다.
  
  활성 영역은 인덱스가 size 보다 작은 원소들로 구성되고, <br>
  **비활성 영역은 인덱스가 size 보다 큰 활성 영역 밖의 참조들이 모두 여기 해당한다.** <br>
  객체 참조 하나를 살려두면 **가비지 컬렉터는 그 객체 뿐 아니라 그 객체가 참조하는 모든 객체들을 찾아서 회수할 수 없다.**
  
  이 문제를 해결할 방법은 간단하다. <br>
  **해당 참조를 다 썼을 때 null(참조 해제)하면 된다.**

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/4a184ff0-2fa7-436d-9db6-16f4ed676416)

  스택 클래스에서 각 원소를 꺼내서 참조가 필요없게 되는 시점에 null 처리를 통해 참조 해제하면 된다.<br>
  우리가 사용하는 Stack 클래스의 구현부를 확인해보면

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/48373c90-9543-4608-9916-eaee78d2ce76)

  **pop 메서드는 내부적으로 peek 메서드를 호출**하여 가장 마지막 원소를 반환받게 되고, <br>
  **removeElementAt 메서드를 호출**하여 가장 마지막 원소의 인덱스를 전달하게 된다.

  elementData[elementCount] = null 부분에서 다 쓴 참조를 null 처리하는 것을 볼 수 있다.<br>
  주석에도 **“to let gc do its work”** 라는 문구가 적혀있는 것을 볼 수 있다.

  다 쓴 참조를 null 처리 하는 것이 메모리 관리에도 도움이 되지만, 프로그램의 오류를 조기에 발견할 수 있는 장점도 있다.
  
  미리 다 쓴 참조를 null 처리 하지 않을 경우 **아무 내색 없이 무언가 잘못된 일을 수행할 가능성이 높아지기 때문이다.**
  
  객체 참조를 null 처리하는 일은 예외적인 경우여야 하고, <br>
  **다 쓴 객체 참조를 해제하는 가장 좋은 방법은 그 참조를 담은 변수를 유효 범위 밖으로 밀어내는 것이다.**
  
  변수를 유효 범위 밖으로 밀어낸다는 의미는 **“변수가 유효한 범위를 벗어나게 되는 것”을 뜻한다.**
  
  ```java
  // 함수 블록 안에서 선언된 변수의 경우
  void do something() {
  	 int x = 1;
  	 ...
  }
  
  	
  // 내부 반복 처리를 위해 선언된 변수의 경우
  for (int x=0; x<INTEGER.MAX_VALUE; x++) {
     System.out.println(x);
  }
  ```

  함수 블록 안에서 변수를 선언한 경우와, for 문을 사용하기 위해 반복자로 선언한 x는 함수 블록 밖에서는 호출할 수 없다.

  변수가 Scope(범위)를 벗어났기 때문이다. <br>
  이처럼 **참조를 담은 변수를 최소가 되게 정의**했다면 다 쓴 참조는 알아서 해지되어 따로 null 처리 하지 않아도 된다.
  
  Stack과 같이 자기 메모리를 직접 관리하는, <br>
  즉 저장소 풀을 만들어 원소를 관리하는 클래스라면 항시 메모리 누수에 주의해야 한다.
  
  **가비지 컬렉터가 보기에는 비활성 영역에서 참조하는 객체도 똑같이 유효한 객체이기 때문이다.**
  
  **캐시 역시 메모리 누수를 일으키는 주범이 될 수 있다.** <br>
  객체 참조를 캐시에 넣고서 그 객체를 다 쓴 뒤로도 한참을 그냥 놔두는 일을 자주 접할 수 있다.
  
  이 문제에 대한 해법 중 하나는 **캐시 외부에서 Key를 참조하는 동안만 엔트리가 살아있는 캐시가 필요하다면 WeakHashMap**을 통해 <br>
  자동으로 제거되게 할 수 있다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/679dd5b1-3e76-40c2-8222-f278a193b7cd)

  WeakHashMap 생성하고 key1, key2 그리고 각 value를 넣어준다. <br>
  처음에는 참조를 유지하고 있기 때문에 key와 value가 전부 출력되지만 참조가 끊긴 후에는 아무것도 출력되지 않는다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/15b51226-e8df-40c7-b0b4-63ced68c1042)
  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/7d16c338-8929-49dd-b74c-71363cb4aeee)
  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/d9dbe791-3b2d-479e-a1a6-7009334bb8ce)

  WeakHashMap의 put 메서드에서 언제 WeakReference를 사용하는지 확인하면, <br>
  Entry의 객체를 생성하는 부분에서 부모 생성자를 호출해서 WeakReference를 사용하게 된다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/c387e724-807b-4f28-bdad-511d1931bc9b)

  key1과 key2에 null을 할당하고 GC를 호출하면 모두 GC에게 수집되어 객체는 소멸되게 된다.
  
  어떻게 이게 가능할까?

  Java의 참조에는 Strong Reference, Soft Reference, Weak Reference, Phantom Reference <br>
  크게 4가지 종류로 나눌 수 있다.

  Strong Reference란 일반적으로 변수를 선언할 때 사용하는 방식이다.

  ```java
  Object object = new Object();
  ```

  변수의 범위가 유효하고, 객체에 대한 강한 참조를 가지고 있는 한 GC의 대상이 되지 않는다.

  **Soft Reference vs Weak Reference**

  ```java
  Integer weak = 300;
  Integer soft = 400;
  
  WeakReference<Integer> weakReference = new WeakReference<>(weak);
  SoftReference<Integer> softReference = new SoftReference<>(soft);
  
  weak = null;
  soft = null;
  
  // System.gc를 호출한다고 해서 무조건 GC가 활동하지 않는다.
  System.gc();
  
  System.out.println(weakReference.get());
  System.out.println(softReference.get());
  ```

  SoftReference는 다 쓴 참조가 발생하면 메모리 부족시 다음 GC가 가동할때 객체를 소멸한다. <br>
  WeakReference는 다 쓴 참조가 발생하면 다음 GC가 가동할때 반드시 객체를 소멸하는 점이 다르다.

  System.gc 메서드를 호출하는 부분에서 반드시 GC가 수행되었다고 가정하면

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f2d8bb8b-45ee-488e-9397-bb639e4cf938)

  이런 출력 결과를 확인할  수 있다. <br>
  SoftReference로 래핑되어 있는 값은 아직 메모리가 부족하지 않기 때문에 수집되지 않은 것이다.

  이런 출력 결과를 확인할  수 있다. SoftReference로 래핑되어 있는 값은 아직 메모리가 부족하지 않기 때문에 수집되지 않은 것이다.

  PhantomReference에 대해 이해하려면, GC가 객체를 처리하는 순서에 대해 알아야한다.

  GC는 객체의 reachablity를 아래의 순서대로 판단하게 된다. <br>

  - StrongReference에 해당하는지 확인
  - SoftReference에 해당하는 지 확인 
  - WeakReference에 해당하는 지 확인
  - finalized
  - PhantomReference에 해당하는지 확인

  이 순서를 거쳐서 PhantomReference에 해당한다면 메모리 해제를 미루고 애플리케이션에게 <br>
  객체 소멸 후 처리를 하도록 한다.
  
  PhantomReference는 ReferenceQueue를 필수적으로 사용해야하며, <br>
  이 과정에서 다른 참조방식과 다르게 GC가 직접 null 처리를 하지 않으므로,**사용자가 코드에서 직접 clear 메서드를 호출해 null 할당을 해줘야한다.**

  PhantomReference는 메모리 해제 전 프로그래머가 과정에 어느정도 개입할 수 있는 장점이 있다. <br>
  Phantom Reachable 한 객체는 언제나 null 을 반환하기 때문에 더 이상 사용할 수 없다.
  
  **System.gc는 JVM에게 GC를 실행해도 좋다는 신호를 보낼 뿐**이고, 자바 언어 명세에서도 JVM에게 <br>
  suggestion(제안)하는 정도이기 때문에 언제 효과를 보게 될지는 알 수 없다.
  
  캐시의 유효기간을 정확히 정의하기 어렵기 때문에 시간이 지날수록 캐시의 가치를 떨어뜨리거나, <br>
  **ScheduledThreadPoolExecutor와 같은 백그라운드 스레드를 활용해 청소해 줄 필요가 있다.**
  
  LinkedHashMap 클래스는 **removeEldestEntry** 메서드를 가지고 있는데 <br>
  이 메서드를 오버라이딩 하여 가장 나중에 들어온 데이터를 삭제하거나, 특정 크기에 도달하면 <br>
  가장 나중에 들어온 데이터부터 정해진 양의 데이터를 삭제하는 등의 방식으로 공간을 관리할 수 있다.
  
  마지막으로 클라이언트가 콜백을 등록만 하고 명확히 해지하지 않는 경우에도, <br>
  메모리 누수가 발생할 수 있다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/2514d036-5788-40b4-a6e6-93da9c26c91a)
  
  이벤트를 관리하는 클래스가 이렇게 있다고 할때

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/8a482d45-4f2d-4ba2-acab-68aa32f0d1ff)

  콜백을 등록한 클라이언트의 참조를 끊어도 클라이언트 안의 콜백은 강한 참조를 가지고 있기 때문에 가비지 컬렉션의 대상이 되지 않아 메모리 누수의 주범이 된다. <br>
  따라서, 이런 문제를 해결하기 위해서는 WeakHashMap이나 WeakReference를 이용하여 코드를 개선할 수 있다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5003014a-f1b3-4775-8a1a-e126f474e302)

  WeakReference를 사용하여 약한 참조를 가져가면 콜백이 실행되고 클라이언트로 부터 <br>
  참조가 끊기면 콜백 객체도 약한 참조로 인해 가비지 컬렉션의 대상이되고 결과적으로 WeakReference 까지 가비지 컬렉션의 대상이 된다.



  




