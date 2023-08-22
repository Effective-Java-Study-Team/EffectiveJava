### 아이템 13 - clone 재정의는 주의해서 진행하라

Cloneable은 복제해도 되는 클래스임을 명시하는 용도의 **믹스인 인터페이스(아이템 20)이다.**

가장 큰 문제는 ***clone 메서드가 선언된 곳이 Cloneable이 아닌 Object이고, 그마저도 protected 라는 것이다.*** 

그래서 Cloneable 인터페이스를 구현하는 것 만으로는 외부 객체에서 clone 메서드를 호출할 수 없다. <br>
리플렉션을 사용하면 가능하겠지만 100% 성공을 보장할 수 없다.

해당 객체가 접근이 허용된 clone 메서드를 제공한다는 보장이 없기 때문이다. <br>
이는 Cloneable 인터페이스가 **clone 메서드를 가지고 있지 않아 생기는 문제점이다.**

- **Cloneable 인터페이스의 역할**

메서드 하나 없는 Cloneable 인터페이스는 도대체 무슨 역할을 할까? <br>
Cloneable 인터페이스는 Object의 protected 메서드인 clone의 동작 방식을 결정한다.

**Cloneable을 구현한 클래스**의 인스턴스에서 clone을 호출하면 그 객체의 필드를 하나하나 복사한 <br>
객체를 반환하지만 그렇지 않은 클래스의  경우 **CloneNotSupportedException**을 던진다.

- **Object의 clone 메서드**

Object의 clone 메서드는 JNI를 활용하는 네이티브 메서드로 작성되어 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/f8b6d0c8-e732-4622-aac2-5234e953a504)

**jvm.cpp** 의 일부코드를 가져왔다.

```java
// Just checking that the cloneable flag is set correct
  // cloneable 플래그가 올바르게 설정되어 있는지 확인한다
  if (obj->is_array()) {
    // 모든 배열은 cloneable 이어야 한다. false라면 assert에서 던진다.
    guarantee(klass->is_cloneable(), "all arrays are cloneable");
  } else {
    // 배열이 아니라면 oop여야 한다. (oop는 ordinary object pointer의 약자)
    guarantee(obj->is_instance(), "should be instanceOop");
    // Cloneable 의 서브타입인가? cloneable이 false라면 assert에서 던진다.
    bool cloneable = klass->is_subtype_of(SystemDictionary::Cloneable_klass());
    guarantee(cloneable == klass->is_cloneable(), "incorrect cloneable flag");
  }
```

이 코드를 보면 Cloneable의 서브타입, 즉 Cloneable 인터페이스를 구현하지 않았을 경우 예외가 발생한다. <br>
Cloneable의 구현 여부는 네이티브 메서드 호출시 네이티브 코드에서 확인하고 있다.

- **clone 메서드의 규약**

명세에는 자세히 나와있지 않지만, 일반적인 사용자 입장에서 clone 메서드는 public으로 제공할 것이며, <br>
이를 호출하면 복제가 제대로 이뤄지리라 기대하는 것이 일반적일 것이다.

clone 메서드의 가장 큰 문제는 생성자를 호출하지 않고도 객체를 생성할 수 있다는 점이다.

**Object 클래스에 나와있는 clone 메서드의 규약은 다음과 같다.**

1. x.clone( ) ≠ x
2. x.clone( ).getClass( ) == x.getClass( )
3. x.clone( ).equals( x )

clone 메서드의 일반 규약은 상당히 허술하다. <br>
**‘복사’의 정확한 뜻은 그 객체를 구현한 클래스에 따라 다를 수 있다** 와 같은 표현을 사용하며 정확하게  <br>
규약을 명시해놓은 equals와 hashCode와는 많은 차이를 보인다.

clone 메서드가 반환하는 객체는 super.clone을 호출해 얻는 것이 관례다.  <br>
연쇄적으로 부모의 생성자를 호출해야 하는 생성자 연쇄와 살짝 비슷한 부분이 있다.

다만 **생성자 연쇄는 강제성**이 있고, 호출하지 않을 경우 컴파일 타임에 에러가 발생한다는 점이 다르다. 

super.clone을 호출하지 않고 생성자를 통해 객체를 반환하게 되면 어떤 문제가 생길까?  <br>
B클래스는 A클래스를 상속하고 있고, A 클래스는 Cloneable 인터페이스를 구현하고 있다.

```java
@Override
protected A clone() throws CloneNotSupportedException {
    A a = new A(this.a);
    System.out.println("a's clone  = " + a);
    System.out.println("a's clone a.this = " + this);
    return a;
}

@Override
protected B clone() throws CloneNotSupportedException {
   return (B) super.clone();
}
```

