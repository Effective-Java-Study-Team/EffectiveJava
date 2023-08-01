### 아이템 10 - equals는 일반 규약을 지켜 재정의하라

- equals 메서드는 재정의하기 쉬워 보이지만 곳곳에 함정이 도사리고 있어서 자칫하면 끔직한 결과를 초래할 수 있어, <br>주의해서 재정의 해야한다.

문제를 회피하는 가장 쉬운 길은 아예 재정의하지 않는 것이다.

아래의 상황 중 하나라도 해당한다면 equals를 재정의하지 않는 것이 최선일 것이다.

- **각 인스턴스가 본질적으로 고유한 경우**
    - 값을 표현하는 게 아니라 동작하는 개체를 표현하는 클래스가 여기 해당한다.
    - **Thread** 클래스와 같이 값을 표현하는 것이 아닌 동작하는 개체를 표현하는 클래스가 대표적이다.
    
- **인스턴스의 논리적 동치성을 검사할 일이 없는 경우**
    - java.util.regex.Pattern은 equals를 재정의하지 않았다.
    - 두 인스턴스가 같은 정규표현식을 나타내는지를 검사하는 논리적 동치성 검사가 필요하지 않다고 판단하여 **설계자가 Object의 기본 equals를 사용했기 때문이다.**

- **상위 클래스에서 재정의한 equals가 하위 클래스에도 딱 들어맞는 경우**
    - 대부분의 Set 구현체는 AbstractSet이 구현한 equals를 상속받아 쓰고, List의 구현체는 AbstractList 로부터 Map의 구현체는 AbstractMap으로부터 상속 받는다.
      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/31683081-fda9-40c5-975c-b4ed37ca0d59)
  이 코드는 AbstractList의 equals 메서드다. <br>
  구현의 방식은 조금씩 다르지만 모든 원소를 순회하면서 전부 같은 값을 가지고 있는지 확인하는 부분은 모두 같다.

- **클래스가 private 이거나 package-private이고 equals 메서드를 호출할 일이 없는 경우**
    - equals가 실수로라도 호출되는 걸 막고 싶다면 아래와 같이 구현해두면 된다.
      
      ```java
      @Override
      public boolean equals(Object o) {
      	throw new AssertionError();  // 호출하지 말 것!
      }
      ```

  그렇다면 어떤 경우에 equals를 재정의해야 할까?
  
  두 객체가 물리적으로 같은지가 아니라 논리적 동치성을 확인해야 할때 상위 클래스의 equals가 논리적 동치성을 <br>
  비교하도록 재정의되지 않았을 경우다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b3fae5f2-fc7f-47c6-bc03-2c4f936e9bff)
  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/df5d3037-5707-42d9-b20d-51d0f967f103)

  20살의 REX 라는 이름을 가진 p1, p2 객체는 다른 사람으로 봐야할까? <br>
  두 객체는 나이도 같고 이름도 같으니 같은 객체로 판단해야 한다(논리적 동치)
  
  이와 같이 Integer 또는 String 처럼 값을 표현하는 클래스들을 올바르게 비교하기 위해서는 equals를 재정의 해야한다. <br>
  equals를 재정의 하지 않으면 객체의 레퍼런스가 같은지 비교하는 **Object의 기본 equals를 사용한다.**

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/33de57a1-4044-47b1-9aeb-99b3a380f3b2)

  위와 같이 내부의 값을 비교하여 값이 전부 같을 경우 **논리적 동치**이므로 같은 객체로 판단해야한다.

  물론 값 클래스라고 하더라도, 값이 같은 **인스턴스가 둘 이상 만들어지지 않음을 보장하는 인스턴스 통제** <br>
  **클래스라면 equals를 재정의하지 않아도 된다.**
  
  인스턴스 통제 클래스는 논리적으로 같은 인스턴스가 2개 이상 만들어질 일이 없으므로, <br>
  논리적 동치성과 객체 식별성이 사실상 똑같은 의미가 된다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f6cd649d-dc4e-4656-8efd-8ce770c8bd17)

  위와 같이 인스턴스 통제 클래스의 경우 같은 인스턴스가 2개 이상 만들어질 일이 없으므로 equals 오버라이딩이 필요하지 않다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/3bf20d3d-a317-4d12-95f2-f1b872ea5521)

