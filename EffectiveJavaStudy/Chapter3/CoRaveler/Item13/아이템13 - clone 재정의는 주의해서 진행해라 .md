# 아이템13 - clone 재정의는 주의해서 진행해라

상태: Done

# Cloneable 이 문제인 이유

1. `clone 가 선언된 곳이 Cloneable이 아닌 Object 이다`
    
    ![Untitled](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item13/pictures/Cloneable%EC%BA%A1%EC%B3%90.png?raw=true)
    
    즉, Comparable 구현만으로는 clone() 사용이 안됨.
    
    <aside>
    💡 인터페이스를 구현한 다는 것은 일반적으로 
    해당 클래스가 그 인터페이스에서 정의한 기능을 제공 한다고 선언하는 행위다. 
    
    그런데 Cloneable의 경우에는 
    상위 클래스에 정의된 protected 메서드의 동작 방식을 변경한 것이다.
    
    </aside>
    
2. `그마저도 protected이다`

### 그럼 대체 Cloneable 은 왜 있냐?

는 결국 그냥 Cloneable 를 구현하지 않은 클래스의 인스턴스가

clone 을 사용하려고 할 때 CloneNotSupportedException 을 던지는 역할이다.

### clone() 메서드의 일반 규약

![스크린샷 2023-08-04 오후 5.07.37.png](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item13/pictures/clone%E1%84%8B%E1%85%B5%E1%86%AF%E1%84%87%E1%85%A1%E1%86%AB%E1%84%80%E1%85%B2%E1%84%8B%E1%85%A3%E1%86%A8.png?raw=true)

<aside>
💡 **clone() 일반 규약**

이 객체의 복사본을 생성해 반환한다. 

‘복사’의 정확한 뜻은 그 객체를 구현한 클래스에 따라 다를 수 있다. 
일반적인 의도는 다음과 같다. 어떤 객체 x에 대해 다음 식은 참이다.

x.clone() != x

또한 다음 식도 참이다.

x.clone().getClass() == x.getClassO

하지만 이상의 요구를 반드시 만족해야 하는 것은 아니다. 
한편 다음 식도 일반적으로 참이지만, 역시 필수는 아니다.

x.clone().equals(x)

관례상, `이 메서드가 반환하는 객체는 super.clone 을 호출해 얻어야 한다.`
이 클래스와 (Object를 제외한) 모든 상위 클래스가 이 관례를 따른다면 다음 식은 참이다. 

x.clone().getClass() == x.getClass()

관례상, 반환된 객체와 원본 객체는 독립적이어야 한다. 
이를 만족하려면 super.clone으로 얻은 객체의 필드 중 
하나 이상을 반환 전에 수정해야 할 수도 있다.

</aside>

이 모든 재앙은 `이 메서드가 반환하는 객체는 super.clone 을 호출해 얻어야 한다` 에서 시작한다.

# 그렇다면 clone() 을 구현해보자

1. `가변 상태를  참조하지 않는 클래스용 clone 메서드`
    
    ```java
    public class PhoneNumber implements Cloneable{
        int areaCode, prefix, lineNum;
    
        public PhoneNumber(int areaCode, int prefix, int lineNum) {
            this.areaCode = areaCode;
            this.prefix = prefix;
            this.lineNum = lineNum;
        }
    
    		// clone method...
    }
    ```
    
    위 PhoneNumber 현재 int형 필드 areaCode, prefix, lineNum 3가지이다.
    
    ```java
    @Override
    public PhoneNumber clone() {
    	return (PhoneNumber) super.clone():
    }
    ```
    
    오버라이딩의 규칙상 return 타입이 같아야 하지만
    
    자바 7부터의 공변 반환 타이핑(covariant return typing) 덕분에
    
    위처럼 리턴 타입을 자기 자신으로 지정할 수 있다.
    
2. `가변 객체를 참조하는 필드가 존재할 때`
    
    ```java
    public class StackTest implements Cloneable {
        private Object[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;
    
        public StackTest() {
            this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
        }
    
        public void push(Object e) {
            ensureCapacity();
            elements[size++] = e;
        }
    
        public Object pop() {
            if (size == 0) throw new EmptyStackException();
            Object result = elements[--size];
            elements[size] = null; // 다 쓴 참조 해제 return result;
            return result;
        }
    
        // 원소를 위한 공간을 적어도 하나 이상 확보한다.
        private void ensureCapacity() {
            if (elements.length == size) elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
    ```
    
    clone 을 1의 방식으로 구현한다고 하면
    
    super.clone() 을 관례적으로 먼저 호출하게 될 것이다.
    
    ```java
    @Override
    public Stack clone() {
    	return (Stack) super.clone()
    }
    ```
    
    이 경우, `elements` 변수가 가르키는 배열의 주소값은
    
    부모와 자손이 같게 되고, 이는 둘 중 하나를 고치면
    
    다른 하나도 알아서 바뀐다는 얘기이다.
    
    ```java
    @Override
    public Stack clone() {
    	Stack result = (Stack) super.clone(); // 현재 result.elements == super.elements
    	result.elements = elements.clone();
    	return result;
    }
    ```
    
    즉, 이를 방지하기 위해, super.clone() 이후
    
    별도로 배열의 clone 메서드를 호출해줘야 한다.
    