A 클래스에서 super.clone을 통한 연쇄 호출을 하지 않고 생성자로 직접 객체를 생성해 반환하면 어떤 문제가 생기는 걸까?

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/d5bfef8b-7d9e-44fd-9d1f-ebcdf7c0b494)

ClassCastException이 발생하게 된다. <br>
자식 객체를 부모 객체로 형변환 하는 것은 가능하다. 하지만 부모 객체를 자식 객체로 형변환 하는것은 불가능하다.

이 부분은 생성자를 호출할때 콘솔에 찍어보면 조금 더 이해하기 편하다. <br>
clone 메서드 연쇄 호출과 생성자 연쇄는 강제성 여부만 빼고 많은 부분이 닮아있다.

```java
public B(int a, int b) {
   super(a);
   this.b = b;
   System.out.println("b's constructor = " + this);
}

public A(int a) {
   this.a = a;
   System.out.println("a's constructor = " + this);
}
```

B 클래스에서 A클래스의 생성자를 명시적으로 호출하고, B 클래스의 필드를 초기화 하는 전형적인 <br>
생성자 연쇄 코드다. 생성자 안에서 현재 인스턴스인 this를 출력하면 어떻게 출력될까?

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/cd871fba-18d2-4cae-83df-cfc258c2ef60)

a의 생성자에서도 b의 생성자에서도 모두 B 클래스의 인스턴스인 것을 확인할 수 있다. <br>
a의 생성자를 호출했을때는 B 클래스의 필드는 초기화 되지 않았지만 a 클래스의 필드는 초기화 된 것을 볼 수 있다.

그렇다면 clone 연쇄 호출을 했을때는 어떤 결과가 나오는지 확인해보자.

```java
@Override
protected A clone() throws CloneNotSupportedException {
    A a = (A) super.clone();
    System.out.println("a's clone  = " + a);
    System.out.println("a's clone a.this = " + this);
    return a;
}

@Override
protected B clone() throws CloneNotSupportedException {
   B b = (B) super.clone();
   System.out.println("b's clone = " + b);
   System.out.println("b's clone b.this = " + this);
   return b;
}
```

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/1fe06cff-db99-4c4e-bf76-d09730f138c9)

clone 연쇄 호출도 모두 B 클래스의 인스턴스인 것을 확인할 수 있다. <br>
**a.this와 b.this의 주소값이 같고, A의 super.clone과 B의 super.clone의 주소 값이 같다.**

이런 부분을 고려해야 할때는 상속받아 사용하는 클래스가 clone 메서드를 재정의한 경우이다. <br>
**클래스가 final 이라면 걱정해야 할 하위 클래스가 없으니 이 관례는 무시해도 괜찮다.**

final 클래스의 clone 메서드가 super.clone을 호출하지 않는다면 Cloneable도 구현할 필요가 없다.

제대로 동작하는 clone 메서드를 정의한다면 super.clone을 호출해서 타입에 맞게 형변환해서 반환하면 된다. <br>
가변 상태를 참조하지 않는 불변 객체나 기본 타입만 클래스에 있는 경우 clone 메서드는 정상 동작한다.

```java
@Override
public PhoneNumber clone() {
	try {
		return (PhoneNumber) super.clone();
	} catch (CloneNotSupportedException e) {
		 throw new RuntimeException(e); // 일어날 수 없는 일이다.
	}
}
```

쓸데없는 복사는 지양한다는 관점에서 보면 불변 클래스는 굳이 clone 메서드를 제공하지 않는 게 좋다.

- **가변 객체를 참조하는 클래스의 clone**

```java
public class Stack implements Cloneable {
    private Object[] element;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        this.element = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object obj) {
        ensureCapacity();
        element[size++] = obj;
    }

    public Object pop() {
        if (size == 0) throw new EmptyStackException();
        Object result = element[--size];
        element[size] = null;
        return result;
    }

    private void ensureCapacity() {
        if (element.length == size) {
            element = Arrays.copyOf(element, 2 * size + 1);
        }
    }


    @Override
    protected Stack clone() throws CloneNotSupportedException {
        Stack stack = (Stack) super.clone();
        return stack;
    }
}
```

간단하게 구현한 Stack 클래스다. <br>
이 클래스의 인스턴스에서 clone 메서드를 호출하면 어떤 문제가 발생할까?

반환된 인스턴스의 size 필드는 올바른 값을 가지겠지만 element 필드는 원본 Stack 인스턴스와 똑같은 <br>
배열을 참조하는 문제가 발생할 것이다.

clone 메서드는 원본 객체에 아무런 해를 끼치지 않아야 하고 복제된 객체의 불변식을 해쳐서는 안된다. <br>
Stack의 clone 메서드가 제대로 동작하려면 스택 내부 정보를 복사해야 한다.

반복문을 통해서 원소를 전부 복사하는 방법도 있지만, 배열의 clone을 호출하면 간단히 해결할 수 있다. 

