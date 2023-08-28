### 아이템 31 - 한정적 와일드카드를 사용해 API 유연성을 높이라

### 한정적 와일드 카드를 사용하는 이유

한정적 와일드 카드에 대해 정리하기 전에, `제네릭`의 `매개변수화 타입`은 `불공변(invariant)` 이라는 것을 먼저 짚고 넘어가야 한다.

```java
// String은 Object 클래스의 모든 멤버를 상속받았기 때문에 Object가 하는 일을 수행할 수 있다.
Object object = new String();

/*
* List<String>은 List<Object>의 상위 타입도 하위 타입도 아니다.
* 제네릭은 불공변이기 때문에 이 코드는 컴파일 에러를 발생시킨다.  
*/
List<Object> object = new ArrayList<String>();
```

위의 코드 처럼 `상위 타입의 변수`로 `하위 타입의 인스턴스`를 가리킬 수 있는 것이 `다형성이`고, `리스코프 치환 원칙`의 기본이 되는 내용이다. <br>
`제네릭`은 공변이 아닌 `불공변`이기 때문에 위의 코드처럼 `제네릭`이 없는 곳에서는 되던 것이 `제네릭`의 `매개변수화 타입`에서는 적용되지 않는다.

`List<Object>`는 `모든 타입`의 객체를 받을 수 있지만, `List<String>`은 `String` 이외의 객체를 받을 수 없다. <br>
즉 `List<Object>`의 역할을 `List<String>`이 대신 해줄 수 없기 때문에 당연히 `리스코프 치환 원칙`을 지킬 수 없다. `제네릭`의  `불공변`은 어떻게 보면 당연한 선택 인 것이다.

그렇다면 `불공변`으로 `리스코프 치환 원칙`을 지켰지만, 왜 `한정적 와일드카드`와 같은 `유연함`이 필요한걸까?

```java
class BeforeRefactorStack<E> {

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public BeforeRefactorStack() {
        this.elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) throw new EmptyStackException();

        E result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    public void pushAll(Iterable<E> src) {
        for (E e : src) {
            push(e);
        }
    }

    public void popAll(Collection<E> dst) {
        while (!isEmpty()) {
            dst.add(pop());
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

간단하게 스택을 구현한 코드다. <br>
여기서 현재 결함이 있는 코드는 두 군데가 있는데 먼저 `결함이 있는 메서드`를 살펴보면 아래와 같다.

```java
public void pushAll(Iterable<E> src) {
    for (E e : src) {
        push(e);
    }
}
```

이 코드는 컴파일이 성공하기 때문에 코드의 문법이 잘못된 것은 아니다. <br>
하지만 논리적인 결론과 다르게 동작하는 점이 문제가 있어서 약간의 개선을 거쳐야 한다.

```java
BeforeRefactorStack<Number> stack = new BeforeRefactorStack<>();
Set<Integer> set = Set.of(1, 2, 3, 4);

// java: incompatible types: java.util.Set<java.lang.Integer>
// cannot be converted to java.lang.Iterable<java.lang.Number>
stack.pushAll(set);
```

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/2a3047f1-9699-4da7-9ffa-42f69c7c1544)

`Number` 타입은 `Integer`의 상위 타입인데, `Number` 타입을 허용하는 `Stack`에서는 `Integer` 타입을 <br>
받아들이지 않고 있는 문제가 발생한다.

`매개변수화` 타입이 `불공변`이기 때문이다. <br>
논리적으로 가능해야 할 부분이 가능하지 않기 때문에 어떻게 해결해야 할까?

`자바`에서는 `한정적 와일드카드 타입`이라는 특별한 `매개변수화 타입`을 지원하고 있다. <br>
`pushAll` 메서드의 입력 매개변수 타입을 `Itreable<? extends E>`로 변경하면 이 문제가 해결된다.

`E`의 `하위 타입`이라면 전부 허용하겠다는 뜻이다. <br>
`하위 타입`은 `자기 자신도 포함`하기 때문에 `E 타입` 역시 들어갈 수 있다.

```java
public void pushAll(Iterable<? extends E> src) {
    for (E e : src) {
        push(e);
    }
}

