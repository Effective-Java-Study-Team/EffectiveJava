### 아이템 14 - Comparable을 구현할지 고려하라

- **Comparable을 구현하는 이유**
    
    Comparable을 왜 구현해야 할까?
        
    Comparable의 단일 메서드인 compareTo는 단순 동치성 비교에 더해 순서까지 비교할 수 있고, 제네릭하다. <br>
    Comparable을 구현했다는 건 그 클래스의 인스턴스들에는 자연적인 순서가 있음을 뜻한다.
        
    모든 기본 타입과 우리가 자주 사용하는 String과 같은 불변 객체도 **Comparable이 구현되어 있기 때문에**  <br>
    **쉽게 정렬할 수 있는 것이다.**
        
    자바 플랫폼 라이브러리의 모든 값 클래스와 열거타입은 Comparable을 구현하고 있다.  <br>
    알파벳, 숫자, 연대 같은 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable을 구현해야 한다.
        
    compareTo 메서드도 equals 처럼 일반 규약이 존재한다.

    ![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/095fe1c2-a59d-4a84-ae29-53cfcab58c3d)

    - 이 객체가 주어진 객체보다 작으면 음의 정수를, 같으면 0을, 크면 양의 정수를 반환한다.
      - 이 객체와 비교할 수 없는 타입의 객체가 주어지면 ClassCastException이 발생한다.
    - Comparable을 구현한 클래스는 sgn(x.compareTo(y)) == -sgn(y.compareTo(x))
      - **x.compareTo(y)는 y.compareTo(x)가 예외를 던질때에 한해 예외를 던져야 한다.**
    - Comparable을 구현한 클래스는 추이성을 보장해야 한다.
      - 이는 equals 규약과 동일하다.
    - Comparable을 구현한 클래스는 모든 **z에 대해 x.compareTo(y) == 0 이면 sgn(x.compareTo(z)) <br>** **== sgn(y.compareTo(z))다.**
      - x와 y가 같다면 x와 y를 z와 비교한 각 결과도 같아야 한다는 것이다.
    - x.compareTo(y) == 0 이면 x.equals(y) == true 여야 한다.
      - 이 권고는 필수는 아니지만 equals가 같은 객체로 판단한다면 **hashCode 역시 같은 값을 반환하듯이 compareTo 메서드도 0을 반환하여 동치임을 보장해주는 것이 좋아보인다.**
     
- **compareTo 규약을 지켜야 하는 이유**

compareTo 규약을 지키지 못하면 비교를 활용하는 클래스가 제대로 동작하지 않는다. <br>
정렬된 컬렉션인 TreeSet & TreeMap, 검색과 정렬 알고리즘을 활용하는 Collections와 Arrays가 대표적이다.

compareTo 규약 또한 equals 규약 처럼 **반사성, 대칭성, 추이성**을 충족해야 한다. <br>
물론 compareTo 또한 구현한 클래스를 확장 해 값 컴포넌트를 추가하면 규약을 지킬 방법이 없다. <br>
equals와 똑같이 **추상 클래스를 이용하거나 합성(Composition)을 이용해서 우회하는 방법이 최선이다.**

**compareTo의 순서와 equals의 결과가 일관되지 않으면** 정렬된 컬렉션에 이 객체를 넣었을때 해당 컬렉션이 <br>
구현한 인터페이스에 정의된 동작이 제대로 수행되지 못할 것 이다.

TreeSet은 내부적으로 TreeMap을 사용하고, TreeSet에서 새로운 원소를 추가할때 TreeMap의 put 메서드를 사용한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/8be3fca8-ad30-44aa-849a-918251a85150)

TreeMap에서 동치성 비교를 할때 compareTo를 사용하는 것을 볼 수 있다. <br>
따라서 Comparable을 구현할때 규약에 맞도록 **제대로 구현해주지 않으면 원하는 동작을 수행하지 않을 수 있다.**

```java
BigDecimal decimal1 = new BigDecimal("1.0");
BigDecimal decimal2 = new BigDecimal("1.00");


System.out.println(decimal1.equals(decimal2)); // false
System.out.println(decimal1.compareTo(decimal2)); // 0 -> 동치
```

왜 equals와 compareTo가 의미하는 바가 다르게 구현되어 있을까?
BigDecimal 클래스는 equals 비교해서 스케일(정밀도)이 다르면 다른 객체로 판단하고 있다.