```java
@Override
protected Stack clone() throws CloneNotSupportedException {
    Stack stack = (Stack) super.clone();
    stack.element = element.clone();
    return stack;
}
```

배열의 clone은 가장 clone 메서드 본연의 기능을 제대로 사용하는 유일한 예시라고 할 수 있다. <br>
**그래서 배열을 복사할 때는 clone을 사용하라고 권장하는 것이다.**

만약 elements 필드가 final 이었다면 지금 방식은 작동하지 않을 것이다. <br>
Cloneable 아키텍처가 ‘**가변 객체를 참조하는 필드는 final로 선언하라**’는 일반 용법과 충돌하는 것이다.

- **clone을 재귀적으로 호출해도 해결되지 않는 경우**

```java
public class HashTable implements Cloneable{

    private Entry[] buckets = new Entry[30];

    private static class Entry {
        final Object key;
        Object value;
        Entry next;

        Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public HashTable clone() throws CloneNotSupportedException {
        // 복제된 버킷은 자신만의 버킷 배열을 가지지만, 원본과 같은 연결 리스트를 참조하는 문제가 생긴다.
        HashTable result = (HashTable) super.clone();
        result.buckets = buckets.clone();
        return result;
    }
}
```

배열을 사용하고 있고, 배열의 clone 메서드 까지 호출해줬지만 이 클래스를 복사하면 문제가 생긴다. <br>
왜냐하면,  Entry 클래스에서 다음 Entry로 연결하기 위해 Entry 타입의 필드를 가지고 있기 때문이다.

즉 배열은 복사했지만, **Entry의 참조를 그대로 공유하기 때문에(연결 리스트는 그대로 참조) 문제가 발생한다.**

```java
@Override
public HashTable clone() throws CloneNotSupportedException {
     // 복제된 버킷은 자신만의 버킷 배열을 가지지만, 원본과 같은 연결 리스트를 참조하는 문제가 생긴다.
    HashTable result = (HashTable) super.clone();
    result.buckets = buckets.clone();
    
    // 기존 버킷의 Entry가 참조하는 연결 리스트를 순회하면서 Entry 객체를 새로 만들어서 넣어준다.
    result.buckets = new Entry[buckets.length];
    for (int i=0; i<buckets.length; i++) {
         if (buckets[i] != null) {
              result.buckets[i] = buckets[i].deepCopy();
         }
    }
    
    return result;
}

Entry deepCopy() {
// 재귀 호출은 리스트의 원소 수 만큼 스택 프레임을 소비하므로 스택 오버플로를 일으킬 위험이 있다.
      return new Entry(key, value,
          next == null ? null : next.deepCopy());
}
```

Entry의 deepCopy 메서드로 자신이 가진 연결리스트 전체를 복사하기 위해 재귀 호출을 사용한다. <br>
원소가 별로 없다면 재귀 호출도 큰 문제가 없지만 **만약 원소 수가 많다면 스택 프레임을 원소 수 만큼 소비하는** <br> 
**문제가 있기 때문에 스택 오버플로 에러가 발생할 수 있다.**

이 문제를 회피하기 위해서는 재귀 호출 대신 반복자를 써서 순회하는 방향으로 변경하면 된다.

```java
Entry deepCopy() {

// 스택 오버플로를 일으키지 않도록 내부 반복자를 이용하는 것이 좋다.
Entry result = new Entry(key, value, next);
for (Entry p = result; p.next != null; p = p.next) {
    p.next = new Entry(p.next.key, p.next.value, p.next.next);
}

    return result;
}

@Override
public HashTable clone() throws CloneNotSupportedException {
    // 복제된 버킷은 자신만의 버킷 배열을 가지지만, 원본과 같은 연결 리스트를 참조하는 문제가 생긴다.
    HashTable result = (HashTable) super.clone();
    result.buckets = buckets.clone();

   // 기존 버킷의 Entry가 참조하는 연결 리스트를 순회하면서 Entry 객체를 새로 만들어서 넣어준다.
   result.buckets = new Entry[buckets.length];
   for (int i=0; i<buckets.length; i++) {
         if (buckets[i] != null) {
             result.buckets[i] = buckets[i].deepCopy();
         }
    }

    return result;
}
```


- **가변 객체를 복제하는 마지막 방법**

super.clone을 호출하여 얻은 객체의 모든 필드를 초기 상태로 설정한다. <br>
그 후 원본 객체의 상태를 다시 생성하는 고수준 메서드들을 호출한다.

HashTable을 예로 들면, buckets 필드를 새로운 버킷 배열로 초기화 하고 원본 테이블에 담긴 모든 <br>
키 - 값 쌍 각각에 대해 복제본 테이블의 put 메서드를 호출해 둘의 내용이 똑같게 해주며 된다.

