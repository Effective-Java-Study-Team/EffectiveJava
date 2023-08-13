### 아이템 18 - 상속보다는 컴포지션을 사용하라

상속은 코드를 재사용하는 강력한 수단이지만, 항상 최선은 아니다.

메서드 호출과 달리 상속은 캡슐화를 깨뜨린다. 상위 클래스의 구현이 어떻게 구현되느냐에 따라 하위 클래스가 제대로 동작하지 않을 가능성이 있다. <br>
상위 클래스는 릴리스마다 내부 구현이 달라질 수 있는데, 그 영향으로 코드 한 줄 건드리지 않은 하위 클래스의 동작이 제대로 되지 않을 수도 있는 것이다.

### 상속을 잘못 사용하면 생기는 문제

```java
public class InstrumentedHashSet<E> extends HashSet<E> {

    // 추가된 원소의 수
    private int addCount = 0;

    public InstrumentedHashSet() {}

    public InstrumentedHashSet(int initCap, float loadFactor) {
        super(initCap, loadFactor);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount = c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

}
```

InstrumentedHashSet 클래스는 HashSet을 상속받고 있다. <br>
추가된 원소의 수를 저장하는 필드와 접근자 메서드를 추가하고, add와 addAll을 재정의했다.

원소를 추가할때 addCount를 하나씩 늘리고, 컬렉션을 받아서 여러 원소를 일괄적으로 추가하는 로직을 보았을때 크게 잘못된 부분은 없어보인다.

```java
InstrumentedHashSet<String> set = new InstrumentedHashSet<>();
set.addAll(List.of("a", "b", "c"));

System.out.println("set.getAddCount() = " + set.getAddCount()); // 6이 출력된다.
```

원소는 3개 뿐이지만 카운팅 변수의 값은 6이 되어있다. 왜 이런 문제가 발생할까?

```java
@Override
public boolean addAll(Collection<? extends E> c) {
    addCount = c.size();
    return super.addAll(c);
}
```

원인은 이곳에 있다.  <br>
부모인 HashSet의 addAll 메서드를 사용하고 있는데, 이 addAll 메서드는 내부적으로 add 메서드를 호출한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/cc0a1971-5218-4f27-aa5c-8c247f5d2ff5)

add메서드는 AbstractCollection의 메서드다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/14a2451d-a4ca-49e4-893f-b80cd4e18add)

HashSet은 **AbstractSet**을 상속받고, **AbstractSet**은 **AbstractCollection**을 상속받았다. <br>
따라서 **HashSet에서 add 메서드를 재정의** 하였기 때문에, super.addAll을 호출했을때 내부적으로 <br>
사용하는 add 메서드는 HashSet이 가지고 있는 메서드이다.

InstrumentedHashSet에서 재정의한 addAll 메서드는 들어온 컬렉션의 사이즈만큼 addCount를 증가시킨다. <br>
그 다음 super.addAll을 호출하게 되는데, 여기서 호출하는 add 메서드는 **HashSet 클래스의 메서드가 아닌 InstrumentedHashSet의 메서드가 호출되게 된다.**

왜냐하면 하위 타입의 인스턴스로 메서드를 호출하게 되면, 오버라이딩 된 메서드가 우선순위를 가지기 때문이다.

```java
@Override
public boolean add(E e) {
    addCount++;
    return super.add(e);
}
```

따라서 이 메서드가 호출되게 되므로, addCount는 다시 원소의 개수만큼 증가하게 된다. <br>
들어온 컬렉션의 원소의 수의 2배만큼 addCount는 증가하게 되는 문제가 생긴다.

- **문제 해결 방법**

그럼 이 문제를 어떻게 해결할 수 있을까? <br>
하위 클래스에서 addAll 메서드를 오버라이딩 하지 않으면 문제가 해결된다.

하지만 이렇게 문제를 해결하게 되면, 상위 클래스의 addAll 메서드가 내부적으로 add를 사용한다는 것에 <br>
의존하고 있어, 상위 클래스의 내부 구현이 변하게 되면 하위 클래스가 제대로 동작하지 않을 수 있다.

**자신의 다른 부분을 사용하는 자기사용(self-use) 여부**는 언제든 변할 수 있으니 이런 가정에 기대서는 안된다. 