// 에러가 발생하지 않는다.
AfterRefactorStack<Number> stack2 = new AfterRefactorStack<>();
Set<Integer> set2 = Set.of(1, 2, 3, 4);
stack2.pushAll(set2);
```

왜 `extends`는 되고 `super`는 되지 않을까? <br>
그 이유는 생각해보면 간단하다. 직접 형변환을 수행하고 있지는 않지만 컴파일러가 자동으로 넣어주기 때문에 실제 코드 동작은 이렇게 동작한다.

```java
public void pushAll(Iterable<? extends E> src) {
    for (E e : src) {
        push((E)e);
    }
}
```

`E`의 상위 타입은 `E` 타입으로 `강제 다운 캐스팅`을 하려하면 `런타임에 문제`가 발생할 것이기 때문이다. <br>
이제 같은 문제를 겪는 메서드를 하나 더 고쳐야한다.

```java
public void popAll(Collection<E> dst) {
    while (!isEmpty()) {
        dst.add(pop());
    }
}
```

`Stack`의 원소를 모두 `pop`하여 나온 순서대로 들어온 `Collection`에 더 해주는 메서드이다.

```java
BeforeRefactorStack<Number> stack3 = new BeforeRefactorStack<>();
stack3.push(1);
stack3.push(2);
Collection<Object> collection = new ArrayList<>();
// java: incompatible types: java.util.Collection<java.lang.Object>
// cannot be converted to java.util.Collection<java.lang.Number>
stack3.popAll(collection);
```

이 코드 역시 문제를 발생시킨다. 

`Collection<Object>`는 `Collection<Number>`로 변환될 수 없기 때문이다. <br>
이 문제 역시 간단하게 `메서드`의 내용을 변경해주면 유연하게 사용할 수 있다.

```java
public void popAll(Collection<? super E> dst) {
    while (!isEmpty()) {
        dst.add(pop());
    }
}
```

여기서는 왜 `super` 키워드를 사용해서 `E`의 `상위 타입`의 컬렉션만 올 수 있는지 살펴봐야 한다. <br>
이 문제는 `와일드 카드`의 종류에 따라 정리가 필요하다.

### 와일드 카드 정리

- 상한 경계 와일드 카드

상한 경계 와일드 카드란 `List<? extends U>`와 같이 사용하는 경우를 말한다. <br>
`자료를 꺼내올때`는 `U 타입`으로 변환해야 안전하게 꺼낼 수 있다. <br>
`어떠한 타입`의 자료도 넣을 수 없게 되어 있어 `null`만 `삽입 가능`하다.

`형제 객체 컬렉션`이 들어오게 되면, `저장`이 `불가능`하기 때문에 저장을 허용하지 않는다. <br>
`U 타입`의 컬렉션이 들어온다면 저장을 해도 되지만 `논리 오류` 때문에 컴파일 에러로 처리되고 있다.

```java
class Fruit {}
class Apple extends Fruit{}
class Banana extends Fruit{}

class FruitBox {
    public static void add(List<? extends Fruit> items) {			 
        item.add(new Fruit());
        item.add(new Apple());
        item.add(new Banana());
    }
}
```

이렇게 클래스들이 있다고 할때, 만약 `메서드`의 인자로 `List<Fruit>`가 아닌 `다른 하위 타입`이 주어지게 된다면 `형제 타입 객체`를 저장할 수 없으므로 `에러`가 `발생`한다.

- 하한 경계 와일드카드

하한 경계 와일드 카드란 `List<? super U>`와 같이 사용하는 경우를 말한다. <br>
`자료를 꺼내올때`는  `Object` 타입으로 변환해야 안전하게 꺼낼 수 있다. <br>
`U 타입`과 `그 자손 타입`만 저장할 수 있다. 

```java
class Food {}
class Fruit extends Food {}
class Apple extends Fruit{}
class Banana extends Fruit{}