고수준 API를 활용해 복제하면 보통은 간단하고 우아한 코드를 얻게 된다. <br>
하지만 아무래도 저수준에서 바로 처리할 때보다는 느리다. 또한 전체적인 Cloneable 아키텍처의 기초가 되는 <br>
필드 단위 객체 복사를 우회한다는 점에서 Cloneable 아키텍처와는 어울리지 않는다.

- **재정의 될 수 있는 메서드를 호출하지 말아야한다.**

clone이 하위 클래스에서 재정의한 메서드를 호출하면, 하위 클래스는 복제 과정에서 자신의 상태를 교정할 <br>
기회를 잃게 되어 원본과 복제본의 상태가 달라질 가능성이 크다.

이 부분은 코드를 보면 매우 쉽다.
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/14436d9a-7af6-4a41-aff1-9c4757a52d9a)

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/66a3452b-555f-4c90-8ecc-0373129d38e6)

부모 클래스의 someLogic은 아무런 로직이 없기 때문에 문제가 없지만, 자식 클래스를 확장하면서 someLogic 메서드에 부모의 필드인 a를 0으로 변경하게 <br>
오버라이딩 했기 때문에 SubClass의 clone을 호출하면 항상 부모의 필드인 a의 값이 0이 되어, 복제본과 원본의 상태가 다를 확률이 크다.

따라서 put 메서드와 clone 메서드 안에서 호출되는 메서드는 private 제어자를 가지거나 final 이여야만 한다.

- **여러가지 clone 메서드에 대한 주의사항**

public인 clone 메서드에서는 throws 절을 없애야 한다. 검사 예외를 던지지 않아야 그 메서드를 사용하기 편리해진다.

**상속용 클래스는 Cloneable를 구현해서는 안된다.** <br>
제대로 동작하는 clone 메서드를 구현해 protected 제어자로 두고 Object 처럼 하위 클래스에서 <br>
Cloneable 구현 여부를 선택할 수 있도록 설계할 수도 있고, <br>
애시당초 **clone을 동작하지 않게 구현해놓고 하위클래스에서 재정의하지 못하게 만들 수도 있다.**

```java
// 하위 클래스에서 Cloneable을 지원하지 못하도록 clone 메서드를 퇴화시킨다.
@Override
protected final Object clone() throw CloneNotSupportException() {
	 throw new CloneNotSupportException();
}
```

**Object의 clone 메서드는 동기화를 염두해두지 않고 만들어졌기 때문에**, super.clone 호출 외에 다른 할 일이 <br>
전혀 없더라도 clone을 재정의하고 동기화해줘야 한다.

Cloneable을 이미 구현한 클래스를 확장한다면 어쩔 수 없이 잘 동작하는 clone 메서드를 구현해야 한다. <br>
하지만 그렇지 않은 경우라면 **복사 생성자와 복사 팩터리라는 더 나은 복사 방식을 제공하면 된다.**

```java
// 복사 생성자
public mixin.Dog(mixin.Dog dog) {
    this.name = dog.getName();
    this.age = dog.getAge();
}

// 복사 팩터리 메서드
public static mixin.Dog newInstance(mixin.Dog dog) {
    return new mixin.Dog(dog.getName(), dog.getAge());
}
```

복사 생성자와 복사 팩터리는 언어 모순적이고 위험천만한 방식으로 객체를 생성하지 않는다. <br>
**또한 정상적인 final 필드 용법과도 충돌하지 않고, 불필요한 검사 예외를 던지거나 형변환도 필요하지 않다.**

복사 생성자와 복사 팩터리는 해당 클래스가 구현한 인터페이스 타입의 인스턴스를 인수로 받을 수 있다.

```java
HashSet<Integer> hashSet = new HashSet<>();
hashSet.add(1);
hashSet.add(2);

TreeSet<Integer> treeSet = new TreeSet<>(hashSet);
```

이렇게 HashSet 객체를 TreeSet 객체로 간단히 복제할 수 있다. <br>
**좀 더 정확한 용어로 이를 변환 생성자와 변환 팩터리라고 부른다.**

클라이언트는 **원본의 구현 타입에 얽매이지 않고 복제본의 타입을 직접 선택**할 수 있는 장점이 있다. <br>
clone 으로는 불가능한 이 기능을 간단히 처리할 수 있는 것이다.

- 정리
    - 새로운 인터페이스를 만들 때는 절대 Cloneable을 확장해서는 안 된다.
    - 새로운 클래스도 이를 구현해서는 안된다.
    - **기본 원칙은 복제 기능은 생성자와 팩터리를 이용하는 것이 가장 좋다는 것!**
    - **clone 메서드 방식이 가장 우아하게 동작하는 건 배열의 clone 방식이다**

결론 → 배열을 복사할때만 clone 메서드를 쓰고 객체의 복사가 필요하다면 복사 생성자나 팩터리를 사용하자






