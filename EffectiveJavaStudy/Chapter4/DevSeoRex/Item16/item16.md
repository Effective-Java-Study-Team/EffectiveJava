### 아이템 16 - public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용하라

인스턴스 필드들을 모아놓은 일 외외는 아무 목적도 없는 클래스가 있다.

```java
class NonEncapsulation {
    // 외부에서 모두 접근 할 수 있도록 열려있다 -> 캡슐화의 이점을 제공하지 못한다.
    public int x;
    public int y;

}
```

이런 클래스는 데이터 필드에 직접 접근할 수 있기 때문에 캡슐화의 이점을 전혀 누리지 못한다. <br>
외부에서 필드에 접근할 때 부수 작업을 수행할 수도 없다.

예를 들면, 유효한 값으로 변경하려고 하는지 유효성을 검증하는 등의 작업조차 할 수 없는 것이다.<br>
객체에 신뢰성 있는 값이 들어왔는지 확신을 가질 수 없는 위험한 상태로 객체가 존재하게 된다.

```java
class Encapsulation {

    private int x;
    private int y;

    // 접근자와 변경자 메서드를 활용해 데이터를 캡슐화한 개선 클래스
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
```

대부분 private 접근자로 필드를 캡슐화하고 외부에서는 메서드를 이용해서 접근하도록 한다.

public 클래스라면 접근자를 제공하면 된다. <br>
클래스 내부 표현 방식은 언제든지 바꿀 수 있는 유연성을 확보했기 때문이다.

public 클래스가 필드를 공개하면 이를 사용하는 클라이언트가 생겨날 것이므로 내부 표현을 마음대로 바꿀 수 없기에 public 클래스는 필드를 공개하지 않고 접근자를 통한 접근만 허용해야한다.

package-private 클래스 또는 private 중첩 클래스라면 데이터 필드를 노출해도 문제가 없다. <br>
같은 패키지나 같은 클래스에서만 쓰는 필드이기 때문에 외부에서 사용할 수 없다.

항상 노출해야 하는 것은 아니지만, 가독성 면에서도 접근자 메서드를 사용하는 것보다 훨씬 좋다. <br>
일례로 Kotlin 에서는 접근자가 있을 경우 필드가 공개 되었을때 처럼 접근이 가능하다.

데이터 필드를 노출해도 문제가 없는 클래스의 경우, 접근자를 통한 부수 처리가 필요하지 않다면 <br>
공개해서 가독성을 올리는 방법도 충분히 고려해볼만 해보인다.

```kotlin
class Car {
	
   val name: String?,
   val age: Int?
}

fun main() {

     val car = Car()
     println(car.name)
     println(car.age)
     println(car.getName())
     println(car.getAge())
}
```

자바 라이브러리에도 Point와 Dimension 클래스에서 public 클래스임에도 필드를 공개하는 규칙을 어겼다. <br>
이 대가로 내부를 노출한 Dimension 클래스의 심각한 성능 문제는 오늘날까지도 해결하지 못했다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/8488a710-570b-44e8-a2c2-f13b466fd2e0)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/4dcd0611-d2c2-4da1-973f-d6ea65b1ebd8)

public 클래스의 필드가 불변이라면 직접 노출할 때의 단점이 조금은 줄어들 수 있다. <br>
API를 변경하지 않고는 표현 방식을 바꿀 수 없고, 필드를 읽을 때 부수 작업을 수행할 수 없다는 단점은 여전하다.

따라서 상수로 사용하는 필드 이외에는 공개하지 않는 것이 좋다.

### 정리
1. public 클래스는 절대 가변 필드를 직접 노출해서는 안된다.
2. package-private 클래스나 private 중첩 클래스에서는 종종 노출하는 편이 나을 때도 있다.
