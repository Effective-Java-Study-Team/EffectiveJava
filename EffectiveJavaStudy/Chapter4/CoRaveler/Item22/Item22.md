# 아이템22 - 인터페이스는 타입을 정의하는 용도로만 사용하라

태그: Done

# 1. 먼저 인터페이스의 정의를 알아보자

> 이미 학습했듯이 객체는 노출되는 메서드를 통해 `외부 세계와의 상호 작용을 정의`합니다. 

메서드는 외부 세계와 객체의 *인터페이스*를 형성합니다. 
예를 들어 텔레비전 전면의 버튼은 플라스틱 케이스 반대편에 있는 전기 배선과 
사용자를 연결하는 인터페이스입니다. "전원" 버튼을 눌러 TV를 켜고 끕니다.
> 
> 
> `가장 일반적인 형태에서 인터페이스는 빈 몸체를 가진 관련 메서드 그룹`입니다. 
> 인터페이스로 지정된 자전거의 `동작`은 다음과 같이 나타날 수 있습니다:
> 
> [출처] : [https://docs.oracle.com/javase/tutorial/java/concepts/interface.html](https://docs.oracle.com/javase/tutorial/java/concepts/interface.html)
> 

```java
interface Bicycle {
    //  wheel revolutions per minute
    void changeCadence(int newValue);

    void changeGear(int newValue);

    void speedUp(int increment);

    void applyBrakes(int decrement);
}
```

정리하자면

<aside>
💡 인터페이스는 무엇을 `할 수 있는 지` 에 대해 정의한 것이다.

</aside>

# 2. 상수 인터페이스란?

`메서드 없이, 상수를 뜻하는 static final 만 가득찬 인터페이스`.

```java
public interface PhysicalConstants {
		static final double AVOGADROS_NUMBER = 6.022_140_857e23; 

		static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

		static final double ELECTRON_MASS = 9.109_383_56e-31;
}
```

# 3. 상수 인터페이스는 안티 패턴이다! (==절대쓰면 안됨)

상수 인터페이스의 코드 중 `AVOGADROS_NUMBER` 라는 필드가 있다.

이는 1 mol 에 존재하는 입자의 갯수를 의미하는 상수이다.

![Untitled](/Users/xpmxf4/Desktop/develop/EffectiveJava/EffectiveJavaStudy/Chapter5/CoRaveler/Item22/pictures/페페물음표.png)

이렇게 얘기하면 화학을 전혀 모르는 사람들은 뭔 개소리냐 라고 할 수 있겠는 데,

쉽게 얘기하면, 거의 `Math.PI` 같은 느낌의 용도를 가진 상수라고 보면 된다.

즉, 아보가드로 수는 화학식 계산 시 사용하는 상수이다.

따라서 일반 유저는 이를 가져다 쓸 일이 없다. 

(Math.PI 로 비유를 들었지만, 일반 유저 기준에서 Math.PI 보다 훨씬 덜 쓴다.)

즉 아보가드로 수는 `내부 구현에 해당` 한다고 볼 수가 있다.

![IMG_0152.jpeg](/Users/xpmxf4/Desktop/develop/EffectiveJava/EffectiveJavaStudy/Chapter5/CoRaveler/Item22/pictures/내부구현이냐아니냐.jpeg)

내부 구현은 다음과 같은 이유들로 private~default 로 숨겨야 한다.

1. 사용자는 알바 아니고
2. 클라이언트가 이를 사용시, 내부 구현에 종속되는 코드를 만든다.
3. [바이너리 호환성](https://www.notion.so/47b344ad9e20442d8ec59f5f6fddd174?pvs=21)

# 4. 굳이 상수를 공개할 시 채택할 방법들

1. 클래스나 인터페이스 자체에 추가하기 (단, 강하게 연관된 상수인 경우만!)
    - ex) `Integer.MAX_VALUE`
2. Enum 타입
3. 인스턴스화가 안되는 유틸리티 클래스 
    
    ```java
    public class PhysicalConstants {
    	private PhysicalConstants(){}
    
    	static final double AVOGADROS_NUMBER = 6.022_140_857e23; 
    
    	static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;
    
    	static final double ELECTRON_MASS = 9.109_383_56e-31;
    }
    ```
    
    ```java
    System.out.println(PhysicalConstants.AVOGADROS_NUMBER); // 6.022_140_857e23
    ```
    

<aside>
🤙🏻 인터페이스는 `타입, 행동을 정의하는 용도`이다.
절대로 상수를 공개하는 용도가 아니다!

</aside>

[바이너리 호환성](https://devchpark.notion.site/47b344ad9e20442d8ec59f5f6fddd174?pvs=4)