addAll 메서드를 다른 방식으로 재정의하는 방법도 있다. <br>
상위 인스턴스의 addAll 메서드를 사용하지 않고, 들어온 컬렉션을 순회하면서 add 메서드를 반복 호출해주면 이 문제를 해결할 수 있다.

```java
/*
*   addAll 메서드를 주어진 컬렉션을 순회하며 add 메서드를 호출하는 것으로 변경하면,
*   상위 클래스의 addAll 메서드가 add를 사용하는지와 상관없이 결과가 옳다.
* */
@Override
public boolean addAll(Collection<? extends E> c) {
    int count = 0;
    		
    for (E e: c) {
    	add(e);
    }
    		
    return c.size() == count;
}
```

여기서도 약간의 문제가 있다. <br>
상위 클래스의 메서드 동작을 다시 구현하는 것은 몹시 번거롭고 오류를 내거나 성능을 떨어뜨릴 수 있다.

하위 클래스에서 접근할 수 없는 private 필드를 활용하고 있는 메서드라면, 이 방법으로는 구현조차 할 방법이 없다.

### 자바 라이브러리에서 상속을 잘못한 예

자바의 HashTable과 Vector를 컬렉션 프레임워크에 포함시켜, Properties와 Stack 클래스로 확장을 하게 되었는데 이로인해 문제가 발생했다.

모두 하위 클래스에서 메서드를 재정의가 가능했기 때문에 문제가 발생했다. <br>
클래스를 확장하더라도 메서드를 재정의하지 않고 새로 만든다면 문제가 생기지 않는다고 할 수도 있다.

하지만 상위 클래스의 메서드와 시그니처가 같으면서 반환타입이 다르거나, 상위 클래스의 메서드와 시그니처, 반환타입이 모두 같으면 문제가 생긴다.

전자의 경우 컴파일 에러가 발생해 실행조차 컴파일도 되지 않을 것이고, 후자의 경우 오버라이딩으로 취급되어 예상치 못한 동작을 할 것이기 때문이다.

### 상속대신 컴포지션을 활용하자

위에서 상속으로 인해 생기는 문제에 대해 보았는데, 이를 피해갈 묘안이 바로 컴포지션이다. <br>
기존 클래스를 확장하는 대신 private 필드로 기존 클래스의 인스턴스를 참조하게 하는 것이다.

```java
public class ForwardingSet<E> implements Set<E> {
    private final Set<E> s;

    public ForwardingSet(Set<E> s) {
        this.s = s;
    }

    @Override
    public int size() {
        return s.size();
    }

    @Override
    public boolean isEmpty() {
        return s.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return s.contains(o);
    }

		... 오버라이딩 코드 중략

}   

public class InheritanceSet<E> extends ForwardingSet<E> {

    private int addCount = 0;

    public InheritanceSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    public int getAddCount() {
        return addCount;
    }

} 
```

새 클래스의 인스턴스들은 기존 클래스에 대응하는 메서드를 호출해 결과를 반환한다. <br>
이런 동작 방식을 전달(forwarding)이라 부른다.

새 클래스의 메서드들을 전달 메서드(forwarding method)라고 부른다. <br>
기존 클래스에서는 **새로운 메서드가 추가되더라도 전혀 영향을 받지 않고, 새로운 클래스는 기존 클래스의** **내부 구현 방식의 영향에서 벗어난다.** 

이렇게 코드를 구현하면 여러 종류의 Set 구현체를 사용할 수 있다는 장점도 있고, <br>
제네릭한 코드이므로 여러 타입을 인수로 받는 Set을 사용할 수 있다.

한 번만 구현해두면 **어떠한 Set 구현체라도 계측할 수 있고, 기존 생성자들과도 함께 사용**할 수 있다.

```java
Set<Integer> integerSet = new InheritanceSet<>(new HashSet<>());
Set<String> stringSet = new InheritanceSet<>(new LinkedHashSet<>());

integerSet.addAll(List.of(1, 2, 3));
stringSet.addAll(List.of("1", "2"));

System.out.println("integerSet.size() = " + integerSet.size());  // 3
System.out.println("stringSet.size() = " + stringSet.size());   // 2
```

- 전달 메서드

전달 메서드가 성능에 주는 영향이나 래퍼 객체가 메모리 사용량에 주는 영향은 거의 없다고 밝혀졌다. <br>
재사용할 수 있는 전달 클래스를 인터페이스당 하나씩만 만들어주면 원하는 기능을 덧씌우는 전달 클래스를 아주 손쉽게 구현할 수 있다는 장점이있다.