**반면에 compareTo 메서드는 값은 동일하지만 스케일이 다른 경우에 같은 객체로 판단하고 있어, 판단 기준이**
**달라 다른 결과를 내고 있다.**

이렇게 되면 어떤 문제가 생길까?

```java
BigDecimal decimal1 = new BigDecimal("1.0");
BigDecimal decimal2 = new BigDecimal("1.00");

Set<BigDecimal> hashSet = new HashSet<>();
Set<BigDecimal> treeSet = new TreeSet<>();

hashSet.add(decimal1);
hashSet.add(decimal2);

treeSet.add(decimal1);
treeSet.add(decimal2);

System.out.println(hashSet.size());  // 2
System.out.println(treeSet.size());  // 1
```

어떤 컬렉션의 구현체를 사용하는지에 따라서 원소의 개수가 변하는 것을 볼 수 있다. <br>
TreeSet은 스케일만 다르고 값이 같은 두 객체를 동치라고 판단해서 중복으로 보고 넣지 않을 것이기 때문이다.

**이처럼 equals와 compareTo가 객체를 다른 기준으로 판단한다면 문제가 생길 여지가 있다.**

- **Comparable의 compareTo 작성 요령**

Comparable은 타입을 인수로 받는 제네릭 인터페이스이기 때문에 compareTo 메서드의 인수 타입은 컴파일 타임에 정해진다.

따라서 인수의 타입이 잘못되면 컴파일 자체가 되지 않는다. <br>
compareTo 메서드는 각 필드가 동치인지를 비교하는 것이 아니라 그 순서를 비교하는 것에 목적이 있다.

Comparable을 구현하지 않은 필드나 표준이 아닌 순서로 비교해야 한다면 비교자(Comparator)를 대신 사용한다. <br>
비교자는 직접 만들거나 자바가 제공하는 것을 사용하면 된다.

```java
public class StringInsensitive implements Comparable<StringInsensitive> {

    private final String s;


    public StringInsensitive(String s) {
        this.s = s;
    }
    

    // 비교자는 직접 만들거나 자바에서 제공하는 것 중 선택해 사용할 수 있다.
    @Override
    public int compareTo(StringInsensitive cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }
}
```

Comparable<StringInsensitive>을 구현해서 StringInsensitive 참조와만 비교할 수 있게 되었다.

Java 7 이전에는 compareTo 메서드에서 정수 기본 타입 필드를 비교할때 관계 연산자 <와 >로 비교를 수행해야 했었다. <br>
그런데 Java 7 부터는 박싱된 기본 타입 클래스들에 compareTo 메서드가 추가되어 그럴 필요가 없어졌다.

compareTo 메서드에서 관계 연산자 <와 >를 사용하는 방식은 거추장스럽고 오류를 유발한다.

```java
// before

@Override
public int compareTo(Number num) {
     return num.x == this.x ? 0 (num.x > this.x ? -1 : 1);
}

// after

@Override
public int compareTo(Number num) {
		return Integer.compareTo(this.x, num.x);
}
```

이전에는 삼항 연산자를 중첩해서 직접 코드를 작성해야 하기 때문에 프로그래머의 실수로 잘못된 결과가 나올 수 있었다. <br>
**박싱된 기본 타입 클래스들의 compareTo를 사용하면 이런 문제를 해결할 수 있고 코드의 가독성까지 챙길 수 있다.**

물론 Integer나 Long 클래스와 같이 실수 타입이 아닌 박싱된 기본 타입 클래스들의 compareTo는 내부적으로 관계 연산자를 이용해 비교를 수행한다.
![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/4f7c8a40-3a89-4279-b323-c6a6bc1ee081)

클래스에 핵심 필드가 여러개 일 경우 어떤 것을 먼저 비교하느냐가 중요하다. <br>
가장 핵심적인 필드부터 비교해서, 비교 결과가 0이 아니라면 순서가 결정된 것이므로 거기서 끝이다.

```java
@Override
public int compareTo(PhoneNumberCompare pn) {
		// 중요한 필드 순서대로 비교를 수행한다.
		int result = Short.compare(areaCode, pn.areaCode);
		
		if (result == 0) result = Short.compare(prefix, pn.prefix);
		if (result == 0) result = Short.compare(lineNum, pn.lineNum);
		
		return result;
}
```

