# 아이템24 - 멤버 클래스는 되도록 static 으로 만들라

태그: Done

<aside>
💡 중첩 클래스란?

다른 클래스안에 정의된 클래스를 의미한다.
좀 더 쉬운 말로는, 위치상 다른 클래스 안에서 선언된 클래스이다.

</aside>

<aside>
💡 종류는 크게 4가지이다.

**1. 정적 멤버 클래스 (이하 static 멤버 클래스)
2. (비정적) 멤버 클래스 (이하 non-static ] 멤버 클래스)
3. 익명 클래스
4. 지역 클래스**

이번 장에서는, 각각의 클래스의 `what`,`when`, `why` 를 알아보자.

</aside>

이번 챕터에서는 외부 클래스는 Outer, 내부 클래스는 Inner 로 설명한다.

# 1. static 멤버 클래스

### What (무엇이고, 어떤 특성을 가졌는 지)

```java
class Outer {
		private int a = 1;

		class Inner {
				private void print() { System.out.println(a); };
		}
}
```

2가지를 제외한다면, 일반 클래스와 다르지 않다.

1. `다른 클래스 안에 선언`
2. `고로, 바깥 클래스의 private 필드에 접근이 가능하다.`

또한 static 멤버 클래스는 일반 static 멤버와 동일한 접근성을 가진다.

### When & Why (주로 언제, 왜 쓰일까?)

흔히 외부 클래스와 함께 쓰일 때만 유용한

public 도우미 클래스로 쓰인다.

- 계산기 클래스에서의 static 멤버 클래스 예시 코드
    
    ```java
    public class Calculator {
        enum Operation {
    			PLUS, MINUS
    		}
    
        public int calculate(int a, int b, Operation operation) {
    				switch (operation) {
                case PLUS:
                    return a + b;
                case MINUS:
                    return a - b;
                default:
                    throw new AssertionError(operation);
            }      
        }
    }
    
    // 사용
    Calculator calculator = new Calculator();
    int result = calculator.calculate(1, 2, Calculator.Operation.PLUS);
    int result2 = calculator.calculate(1, 2, Calculator.Operation.MINUS);
    System.out.println("result = " + result); // result = 3
    System.out.println("result2 = " + result2); // result2 = -1
    ```
    

# 2. non-static 멤버 클래스

### What

선언의 차이로는 static 이 하나 안 붙는다지만,

내부적으로는 차이가 꽤나 있다.

### When & Why

`non-static 멤버 클래스의 인스턴스는 Outer 클래스의 인스턴스와 연길이 된다.`

⇒ non-static 멤버 클래스의 인스턴스에서 `Outer 클래스의 인스턴스에 대한 참조`를 가지고 있다.

따라서 Outer 클래스의 인스턴스의 정보가 필요할 경우 (==참조를 가지고 있어야 하는 경우) 

non-static 클래스를 사용하게 된다.

- MyIterator 코드 예시
    
    ```java
    public class MySet<T> extends AbstractSet<T> {
        ArrayList<T> list = new ArrayList<>();
    
        @Override
        public int size() {
            return list.size();
        }
    
        @Override
        public boolean add(T t) {
            return list.add(t);
        }
    
        @Override
        public Iterator<T> iterator() {
            return new MyIterator();
        }
    
        public class MyIterator implements Iterator<T> {    // 직접 iterator 를 구현해야 하는 경우
            private int currIdx = 0;
    
            @Override
            public boolean hasNext() {
                return currIdx < list.size();
            }
    
            @Override
            public T next() {
                return list.get(currIdx++); // 현재 인스턴스의 정보(이 경우, list 의 정보) 필요함,
            }
        }
    }
    ```
    

이 참조 정보는 non-static 멤버 클래스의 인스턴스가 가지고 있는다.

이는 메모리 공간을 차지하게 되므로, 외부 인스턴스의 대한 참조가 필요없다면

static 멤버 클래스로 전환하자.

- Map, Entry 코드 예시
    
    ```java
    public class BadMapExample {
        public ArrayList<MapEntry> entries = new ArrayList<>();
    
        class MapEntry {
            private Object key;
            private Object value;
    
            public MapEntry(Object key, Object value) {
                this.key = key;
                this.value = value;
            }
    
            public Object getKey() {
                return key;
            }
    
            public Object getValue() {
                return value;
            }
    
            public void setKey(Object key) {
                this.key = key;
            }
    
            public void setValue(Object value) {
                this.value = value;
            }
    
            @Override
            public String toString() {
                String result = "key : " + key + ", value : " + value + ", MapExample = " + BadMapExample.this;
                return result;
            }
        }
    
        public void putEntry(Object key, Object value) {
            entries.add(new MapEntry(key, value));
        }
    
        public void printAllElements() {
            for (MapEntry me : entries)
                System.out.println(me);
        }
    }
    ```
    
    ```java
    BadMapExample bme = new BadMapExample();
    System.out.println("bme = " + bme);
    System.out.println();
    
    for(int i = 1; i <= 5; i++) {
    		bme.putEntry(i, i);
    }
    
    bme.printAllElements(); // 각각의 entry 가 쓸데없이
    												// bme 에 대한 참조를 가지고 있다.
    
    bme = CoRaveler.Item24.BadMapExample@7a81197d
    key : 1, value : 1, MapExample = CoRaveler.Item24.BadMapExample@7a81197d
    key : 2, value : 2, MapExample = CoRaveler.Item24.BadMapExample@7a81197d
    key : 3, value : 3, MapExample = CoRaveler.Item24.BadMapExample@7a81197d
    key : 4, value : 4, MapExample = CoRaveler.Item24.BadMapExample@7a81197d
    key : 5, value : 5, MapExample = CoRaveler.Item24.BadMapExample@7a81197d
    ```
    

# 3. 익명 클래스

### What

익명 객체와 익명 클래스의 정의를 보고 가자.

![IMG_0156.jpeg](/Users/xpmxf4/Desktop/develop/EffectiveJava/EffectiveJavaStudy/Chapter5/CoRaveler/Item24/pictures/3070EF4C-4B28-467E-8D71-4D7615D83E4C_1_201_a.jpeg)

### When & Why

익명 클래스는 

- 쓰이는 시점에 선언과 동시에 인스턴스가 만들어지고,
- 응용하는 데 제약이 많아
- ex) instanceof 검사 or 클래스 이름을 써야하는 경우, 여러 인터페이스 구현x, 클래스 상속x)
- 그닥 자주 쓰이지는 않는 다.

- 그나마 쓰이던 작은 함수 객체 or 처리 객체를 만들 때 사용했는 데
- 이마저도 람다가 대체를 하게 되었다.
- Comparator 코드 예시
    
    ```java
    Collections.sort(words, new Comparator<String>() {
    	public int compare(String a, String b) {
    //		return a.length() - b.length();, 안 좋은 예시!
    		return Integer.compare(a.length(), b.length());
    	}
    }
    ```
    
    ```java
    Collections.sort(words, (a,b) -> Integer.compare(a.length(), b.length()));
    ```
    

# 4. 지역 클래스

### What

- 메서드 내에서 선언되는 지역 클래스
- non static context 에서만 Outer 인스턴스 참조가 가능하다
- static 멤버는 불가능
- 가독성을 위해 짧게 써야 한다.

### When & Why

사실 언제 쓰는 지 잘 모르겠음;