class FruitBox {
    public static void add(List<? super Fruit> items) {
        item.add(new Fruit());
        item.add(new Apple());
        item.add(new Banana());
        item.add(new Food());
    }
}
```

저장할때는 왜 U 타입과 그 자손만 될까? <br>
위의 코드를 보면 `Food`가 최고 조상이고 그 아래로 `Fruit`와 `Apple` 등의 자손이 있다.

만약 `메서드`의 인자로 `List<Fruit>`를 제공한다면 `Food` 타입은 저장이 불가능하다. <br>
따라서 업캐스팅 가능 상한인 `Fruit 타입`으로만 제한된다. <br>
`Fruit 타입` 이거나, `Fruit 타입`으로 `업캐스팅`이 `가능`한 객체만 `저장 가능`하다는 이야기가 된다.

이렇게 `super` 키워드와 `extends` 키워드를 어떻게 구분할지 고민된다면 아래의 규칙을 따르면 된다.

### PECS : producer - extends, consumer - super

`매개변수화 타입 T`가 생산자라면 `extends`를, 소비자라면 `super`를 사용하라는 간단한 약속이다.

```java
public void pushAll(Iterable<? extends E> src) {
    for (E e : src) {
        push(e);    
    }
}

public void popAll(Collection<? super E> dst) {
    while (!isEmpty()) {
        dst.add(pop());
    }
}
```

이 두 개의 메서드를 비교해보면 간단하다.

여기서 `src 매개변수`는  `pushAll` 메서드에서 `E`의 `인스턴스`를 `생산하는 역할`을 한다. <br>
반면에 `popAll` 메서드에서 `dst 매개변수`는 Stack 으로부터 `E 인스턴스`를 `소비`하는 역할을 한다

이렇게 `매개변수화 타입`을 사용하는 `매개변수`에서 어떤 역할을 하는지에 따라서 어떤 종류의 와일드 카드를 사용할지 결정할 수 있는 것이다.

### 자바의 타입 추론 능력과 와일드 카드 보충

`한정적 와일드 카드`를 사용해서 유연한 설계를 가져갈 수 있게 코드를 변경했다. <br>
여기서 주의할 점은 절대 반환 타입은 `한정적 와일드카드 타입`을 `사용해서는 안된다는 것`이다. <br>
유연성을 높여주기는커녕 `클라이언트 코드`에서도 `와일드카드 타입`을 써야 하기 때문이다.

자바 7 까지는 자바의 `타입 추론 능력`이 강력하지 못해서, `문맥에 맞는 반환 타입을 명시`해야 했었다.

```java
Set<Integer> set = Set.<Integer>of(1, 2, 3, 4);
```

`자바 8` 버전 부터는 지금은 `타입 추론 능력`이 좋아져서 `명시적 타입 인수`를 사용하지 않아도 코드가 동작한다.

```java
Set<Integer> set = Set.of(1, 2, 3, 4);
```

이번에는 `PECS 공식`이 두 번 적용된 복잡한 예제를 한 번 확인해보자

```java
public static <E extends Comparable<? super E>> Optional<E> maxRefactor(List<? extends E> list) {
    if (list.isEmpty()) {
        return Optional.empty();
    }
    		
    E result = null;
    for (E e : list) {
        if (result == null || e.compareTo(result) > 0) {
            result = Objects.requireNonNull(e);
        }
    }
    		
    return Optional.of(result);
}
```

먼저 `Comparable`을 살펴보면, `Comparable<E>`는 `E` 인스턴스를 `소비`한다. <br>
그래서 `소비자의 역할`을 하고 있으므로 `super` 키워드를 사용했다.

반면에 `List<E>`는 `E` 인스턴스를 `생산`하기 때문에 `생산자의 역할`을 한다. <br>
따라서 `extends` 키워드를 사용했다.

비교는 `같은 타입의 객체`끼리 할 것 같은데 왜 `Comparable`도 `Comparable<? super E>`와 같이 `한정적 와일드 카드`를 이용해야 하는 걸까? <br>
`Comparable` 또는 `Comparator`를 직접 구현하지 않고, 직접 구현한 다른 타입을 지원하기 위해서 `와일드카드`가 필요하다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/3b00afd6-6ec2-403f-81fd-b97f65f2837f)

```java
interface Animal extends Comparable<Animal> {
    void walk();
}