3. `참조 안 참조` 의 형태일 때
    
    ```java
    public class HashTable implements Cloneable { 
    	private Entry[] buckets = •••;
    	
    	private static class Entry { 
    		final Object key;
    		Object value;
    		Entry next;
    
    		Entry(Object key, Object value, Entry next) { 
    		this.key = key;
    		this.value = value;
    		this.next = next; }
    	}
    	... // 나머지 코드는 생략
    }
    ```
    
    HashTable의 경우, buckets 의 형태는 다음과 같다.
    
    `[ 참조값1, 참조값2, ... ]`
    
    즉 2번의 경우처럼 clone 을 해준다고 해도 얕은 복사와 같은 형태라
    
    결국에는 같은 주소를 가지게 되어, 2번의 문제와 동일한 문제가 존재한다.
    
    즉, 일일히 `깊은 복사` 를 해야 한다.
    
    1. `재귀적 clone 메서드`
        
        ```java
        public class HashTable implements Cloneable { 
        	private Entry[] buckets = •••;
        	
        	private static class Entry { 
        		final Object key;
        		Object value;
        		Entry next;
        
        	Entry(Object key, Object value, Entry next) { 
        		this.key = key;
        		this.value = value;
        		this.next = next; 
        
        		Entry deepCopy() {
        			return new Entry(key, value, next == null ? null : next.deepCopy()); // 여기서 재귀호출
        		}
        	}
        	
        	@Override
        	public HashTable clone() {
        			HashTable result = (HashTable) super.clone();
        			result.buckets = new Entry[buckets.length];
        			for(int i = 0 ; i < buckets.length ; i++) {
        					if(buckets[i] != null) {
        							result.buckets[i] = buckets[i].deepCopy();
        			return result;
        	}
        	... // 나머지 코드는 생략
        }
        ```
        
        ![IMG_0150.jpeg](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item13/pictures/deepCopy().jpeg?raw=true)
        
        이렇게 하게 된다면 리스트의 원소 수 만큼 스택 프레임이 사용된다.
        
        즉, 리스트의 길이가 길다면, stackoverflow 를 일으킨다.
        
    2. `연결 리스트를 반복적으로  호출로 복사`
        
        ```java
        Entry deepCopy() {
        	Entry result = new Entry(key, value, next);
        	for (Entry p = result ; p.next != null ; p = p.next) {
        		p.next = new Entry(p.next.key, p.next.value, p.next.next);
        	return result;
        }
        ```
       ![list.jpeg](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item13/pictures/next.next.jpeg?raw=true)
        
    3. `고수준 API 활용`
        
        ```java
        @Override
        public HashTable clone() {
        	HashTable result = (HashTable) super.clone();
        	result.buckets = new Entry[buckets.length];
        	for(Entry entry : buckets) {
        		result.put(entry.key, entry.value);
        	}
        	return result;
        }
        	
        ```
        
        장점 : 코드가 우아하다
        
        단점 : 성능이 안 좋다 / 필드 단위 객체 복사라는 Cloneable 아키텍쳐와 안 어울리다.
        

# clone 상속용 클래스 설계 방식

1. 제대로 작동하는 clone 메서드를 구현해 protected 로 두고 
CloneNotSupportedException 도 던질 수 있다고 선언하기
    
    ```java
    public class SuperClass {
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
    ```
    
    이렇게 하면, 상속할 클래스들은
    
    clone 메서드를 선택적으로 구현하게 된다.
    
2. clone 을 동작하지 않게 구현해놓고, 하위 클래스에서 재정의하지 못하게 할 수도 있다.
    
    ```java
    public class SuperClass {
        @Override
        protected final Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
        }
    }
    ```
    
    이렇게 하면 오버라이딩, 사용 둘다 사용이 불가하다.
    

# clone 대신 해결책

1. 복제 생성자
    
    ```java
    public Foo (Foo f) {
    	// 복제 코드...
    }
    ```
    
2. 복제 팩토리
    
    ```java
    public static Foo newInstance(Foo f) {
    	// 필드 복제 코드...
    }
    ```