- **Object 명세에 적힌 equals 규약이다.**
  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/50cdcc3a-5157-4cea-9032-a077233147ba)

- **반사성(reflexive)**
  - null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true다.
- **대칭성(symmetry)**
  - null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true면 y.equals(x)도 true다.
- **추이성(transitivity)**
  - null이 아닌 모든 참조 값 x, y, z에 대해서 x.eqauls(y)가 true이고 y.equals(z)가 true이면 x.equals(z)도 true이다.
- **일관성(consitsency)**
  - null이 아닌 모든 참조 값 x, y에 대해 x.equals(y)를 반복해서 호출하면 항상 true 또는 false를 반환해야 한다.
- **null - 아님**
  - null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false다.

  equals 규약을 어기게 되면 프로그램이 이상하게 동작한다. <br>
  세상에 홀로 존재하는 클래스는 없다. 한 클래스의 인스턴스는 다른 곳으로 빈번히 전달되기 마련이다.
  
  컬렉션 클래스들을 포함해 수많은 클래스는 전달받은 객체가 equals 규약을 지킨다고 가정하고 동작한다. <br>
  equals 메서드가 쓸모 있으려면 **모든 원소가 같은 동치류에 속한 어떤 원소와도 서로 교환할 수 있어야 한다.**

- **반사성 - 자기 자신과 같아야 한다.**
    - 반사성은 객체는 자기 자신과 같아야 한다는 뜻이다. x.equals(x) 와 같이 말이다.
    - 반사성 요건을 어긴 클래스의 인스턴스를 컬렉션에 넣고 contains를 호출하면 없다고 답할것이다.

- **대칭성 - 서로에 대한 동치 여부에 똑같이 답해야 한다.**
    - 대칭성은 x.equals(y)가 true라면 y.equals(x)도 true여야 한다.
    - 반사성 요건은 어기기가 더 어려워 보이지만 대칭성 요건은 자칫하면 어길 수 있다.
      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/769a284f-69c5-45c9-9df5-f864baa3b495)

      이 클래스의 가장 큰 문제점은 일반 문자열과도 비교를 시도한것이다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/7e771906-1770-4e13-b40f-e4e9b7e3df1e)

      같은 값을 비교했지만 결과가 다르다 왜 그럴까?

      CaseInsensitiveString 클래스는 String 인스턴스가 들어와도 값을 기반으로 비교할 수 있게 equals 메서드가 오버라이딩 되어있다.
      
      하지만 String 클래스는 CaseInsensitive 클래스의 인스턴스가 들어가면 String 인스턴스가 아니라는 <br>
      이유로 false를 반환한다.
      
      따라서 대칭성 규약을 위반하게 되는 것이다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/df3f6f32-0f13-4b30-ba3d-7e0320f4f041)

      만약 이런 코드가 있을때 false를 반환할 수도 있고 true를 반환할 수도 있다. <br>
      심지어 런타임 예외를 던질 수도 있다. 이는 순전히 구현하기 나름이기 때문에 어떻게 반응할지 예상할 수 없다. <br>
      **equals 규약을 위반하면 객체들이 어떻게 반응할 지 예측이 안된다.** 
      
      이 문제를 해결하는 방법은 간단하다 String과 연동해서 비교하겠다는 마음을 버리고 코드를 수정하면 간단히 해결된다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b330fe95-6089-4324-baba-6f4432b5daf0)

      자바 라이브러리에서도 equals 대칭성을 위배한 사례가 있다. <br>
      바로 **Date와 TimeStamp** 이다. 구체 클래스를 확장해 값을 추가한 클래스의 문제다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/609c12a2-e625-4abf-ad74-829bea8bc4df)

      같은 시간을 넣어서 객체를 생성해도 equals 비교시 대칭성에 위배되고 있다. 왜 그럴까? <br>
      Timestamp 클래스가 들어와도 Date 클래스로 형변환이 가능하다. TimeStamp의 상위 클래스이기 때문이다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/92687a80-ca0e-4212-b1a1-18a96ab6a24f)

      따라서 Date 클래스의 equals는 정상적으로 동작한다. Date를 넣어도, Timestamp를 넣어도 <br>
      가지고 있는 시간의 값만 같다면 true를 반환한다.

      ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b41a60ea-126e-4140-aaf2-d4076a4142b4)

      Timestamp의 equals는 Timestamp의 인스턴스가 아니라면 무조건 false를 반환하기 때문에 equals 규약에 위배되는 결과를 반환하게 된다.