이렇게 작성할 수도 있지만, Comparator 인터페이스가 제공하는 비교자 생성 메서드를 이용하면 메서드 체이닝 <br>
방식으로 간단하게 비교자를 생성할 수 있다.

```java
// 메서드 연쇄 방식의 정적 비교자 생성 메서드를 이용한 방법
private static final Comparator<PhoneNumberCompare> COMPARATOR =
            Comparator.comparing((PhoneNumberCompare pn) -> pn.areaCode)
            .thenComparingInt(pn -> pn.prefix)
            .thenComparingInt(pn -> pn.lineNum);
```

이제 compareTo 또는 compare 메서드에서 그저 [COMPARATOR.compare](http://COMPARATOR.compare) 메서드를 호출하기만 하면 <br>
정해진 순서대로 비교가 수행되게 되는 것이다.

물론 이 방식에도 단점이 있으니, 약간의 성능 저하가 뒤따른다는 점이다.
데이터가 너무 많아서 10% 정도의 성능 저하도 양보할 수 없다면 아까와 같은 방식을 사용해야겠지만 그것이 아니라면 **생산성과 가독성을 챙긴 비교자 생성 메서드 방식도 좋아보인다.** <br>

해시코드 값의 차를 기준으로 하는 비교자를 사용하는 경우도 있는데 이러면 추이성을 위배할 수 있다. <br>
이 방식은 **정수 오버플로**를 일으키거나 **부동소수점 계산 방식**으로 인한 문제를 일으킬 수 있다.

- **정수 오버플로 문제**

```java
// 정수 오버플로우로 인한 비교 문제
HashCodeCompare instance1 = new HashCodeCompare(Integer.MIN_VALUE);
HashCodeCompare instance2 = new HashCodeCompare(Integer.MAX_VALUE);


List<HashCodeCompare> compareArrayList = new ArrayList<>();
compareArrayList.add(instance1);
compareArrayList.add(instance2);

System.out.println("compareArrayList = " + compareArrayList);

compareArrayList.sort(HashCodeCompare.HASH_COMPARATOR);

System.out.println("compareArrayList = " + compareArrayList);

// instance1의 해시코드 값이 더 작은데 instance1이 더 크다고 결과가 나온다
System.out.println(HashCodeCompare.HASH_COMPARATOR.compare(instance1, instance2));
```

instance1이 더 작은 해시코드 값을 가지고 있지만 MAX_VALUE에 대한 빼기 연산을 하면서 정수의 표현범위를 <br>
벗어나서 양수가 되어 더 큰 값이라는 잘못된 판단을 한다.

- **부동 소수점 계산 문제**

```java
List<Double> doubleList = new ArrayList<>();
        doubleList.add(1.1321231324213);
        doubleList.add(1.1233333333333312);
        doubleList.add(1.1233333333333322);

 // 부동 소수점 계산 방식으로 인해 값의 손실을 불러와 결과가 제대로 나오지 않는다.
 Collections.sort(doubleList, new DoubleComparator());

  for (Double num : doubleList) {
      System.out.println(num);
  }


class DoubleComparator implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        return (int) (o1 - o2); // 부동 소수점 연산으로 인한 문제
    }
}
```

부동 소수점 연산으로 인해 제대로 된 정렬 결과가 나오지 않는다.
따라서 [Double.compare](http://Double.compare) 또는 Comparator.comparingDouble 메서드를 활용해 비교를 해줘야한다.

```java
class RefactorComparator implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }
}

static Comparator<Double> COMPARATOR = 
															Comparator.comparingDouble(Object::hashCode);
```

- **정리**

해시코드 값의 차를 기준으로 하는 비교자를 사용하지 말자. <br>
해시코드 비교를 기반으로 비교를 할 것이라면 정적 compare 메서드를 활용한 비교자 또는 비교자 생성 메서드를 활용한 비교자를 사용하자.

```java
// 정적 compare 메서드를 활용한 비교자
static Comparator<Object> hashCodeOrder = 
						(c1, c2) -> Integer.compare(c1.hashCode(), c2.hashCode());


// 비교자 생성 메서드를 활용한 비교자
static Comparator<Object> hashCodeOrder = 
						Comparator.comparingInt(o -> o.hashCode());
```



