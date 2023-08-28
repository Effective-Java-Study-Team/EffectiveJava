# 29장 - 이왕이면 제네릭 타입으로 만들라

태그: Done

# 굳이 제네릭으로 만들어야 하는 이유

해당 타입(클래스, 인터페이스) 를 가져다 쓰는 클라이언트가 

캐스팅을 하지 않아도 되서 안전하다.

# 그렇다면 제네릭 타입으로 만드는 이유?

타입 매개 변수를 추가하면 된다.

```java
public class Stack {...}
⬇️
public class Stack<E> { ... }
```

# 기존의 클래스를 제네릭 클래스로 만든다면?

이와 같이 Stack 이 있다고 해보자.

```java
public class MyStack {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public MyStack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object o) {
        ensureCapacity();
        elements[size++] = o;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null;
        return result;
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

위에서 말한 제네릭 타입으로 바꾸면 다음과 같다.

```java
public class MyStack<T> {
//...
private T[] elements;
//...
public GenericMyStack1() {
		elements = new T[DEFAULT_INITIAL_CAPACITY];
}

public void push(T o) {
        ensureCapacity();
        elements[size++] = o;
}

public T pop() {
	if (size == 0)
		throw new EmptyStackException();
		T result = elements[--size];
		elements[size] = null;
		return result;
}
```

다만 이렇게 바꾸면 빨간 줄, 노란 줄이 잔뜩 뜨게 되는 데,

확인이 되는 가? 된다면 스킵해도 된다.

여러가지가 있지만 일단 빨간 줄은 

`elements = new T[DEFAULT_INITIAL_CAPACITY]` 이 부분이다.

왜냐하면 실체화 불가 타입으로 배열 생성은 되지 않기 때문이다.

따라서 이를 해결하기 위한 솔루션들을 알아보자.

# 기존 → 제네릭 시에 생기는 문제 해결법

## 솔루션 1 - 제네릭 배열 생성을 금지 제약 대놓고 우회

제네릭 타입의 배열 생성을 하면 안되지만,

이를 그냥 대놓고 우회하는 방법이 있다.

```java
private E[] elements;
//...

public Stack() {
	elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
}
```

생성 시에 Object 로 하고, 이를 casting 한다.

하지만 이렇게 하게 된다면 `unchecked casting` 경고가 나오게 된다.

따라서, 이를 피하기 위해 `@SuppressWarning("unchecked")` 를 사용하면 된다.

다만 이전의 아이템에서 나온 것처럼,

이때는 무조건 주석으로 어째서 경고를 무시해도 되는 지 적어줘야 한다.

```java
// 1. elements 는 private
// 2. push 로 T 타입만 들어온다.
// -> 따라서 unchecked 는 보장됩니다.
@SuppressWarnings("unchecked")
public GenericMyStack1() {
		elements = (T[]) new Object[DEFAULT_INITIAL_CAPACITY];
}
```

## 솔루션 2 - elements 를 E[] → Object[] 로 바꾸기

```java
private E[] elements;
↓
private Object[] elements;
```

이렇게 하게 된다면, `pop` 메서드에서 문제가 생긴다.

Object 를 꺼냈는 데, T 로 캐스팅을 하기 때문에!

하지만 우리는 솔루션 1에서 주석으로 달은 것처럼,

캐스팅에 문제가 없음을 안다.

따라서 pop 메서드에 주석와 `@SuppressWarning("unchecked")` 을 달고 해결하면 된다.

```java
		// 1. elements 는 private
    // 2. push 로 T 타입만 들어온다.
    // -> 따라서 unchecked 는 보장됩니다.
    public T pop() {
        if (size == 0)
            throw new EmptyStackException();
        @SuppressWarnings("unchecked") T result = (T) elements[--size];
        elements[size] = null;
        return result;
    }
```

# 번외) 제네릭의 근본문제점, 기본타입 불가

[Why don't Java Generics support primitive types?](https://stackoverflow.com/questions/2721546/why-dont-java-generics-support-primitive-types#answer-2721557)

자바의 제네릭은 이전 버전과의 호환을 위해

런타임시에는 없어진다.

즉, 제네릭은 Object 로 바뀌게 되고,

다운캐스팅을 위한 코드가 삽입된다.

Object 를 다운캐스팅 해야 하니 기본형은 이에 해당이 불가능하다.