- **추이성(transitivity) - a.equals(b) == true 이면서 b.equals(c) == true 라면 a.equals(c) 역시 true다.**

  추이성 역시 자칫하면 어기기 쉬운 항목중 하나이다. 상위 클래스에 없는 새로운 필드를 하위 클래스에 추가하는 상황을 생각해보면 알 수 있다.
  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/175c925d-86b2-4d6e-bd9e-59124fc09dbe)

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/1371cbca-44a0-4756-a3ee-6b859a9296cb)

  Point 클래스와 Point 클래스를 확장한 ColorPoint 클래스를 작성했다. <br>
  Point 클래스의 equals를 상속해서 사용하면 색상 정보를 무시한채 비교를 수행하기 때문에 ColorPoint 클래스 역시 equals를 재정의 해주었다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/af131bd7-9c25-430b-8f62-f0d13f3171ac)

  왜 이런 결과가 나오게 될까?
  Point 클래스의 equals는 색상을 무시하고 동작하기 때문에 true가 나오게 되고, <br>
  ColorPoint의 equals는 ColorPoint의 인스턴스가 아니라고 false를 반환하게 된다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/8f080014-5fa4-47c3-97b4-b1124d957967)

  ColorPoint의 equals가 Point와 비교할떄는 색상을 무시하도록 코드를 변경하면 해결될까? <br>
  이 방법으로 대칭성을 만족시킬 수 있게 되었다. 하지만 이 방법은 추이성을 깨버린다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/fd86784a-7c93-4ccb-ae42-53b39ea1a282)

  또 다른 문제도 있다.

  Point 클래스의 다른 하위 클래스인 SmellPoint 클래스를 만들고 같은 방식으로 equals를 오버라이딩 <br>
  하게 되면 StackOverflowError 까지 만날 수 있다.

  SmellPoint와 ColorPoint 사이의 equals 비교가 무한 재귀에 빠지는 이 코드 때문이다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a6cc9ccb-c9e4-4911-b702-49a307e8806a)

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/d277093d-0be7-4f74-b5a1-20f6c043c600)

  자신의 인스턴스가 아니면 들어온 객체의 equals를 호출하게 되는데 <br>
  호출된 equals 역시 자신의 인스턴스가 아니기 때문에 들어온 equals를 호출하게 되고 끝나지 않는 재귀 호출이 발생하게 되는 것이다.

  이 문제를 해결할 수 있는 방법은 없을까?
  
  이 현상은 모든 객체 지향 언어의 동치 관계에서 나타는 근본적인 문제다. <br>
  따라서 부모 클래스를 확장해 새로운 값을 추가하면서 equals 규약을 지킬 방법은 없다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a5b8249f-da42-4daf-970d-f38f28da9c02)

  Point 클래스의 equals를 위와 같이 오버라이딩 하면 같은 구현 클래스의 객체와 비교할 때만 true를 반환한다.

  Point의 하위 클래스는 정의상 여전히 Point 이므로 어디서든 Point 로서 활용될 수 있어야 하는데 이 코드는 LSP(리스코프 치환 원칙) 위반이다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/74678715-0f4f-4769-8f72-b037b444a88d)

  값을 추가하지 않고 CounterPoint 클래스를 만들어 Point 클래스를 확장했다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/472a3f07-8da5-42f2-aef5-0a3734240e13)

  Point 클래스의 내부 x, y 값이 완전히 같은 CouterPoint 객체를 unitCircle Set에서 찾으면 없다고 나온다. <br>
  **CounterPoint의 인스턴스는 어떤 Point와도 같을 수 없기 때문에 이렇게 동작한다.**

  아까와같이 instanceof 기반으로 올바로 구현했다면 onUnitCircle 메서드가 제대로 동작했을 것이다.

  구체 클래스의 하위 클래스에서 값을 추가할 방법은 없어도 괜찮은 우회방법은 있다. <br>
  **상속 대신 컴포지션**을 사용한 방법을 따르면 된다.
  
  Point를 ColorPoint의 private 필드로 두고 **ColorPoint와 같은 위치의 일반 Point를 반환하도록** <br>
  **view 메서드를 public 으로 추가하면 된다.**

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a77ff6a2-ecc4-497f-b580-924b6772f69a)

  자바 라이브러리 중에도 구체 클래스를 확장해 값을 추가한 클래스가 종종있어 문제를 발생시킨다. <br>
  java.sql.Timestamp는 java.util.Date를 확장한 후 nanoseconds 필드를 추가했다.

  결과적으로 **Timestamp의 equals는 대칭성을 위배하게 되었다.**

  추상 클래스의 하위 클래스에서라면 equals 규약을 지키면서도 값을 추가할 수 있다.

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5f037f09-7559-42d6-9c5b-1c392fd32e40)

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/c60c36d4-f114-4108-b67a-6986487aa154)

  ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/dac33528-4a90-4d51-8521-d47bac1a56ac)

  **상위 클래스의 인스턴스를 생성할 수 없기 때문에 지금까지 나온 문제들은 일어나지 않는다.** <br>
  왜냐하면 상위 클래스인 **Coffee는 인스턴스를 생성할 수 없기 때문에** Point 와 같이 상위 타입의 인스턴스와 <br>
  하위 구현체를 비교할 수 없게 강제할 수 있다.
  
  또한 Latte와 Americano는 다른 클래스이고 같은 하위 구현체들끼리만 비교를 수행할 수 있기 때문에 <br>
  equals 규약을 어기지 않을 수 있다.
  
  물론 익명 객체를 이용한 객체 생성은 가능하지만, abstract 키워드가 붙었다면 직접적인 인스턴스화를 <br>
  권하지 않고 상속을 통한 확장이 사용 목적에 부합하다고 보아야 하기 때문에, <br>
  **익명 객체를 통한 인스턴스 생성까지 equals 규약을 통해 살펴보아야 할 필요성은 없어보인다.**
  
  만약 Coffee의 추상 메서드가 3개 이상이라면 그것을 모두 구현하면서 까지 익명 객체를 생성해 equals <br>
  비교를 수행하는 것은 정말 비효율적이므로 **익명 객체를 통한 인스턴스화는 고려하지 않는다.**