**가장 좋은 예시는 모든 컬렉션 인터페이스용 전달 메서드가 구현되어 있는 구아바 라이브러리이다.**

- **약간의 TMI**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/151d7236-6c4f-4527-b242-9734a784721b/Untitled.png)

InstrumentHashSet에서 호출하던 부모의 생성자의 **loadFactor는 어디에 사용하는 것일까?**

loadFactor 값은 **해시맵의 크기와 저장된 요소의 비율을 결정하는 값**이다. <br>
**기본값은 0.75**이며, **0.0부터 1.0 사이의 값을 일반적으로 가지게된다.** 값이 작을수록 해시맵은 더 많은 공간을 남겨두고, 값이 클수록 더 적은 공간을 남겨둔다. <br>
값이 너무 작으면 메모리 낭비가 심해지고, 값이 너무 크면 충돌이 더 많이 발생할 수 있다.

해시맵 내부에서 충돌이 발생했을때 리사이징을 언제 수행할지 결정하는 역할을 하기도한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f92c50de-2f68-4c71-8d73-25b47f487605)

resize 메서드 내부에서 loadFactor 값을 활용하는 것을 볼 수 있다.

### 래퍼 클래스와 데코레이터 패턴

다른 Set 인스턴스를 감싸고 있고, InstrumentedSet 같이 다른 인스턴스를 감싸고 있는 클래스를 래퍼(Wrapper) 클래스라고 한다.

다른 Set에 계측 기능을 덧씌운다는 점에서 데코레이터 패턴이라고 부른다. <br>
컴포지션과 전달의 조합은 넓은 의미에서 위임(delegration)이라고 부르기도 한다.

정확히 하자면 래퍼 객체가 내부 객체에게 자기 자신의 참조를 넘기는 경우만 위임에 해당한다.

- **데코레이터 패턴**

그렇다면 데코레이터 패턴은 무엇일까?

데코레이터 패턴이란 주어진 상황, 용도에 따라 **어떤 객체에 책임을 덧붙이는 패턴**으로 기능 확장이 필요할 때 서브클래싱 대신 쓸 수 있는 유연한 대안이 될 수 있다.

데코레이터 패턴은 크게 4가지 구성 요소를 가지고 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/aa1f93cf-3ce7-46f6-8dcc-830dbf988ba3)

- **Component** : 실질적인 인스턴스를 컨트롤 하는 역할
    - 인터페이스로 정의하며, 데코레이터와 실질적인 인스턴스를 하나로 묶는 역할을 한다.
- **ConcreteComponent** : Component의 실질적인 인스턴스의 부분이다.
    - 보통 BaseComponent 라는 이름으로 많이 사용하기도 한다.
    - 예를 들면 빵 → 크림빵, 빵 → 카스테라 → 생크림 케이크 처럼 모든 베이스가 되는 빵을 의미한다.
- **Decorator** : 추상화된 장식자 클래스이다.
    - 원본 객체를 합성한 wrapper 필드와 인터페이스의 구현 메서드를 가지고 있다.
- **ConcreteDecorator** : 구체적인 장식자 클래스이다.
    - 부모 클래스가 감싸고 있는 하나의 Component를 호출하면서 호출 전/후로 부가적인 로직을 추가할 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/276a9ee7-e354-4576-becd-5f33ab1dc810)

이 예제에서는 4가지 구성 요소를 따져보면,

- **Component** : Coffee
- **ConcreteComponent** : BaseCoffeeComponent
- **Decorator** : CoffeeDecorator
- **ConcreteDecorator** : Water, Latte

```java
public interface Coffee {

    String add();
}

public class BaseCoffeeComponent implements Coffee {
    @Override
    public String add() {
        return "espresso";
    }
}

abstract class CoffeeDecorator implements Coffee {

    private final Coffee coffee;

    CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String add() {
        return coffee.add();
    }
}

public class Latte extends CoffeeDecorator {

    public Latte(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String add() {
        return super.add() + " + Milk";
    }
}

public class Water extends CoffeeDecorator {

    public Water(Coffee coffee) {
        super(coffee);
    }

    public String add() {
        return super.add() + " + water";
    }
}
```

