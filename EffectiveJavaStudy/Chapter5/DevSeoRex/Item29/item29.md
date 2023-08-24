### 제네릭 타입을 사용하지 않는 코드를 제네릭 타입으로 바꾸자

```java
public class MyStack {

    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public MyStack() {
        this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0) throw new EmptyStackException();

        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

간단하게 구현한 `스택`클래스이다. <br>
이 `스택` 클래스는 `제네릭`을 사용하지 않고 `Object`를 기반으로 하고 있는 `클래스`다.

이 클래스는 `제네릭 타입`으로 만들었어야 한다. <br>
이 클래스를 `제네릭 타입`으로 바꿔도 `클라이언트`는 아무런 해가 없다. <br>
지금 상태에서 클라이언트는 스택에서 꺼낸 데이터를 형변환하다가 `ClassCastException`을 만날 수 있다.

```java
public class MyGenericStack<E> {

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public MyGenericStack() {
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

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
```

`E`와 같은 실체화 불가 타입으로는 `배열 인스턴스`를 생성할 수 없다. <br>
따라서 `Object 배열`을 `E` 타입의 배열로 형변환하는 방법을 사용해야 하는데, 이 경우 `컴파일러`가 경고를 보낸다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/962bd8ac-076e-4c4c-bbe4-8de86fbcf6f6)

`비검사 형변환` 경고를 보여주는데, 이런 방법도 사용할 수 있지만 일반적으로 `타입 안전`하지는 않다. <br>
컴파일러는 이 프로그램이 타입 안전한지 증명할 방법이 없기 때문에 경고를 제거할 수 없다.

`elements 배열`은 외부에 `공개`되거나 `공유`되지 않으므로, `push 메서드`를 통해 저장되는 타입은 항상 `E`임을 보장받을 수 있다. <br>
따라서 이 `비검사 형변환`은 항상 `안전`하다.

```java
@SuppressWarnings("unchecked")
public MyGenericStack() {
    this.elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
}
```

이 `비검사 형변환`이 `안전`하다는 것을 증명했기 때문에 범위를 `@SuppressWarnings` 애너테이션을 이용해 `비검사 경고`를 `제거`해준다. <br>
`제네릭 배열` 생성 오류를 해결하는 두 번째 방법은 `elements` 필드의 타입을 `E[]`에서 `Object[]`로 바꾸는 것이다.

```java
private Object[] elements;

public MyGenericStack() {
    this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
}

public E pop() {
    if (size == 0) throw new EmptyStackException();
    		
    E result = (E) elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
}
```

배열의 타입을 `Object 배열`로 변경하고, `pop 메서드`에서 원소를 꺼내올때 `E` 타입으로 `형변환`해주면 된다. <br>
E 타입은 실체화 불가 타입이기 때문에 pop 메서드 역시 경고를 제거해 줘야 한다.

```java
public E pop() {
    if (size == 0) throw new EmptyStackException();
    		
    @SuppressWarnings("unchecked") E result = (E) elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
}
```

이 두가지가 `제네릭 배열` 생성을 제거하는 방법이다. <br>
`첫 번째 방법`은 가독성이 좋고, 코드가 간단해서 `현업`에서 `자주 사용`되는 방식이다.

`힙 오염(Heap Pollution)` 이 마음에 걸리는 사람은 두 번째 방식을 고수한다고 하는데, <br>  
`Java Document` 에서는 `힙 오염`에 대해서 아래와 같이 표현하고 있다.

*`힙 오염`은* `매개변수화`된 유형의 변수가 해당 `매개변수화`된 유형이 아닌 개체를 `참조할 때 발생`한다. <br>
두 번째 방법도 `매개변수화 된 유형의 변수`로 `매개변수화 된 유형이 아닌 개체`를 참조하기에 `힙 오염`이라고 볼 수 있다. 왜 두 번째 방법이 더 안전한지는 `이해하기 어려웠다.` <br>

`Java`의 `제네릭 컬렉션`들은 두 번째 방법을 이용해서 대부분 구현되어 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/25d0bea7-66c9-4886-b183-d67ed01ec810)

- **힙 오염(Heap Pollution)**

힙 오염은 `JVM`의 `힙(Heap) 메모리 영역`에 저장되어있는 특정 변수(객체)가 `불량 데이터`를 `참조`함으로서, 데이터를 가져오려고 할때 `런타임 에러`가 발생할 수 있는 오염 상태를 말한다.

```java
List<String> stringList = new ArrayList<>();
stringList.add("111");
List<Integer> integerList = (List<Integer>) (Object) stringList;
int a = integerList.get(0);
```

이 코드는 예외가 반드시 발생하는 코드다. <br>
그리고 논리적으로도 `List<String>` → `Object` → `List<Integer>`로 형변환되는 이상한 코드다. <br>
근데 이게 왜 컴파일 에러가 발생하지 않을까?

컴파일러는 타입 캐스팅을 꼼꼼히 검사하지 않는다. <br>
그저 `타입 캐스팅`한 `인스턴스`를 할당할 변수에 저장할 수 있는지 `가능 여부`만 `체크`한다. 

힙 오염을 방지하기 위해서 컬렉션을 사용한다면 이렇게 막을 수 있다.

```java
List list = Collections.checkedList(new ArrayList<>(), String.class);

list.add("111");
// Attempt to insert class java.lang.Integer 
// element into collection with element type class java.lang.String
list.add(111);
```

`Collections` 클래스의 `checked… 메서드`는 checked가 붙은 컬렉션을 반환한다. <br>
이 `컬렉션들`은 원소를 저장할때 타입 검사를 진행하기 때문에 저장시 허용되지 않는 타입은 예외가 발생한다.

```java
public void add(int index, E element) {
    list.add(index, typeCheck(element));
}

@SuppressWarnings("unchecked")
E typeCheck(Object o) {
    if (o != null && !type.isInstance(o))
        throw new ClassCastException(badElementMsg(o));
    return (E) o;
}
```

### 배열보다는 리스트를 우선하라와 모순되는 것이 아닌가?

`배열`보다는 `리스트`를 `우선`하라고 했는데 이 내용과 모순되어 보이는 것 같기도 하다. <br>
`제네릭 타입` 안에서 리스트를 사용하는게 `항상 가능`한 것은 아니고, 꼭 더 좋지도 않다.

자바가 리스트를 기본 타입을 제공하지 않기 때문에 `ArrayList`와 같은 `제네릭 타입`도 결국 배열을 사용해 구현해야 한다. <br>
`HashMap`과 같은 `제네릭 타입`은 성능을 위해 배열을 사용한다.

대부분의 `제네릭 타입`은 `타입 매개변수`에 제약을 두지 않는다. <br>
하지만 기본 타입은 사용할 수 없다. `Stack<int>` 나 `Stack<double>`을 만들려고 하면 컴파일 에러가 난다. 

`기본 타입(primitive type)`은 데이터로 취급되어 `Object`의 하위 타입이 아니기 때문이다. <br>
이 문제를 우회하기 위해서 `박싱된 기본타입`을 사용하면 되고, 원소를 기본 타입으로 추가해도 `오토-박싱`을 제공하기 때문에 문제도 해결할 수 있다.

물론 한정적 와일드 카드를 사용하는 경우에는 들어갈 수 있는 타입이 제한된다.

```java
public class MyGenericStack<E extends String> {}
```

이런식으로 코드를 변경한다고 하면, `E`에는 `String`과 그 하위타입만 들어갈 수 있게 되는 것이다. <br>
이런 타입 매개변수 `E`를 `한정적 타입 매개변수`라고 부른다.

- **정리**
1. `클라이언트`에서 직접 `형변환`해야 하는 타입보다 `제네릭 타입`이 더 `안전`하고 쓰기 편하다.
2. 새로운 타입을 설계할 때는 `형변환 없이도` 사용할 수 있도록 하라.
3. 기존 타입 중 `제네릭`이었어야 하는 게 있다면 `제네릭 타입`으로 `변경`하라.