- **일관성 - 두 객체가 같아면 앞으로도 영원히 같아야 한다.**

  equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안된다.

  java.net.URL 클래스의 equals는 **주어진 URL과 매핑된 호스트의 IP 주소를 이용해 비교한다.** <br>
  호스트 이름을 IP 주소로 바꾸려면 네트워크를 통해야 하는데 그 결과가 항상 같다고 볼수는 없다.

  이러한 문제를 피하려면 equals는 **항시 메모리에 존재하는 객체만을 사용한 결정적 계산만 수행해야 한다.**

- **모든 객체는 null과 같지 않아야 한다(null - 아님)**

  equals 메서드를 호출할때 비교대상 객체가 null 이라면 true를 반환하지는 않겠지만 NPE가 발생할 수 있을 것이다.

  ```java
  @Override
  public boolean equals(Object o) {
		if (o == null) return false;
  }
  ```

  이렇게 위의 코드 처럼 명시적으로 null 검사를 수행하려는 경우가 많은데 굳이 그럴 필요는 없다.

  ```java
  @Override
  public boolean equals(Object o) {
		if( !(o instanceof Point)) {
			 return false;
		}
  }
  ```

  instanceof 연산자를 이용해 타입 검사를 하면 **첫 번째 피연산자가 null이면 false를 반환한다.** <br>
  따라서 입력이 null이면 타입 확인 단계에서 false를 반환하기에 null 체크를 하지 않아도 된다.