데코레이터 패턴을 구현한 클래스들이다.  <br>
커피의 베이스는 에스프레소기 때문에 BaseComponent는 espresso 문자열을 반환한다.

커피 데코레이터 클래스는 커피를 구현한 데코레이터를 내부에 가지고 있으면서 그 데코레이터의 add 메서드를 호출한다.

커피 데코레이터 클래스를 상속받은 Water와 Latte는 물을 넣거나 우유를 넣는 부가 동작을 수행한다.

```java
Coffee latte = new Latte(new Water(new BaseCoffeeComponent()));
System.out.println(latte.add()); // espresso + water + Milk

Coffee americano = new Water(new BaseCoffeeComponent());
System.out.println(americano.add()); // espresso + water

Coffee espresso = new BaseCoffeeComponent();
System.out.println(espresso.add()); // espresso

Coffee strangeCoffe = new Water(new Latte(new BaseCoffeeComponent()));
System.out.println(strangeCoffe.add()); // espresso + Milk + water
```

데코레이터를 조합하는 순서에 따라서 다른 동작을 하도록 유연하게 기능을 조합할 수 있다.

- **Java 에서의 데코레이터 패턴**

```java
BufferedReader br = new BufferedReader(new FileReader(new File("test.txt")));
```

자바에서도 데코레이터 패턴을 이용한 클래스가 여러가지 있다.

### 래퍼 클래스와 콜백 프레임워크의 문제

래퍼 클래스는 단점이 거의 없어서 고려할 사항이 많지 않다.  <br>
단 하나의 문제가 있다면 콜백 프레임워크와는 어울리지 않는 다는 점만 주의하면 된다.

콜백 프레임워크는 자기 자신의 참조를 다른 객체에게 넘겨서 다음 호출때 사용하도록 하는데, 이때 내부 객체는 자신을 감싸고 있는 래퍼의 존재를 모른다.

따라서 **자신(this)의 참조를 넘기게 되서, 콜백 때는 래퍼가 아닌 내부 객체를 호출하게 된다**. 이 문제를 SELF 문제라고 부른다.

```java
public interface SomethingWithCallback {

    void doSomething();

    void call();
}

class Wrapper implements SomethingWithCallback {

    private final WrappedObject wrappedObject;

    Wrapper(WrappedObject wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    @Override
    public void doSomething() {
        wrappedObject.doSomething();
    }

    void doSomethingElse() {
        System.out.println("We can do everything the wrapped object can, and more!");
    }

    @Override
    public void call() {
        System.out.println("Wrapper callback!");
    }
}

public class WrappedObject implements SomethingWithCallback {

    private final SomeService service;

    public WrappedObject(SomeService service) {
        this.service = service;
    }

    @Override
    public void doSomething() {
        service.performAsync(this);
    }

    @Override
    public void call() {
        System.out.println("WrappedObject callback!");
    }
}

final class SomeService {

    void performAsync(SomethingWithCallback callback) {
        new Thread(() -> {
            System.out.println(callback.getClass().getName());
            perform();
            callback.call();
        }).start();
    }

    void perform() {
        System.out.println("performed!");
    }
}
```

이런 코드가 있다고 할때 Wrapper 클래스에서 WrappedObject의 doSomething 메서드를 호출하게 되고,  <br>
WrappedObject는 자신을 감싸고 있는 Wrapper 클래스의 존재를 모르기 때문에 callBack 메서드인   <br>
SomeService 클래스의 performAsync를 호출하며 자신의 참조를 넘겨주게 된다.

이렇기 때문에 내부 객체의 참조가 넘어가는 SELF 문제가 발생하게 된다.

```java
SomeService service = new SomeService();
WrappedObject wrappedObject = new WrappedObject(service);
Wrapper wrapper = new Wrapper(wrappedObject);

/*
	출력 : 
  callback.WrappedObject -> 전달된 참조의 클래스 이름
	performed!
	WrappedObject callback!
*/
wrapper.doSomething();
```

### 상속과 컴포지션의 갈림길에서..

상속과 컴포지션을 어떨때 사용해야 하는지 구분하는 가장 쉬운 방법이 있다.

- **is - a 관계**
    - B는 A다. 로 표현할 수 있을때만 상속이 가능하다.
    - 예를 들어 **배는 과일이다**. 는 is - a 관계이므로 상속관계를 활용할 수 있다.
    - **학생은 교과서다.** 는 has - a 관계이므로 상속관계를 적용할 수 없다.