interface Dog extends Animal {
    void bark();
}

class SuperAnimal implements Animal {
    @Override
    public void walk() {

    }

    @Override
    public int compareTo(Animal o) {
        return 1;
    }
}

public class FrenchDog implements Dog {
    @Override
    public void walk() {

    }

    @Override
    public void bark() {

    }

    @Override
    public int compareTo(Animal o) {
        return o.compareTo(this);
    }
}
```

이런 상속 계층을 가지고 있는 `클래스`와 `인터페이스`가 있다고 해보겠다. <br>
`Dog`는 `Dog 인스턴스` 뿐만 아니라 `Animal 인스턴스`와도 비교할 수 있도록 와일드카드 사용이 필요한 것이다.

`자바 플랫폼`에서는 `ScheduledFuture` 와 `Delayed`의 관계에서 이런 설계를 찾아볼 수 있다. <br>
`ScheduledFuture`는 인터페이스 이므로, `ScheduledFutureTask` 구현체를 보면 이해하기 쉽다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/445384a9-44bf-4f5a-91a1-de1b142beff0)

`Delayed` 인스턴스와 `ScheduledFutureTask`의 인스턴스를 전부 비교할 수 있도록 `확장`되어 있다.

- **비한정적 와일드 카드의 문제**

```java
public static <E> void swap(List<E> list, int i, int j);
public static void swap(List<?> list, int i, int j);
```

만약 두 개의 메서드가 있는데 `public API`로 사용하려 한다면 두 번째 메서드가 간단하고 좋을 것이다. <br>
`메서드 선언`에 `타입 매개변수`가 한 번만 나온다면 `와일드 카드로 대체`하는 것이 기본 규칙이다.

문제는 `비한정적 와일드 카드`를 사용하는 두 번째 코드는 컴파일조차 되지 않는다는 점이다. <br>
`비한정적 와일드 카드`는, 데이터를 꺼낼 때는 `super의 특징`을 자료를 넣을때는 `extends의 특징`을 가진다.

안전하게 꺼내려면 `Object` 타입으로만 받아야하고, 어떠한 타입의 자료도 넣지 못한다(`null은 가능`)

```java
public static void swap(List<?> list, int i, int j) {
	 list.set(i, list.set(j, list.get(i)));
}
```

이 문제를 해결하기 위해서는 `와일드카드 타입`의 `실제 타입`을 알려주는 `도우미 메서드`를 작성해서 활용해야한다.

```java
public static void swap(List<?> list, int i, int j) {
	 swapHelper(list, i, j);
}

public static <E> void swapHelper(List<E> list, int i, int j) {
	 list.set(i, list.set(j, list.get(i)));
}
```

`swapHelper`의 존재 여부를 몰라도 클라이언트는 `swap 메서드`를 자연스럽게 사용할 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a6e1f5fe-6781-4e84-b331-44b068603e76)

`Collections`의 `swap` 메서드는 와일드 카드 캡쳐 대신에 로 타입을 이용해서 문제를 해결했다. <br>
`swap` 처럼 특정 원소를 다른 인덱스에 삽입하는 `간단한 작업`만 할거라면, `매개변수화 타입`을 사용하는 `제네릭 메서드`로 간단하게 해결하면 좋지 않을까 하는 생각이든다.

- **정리**
1. 조금 복잡하더라도 `와일드카드 타입`을 적용하면 `API가 훨씬 유연`해진다.
2. `PECS 공식`을 기억해서 잘 사용하자
3. `생산자는 extends`, `소비자는 super` 를 사용해야 한다.
4. `Comparable`과 `Comparator`도 모두 `소비자`다.