- **equals 메서드를 잘 구현하기 위한 방법**
  - **== 연산자를 사용해 입력이 자기 자신의 참조인지 확인할 것**
    - 비교 작업이 복잡한 경우 성능 최적화가 가능하다.
      
      ```java
      // 자기 자신의 참조인지 확인
		  if (o == this) return true;
      ```
  - **instanceof 연산자로 입력이 올바른 타입인지 확인한다.**
    - 올바른 타입이라 함은 equals가 정의된 클래스인 것이 보통이지만, **그 클래스가 구현한 특정 인터페이스가 될 수도 있다.**
    - Set, List, Map, Map.Entry 등의 컬렉션 인터페이스들이 여기 해당한다.

      ```java
		  // 입력이 올바른 타입인지 확인
      if (!(o instanceof <Type>)) return false; 
      ```
  - **입력을 올바른 타입으로 형변환한다.**
      ```java
  	  // 입력을 올바른 타입으로 형변환
  		
      // 방식 1. 직접 변환
  		<Type> type = (<Type>) o;
  
     // 방식 2. Java 16 부터 나온 Pattern Matching 사용
  	 if (!(o instanceof <Type> type)) return false;
      ```

  - **입력 객체와 자기 자신의 대응되는 ‘핵심’ 필드들이 모두 일치하는지 하나씩 확인**

    ```java
    return type.property1 == this.propety1 
					&& type.property2 == this.property2 ... etc
    ```

    float와 double을 제외한 기본 타입 필드는 == 연산자로 비교한다. <br>
    float와 double 필드는 각각 정적 메서드인 compare로 비교해야 한다.

    Float.NaN, -0.0f, 특수한 부동소수 값을 다뤄야 하기 때문이다.
    
    **Float와 Double 클래스의 equals 메서드**를 사용할 수도 있지만 이 메서드들은 오토박싱을 <br>
    수반할 수 있기 때문에 성능상 좋지 않다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/1024656b-c004-47b8-adc2-162a711a217a)

    때론 null도 정상 값으로 취급하는 참조 타입 필드도 존재한다. <br>
    이런 필드를 비교할때는 Objects.equals 메서드를 활용해 NPE를 방지할 수 있다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b8775d97-31df-43b8-8ad6-f003c4582ae0)

    CaseInsensitiveString 처럼 비교하기가 아주 복잡한 필드를 가진 경우 **그 필드의 표준형을 저장** <br>
    **해두고 표준형끼리 비교하면 훨씬 경제적이다.**

    문자열을 기준으로 생각해보면 “apple tree”와 “appletree”는 다른가? 

    만약 엄격하게 공백까지 구분해야 하는 비즈니스 요구사항이 있다면 다르게 취급하겠지만 대부분은 같다고 할것이다.
    
    이럴 경우에 apple tree와 appletree를 비교할때마다 공백을 없앤 정규형을 만들지 말고, <br>
    클래스의 **최초 인스턴스가 생성될때(불변 클래스) 또는 값이 변경될때(가변 클래스) 정규형을 저장해두고 비교하면 경제적이라는 뜻으로 이해했다.**

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/5e7b7707-e89a-4dd8-b9ce-68a610357b21)

    객체를 생성하거나 문자열의 값을 바꿀때 정규형을 미리 저장하도록 클래스를 작성해두었다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a8f13561-72eb-4692-92e9-2415049e4013)

    공백을 무시하고 비교해야 하거나, 줄바꿈 문자를 무시하거나 여러가지 비즈니스 요건에 따라 <br>
    정규형을 지정하고 미리 저장해두면 비교시에 훨씬 경제적일 것 같다.
    
    이외에도 어떤 필드를 먼저 비교하느냐에 따라서 equals의 성능을 좌우하기도 한다. <br>
    **다를 가능성이 더 크거나 비교하는 비용이 싼 필드를 먼저 비교하는 것이 좋다.**
    
    동기화용 lock 필드와 같이 객체의 논리적 상태와 관련없는 필드를 비교하지 말아야 하며, <br>
    핵심 필드로부터 계산해낼 수 있는 파생 필드 역시 굳이 비교할 필요는 없다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/e45e6daa-08fe-4d4c-8c0b-72e4ad7c04c8)

    이런 Circle 클래스가 있다고 할때 핵심 필드인 radius(반지름)이 같다면 area도 반드시 같게 되있다. <br>
    따라서 area는 비교대상에서 제외해도 된다는 이야기이다.
    
    만약에 200쪽의 페이지를 가지고 있는 노트 객체를 표현한다고 했을때 <br>
    전체 페이지를 일일히 비교하는 것 보다는 노트에 필기한 부분만 비교해서 같은지 확인하는 것이 더 경제적일 것이다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/d2338e66-ab53-447b-9a04-2a753e365214)

    - 주의 사항
      - **equals를 재정의할 땐 hashCode도 반드시 재정의하자(아이템 11)**
      - **너무 복잡하게 해결하려 들지 말자**
          - 필드들의 동치성만 검사해도 equals 규약을 어렵지 않게 지킬 수 있다.
          - 너무 공격적으로 파고들다가 문제를 일으키기도 한다.
      - **Object 외의 타입을 매개변수로 받는 equals 메서드는 다중정의에 해당한다.**













  







  



      










      