- **has - a 관계**
    - B는 A를 가지고 있다로 표현할 수 있는 경우로 컴포지션이 어울리는 경우다.
    - 학생은 교과서를 가지고 있다. 는 has - a 관계이므로 컴포지션 대상이다.
    - 배는 과일을 가지고 있다. 는 is - a 관계이므로 컴포지션 대상이 아니다.
    

- **Java Platform Library 조차 위반한 원칙**

스택은 벡터가 아니므로 Stack은 Vector를 확장해서는 안됬다. <br>
속성 목록도 해시테이블이 아니기 때문에 Properties도 Hashtable을 확장해서는 안됬다.

컴포지션을 써야 할 상황에서 상속을 사용하는 건 내부 구현을 불필요하게 노출하는 꼴이된다. <br>
그 결과 API가 내부 구현에 묶이고 그 클래스의 성능도 영원히 제한되게 된다.

클라이언트가 노출된 내부에 직접 접근할 수 있다는 심각한 문제가 더 큰일이다.

Properties 클래스를 예로 들면 이런 문제가 생긴다. <br>
Properties는 **키와 값을 문자열만 허용하려고 설계한 클래스**이다. 하지만 상위 클래스인 **Hashtable의 메서드인 put을 호출**하면 모든 타입의 값을 허용하므로 **불변식을 깨버린다.**

```java
Properties properties = new Properties();
properties.put("1", 2);
properties.setProperty("2", "4");

System.out.println(properties.getProperty("1"));  // null
System.out.println(properties.get("1")); // 2

System.out.println(properties.get("2"));  // 4
System.out.println(properties.getProperty("2")); // 4
```

Properties 클래스의 setProperty을 호출하는 것과, Hashtable 클래스의 put을 오버라이딩 한 Properties 클래스의 put은 동일한 Map에 데이터를 저장한다.

하지만 값을 꺼내오는 부분에서 다른 결과가 나온다.

Hashtable의 put을 이용해서 문자열이 아닌 값을 넣고 **getProperty 메서드를 활용해서 값을 빼려고 하면** <br>
**값이 없다고 나온다.**

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/934d085b-350f-4bb5-820d-a56241e7d45d)

getProperty 메서드는 value가 String이 아니라면 null을 반환해버리기 때문이다. <br>
문자열 값이 아니라면, **설령 값이 있더라도 null이 반환되게 되는 것이다.**

이 뿐만이 아니라, store와 load 같은 Properties의 메서드도 불변식이 깨지는 순간 사용할 수 없게된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/06ef2d16-b3c7-40c7-9ee6-d3b1bde5d1ee)

store 메서드를 보면 key와 value를 String으로 형변환하는 부분이 있다. <br>
문자열이 아닌 값을 Hashtable의 put으로 넣어 둔 경우, ClassCastException이 발생해 사용할 수 없게 될 것이다.

Properties의 사례처럼 문제를 바로잡고자 했을때는 이미 사용자들이 Properties에 **문자열 이외의 타입을 키와 값으로 사용**하고 있었기 때문에 바로잡을 수 없게 된것이다. <br>

상속과 컴포지션 어떤 것을 활용할지 결정하기 전에 마지막으로 꼭 해봐야 할 질문이 있다.

**확장하려는 클래스 API에 아무런 결함이 없는가?** <br>
상속은 상위 클래스의 결함까지도 그대로 승계한다. 컴포지션으로는 이런 결함마저 숨기는 새로운 API 설계가 가능하다는 점을 기억해야한다.

### 정리
   
1. 상속은 캡슐화를 해친다.
2. 상속은 상위 클래스와 하위 클래스가 is - a 관계인 경우만 가능하다.
3. is - a 관계일때도 하위 클래스의 패키지가 상위 클래스와 다르고, 상위 클래스가 확장을 고려해 설계된 클래스가 아니라면 문제가 될 여지가 농후하다.
4. 상속의 취약점을 피하기 위해 컴포지션과 전달을 이용하자.
5. 래퍼 클래스로 구현할 적당한 인터페이스가 있는 경우 래퍼 클래스는 하위 클래스보다 견고하고 강력하다. 심지어 성능 이슈또한 없다.
