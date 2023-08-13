# 아이템 7. 다 쓴 객체 참조를 해제하라

## 다 쓴 객체로 메모리 누수가 발생하는 케이스

### 1. 자체 메모리를 직접 관리하는 클래스

```java
public class Stack {
	private Object[] elements;
	private int size = 0;
	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	public Stack() {
				elements = new Object[DEFAULT_INITIAL_CAPACITY];
	}
	
	public void push(Object e) { 
				ensureCapacity(); 
				elements[size++] = e;
	}

	public Object pop() { 
			if (size = 0)
					throw new EmptyStackException(); 
			return elements[—size]; 
	}
	
	private void ensureCapacity() {
			 if (elements.length = size)
					elements = Arrays.copyOf(elementsf 2 * size + 1);
	} 
}
```

- 위 Stack 에서 pop 메서드를 사용하여 반환된 원소의 참조는 elements 배열의 ‘활성영역’밖의 참조이자,  ‘다 쓴 참조(obsolete reference)’가 된다. (활성 영역은 인덱스가 size보다 작은 원소들로 구성된다.)
- 이렇게 발생된 다 쓴 참조들은 **가바지 컬렉터(GC)에 의해 회수되지 않아 메모리 사용량을 늘리게되어 성능의 저하**로 이어지게 된다. 심할 경우 디스크 페이징이나 OutOfMemoryError를 일크켜 프로그램이 종료되기도 한다.

```java
public Object pop() {
		if (size == 0)
				throw new EmptyStackException();
		Object result = elements[—size];
		elements[size] = null; //** 다 쓴 참조 해제
		
		return result;
}
```

- GC 입장에서 Stack 내부 비활성 영역에서 참조는 객체도 똑같이 유효한 객체이다.
  비활성 영역이 되는 순간 null 처리해서 해당 객체를 더는 쓰지 않을 것임을 GC에 알려야 한다.
    - null로 다 쓴 참조를 해제할 경우 잘못된 참조 사용을 방지 미연에 방지 할 수 있다.
      NullPointerException 발생으로 프로그램 오류를 조기에 발견 가능하다.
    - 하지만 객체 참조를 null 처리하는 일은 예외적인 경우여아 한다.
- (Stack과 같은)자기 메모리를 직접 관리하는 클래스라면 개발자가 항시 메모리 누수에 주의해야 한다.

### 2. 캐시로 인한 메모리 누수

- 객체 참조를 캐시에 넣은 뒤, 그 객체를 다 쓴 뒤에도 그냥 두는 경우

    ```java
    public static void main(String[] args) {
    
            Map<Animal, String> map = new HashMap<>();
    
            Animal tiger = new Animal("tiger");
            Animal monkey = new Animal("monkey");
    
            map.put(tiger,"호랑이");
            map.put(monkey,"원숭이");
    
            tiger = null;
    
            System.gc();
    
            map.entrySet().forEach(
                    System.out::println
            );
        }
    
    /**
    
    $Animal@7a81197d=호랑이
    $Animal@5ca881b5=원숭이
    
    **/
    ```

    - key에 해당하는 객체(tiger)를 null로 초기화 했음에도 엔트리는 그대로 남아 출력되는 것을 확인할 수 있다.

    ```java
    **WeakHashMap**<Animal, String> map = new WeakHashMap<>();
    
    Animal tiger = new Animal("tiger");
    Animal monkey = new Animal("monkey");
    
    map.put(tiger,"호랑이");
    map.put(monkey,"원숭이");
    
    tiger = null;
    
    System.gc();
    
    map.entrySet().forEach(
            System.out::println
    );
    
    /** 
    
    $Animal@5ca881b5=원숭이
    
    **/
    ```

    - `WeakHashMap` 은 weak reference(약한 참조) 특성을 이용하여 key에 해당하는 객체가 더 이상 존재하지 않을 경우, 엔트리 또한 GC 프로세스를 통해 제거한다.
    - 캐시 외부에서 키를 참조하는 동안만 엔트리가 살아 있는 캐시가 필요한 상황일때 `WeakHashMap` 을 사용해 캐시를 만들자.
    - 캐시를 만들 때 캐시 엔트리의 유효기간을 정확히 정의하기 어렵다. ScheduledThreadPoolExecutor 와 같은 백그라운드 스레드를 활용하여 청소해주거나 캐시에 새 엔트리를 추가할 때 부수작업으로 수행하는 방법이 있다.
        - LinkedHashMap은 removeEldestEntry 메서드를 사용하여 후자의 방식으로 처리한다.

            ```java
            void afterNodeInsertion(boolean evict) { // possibly remove eldest
                LinkedHashMap.Entry<K,V> first;
                if (evict && (first = head) != null && removeEldestEntry(first)) {
                    K key = first.key;
                    removeNode(hash(key), key, null, false, true);
                }
            }
            
            protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
                    return false;
            }
            ```

            - LinkedHashMap에 삽입 후 실행되는 메서드(afterNodeInsertion)에  조건으로 주어진 removeEldestEntry 메서드는 가장 오래된 요소를 제거해야 할지 여부를 판단하는 역할을 하는데, 기본 반환 값이 false이다. 보편적으로 override 하여 map의 사이즈가 원하는 범위를 넘어섰을 경우에 대한 조건을 주는것으로 사용한다.
        - 더 복잡한 캐시를 만들고 싶다면 java.lang.ref 패키지를 직접 활용해야 할 것이다.

### 3. 리스너(listener) 혹은 콜백(callback) 등록으로 인한 메모리 누수

```java
Callee callee = new Callee();
callee.setCallback(arg ->
    System.out.println("입력받은 메세지 > "+arg.getMsg())
);

callee.onInputMessage();
```

- 위 코드와 같이 콜백을 등록 후 처리하고 해지 않으면 콜백이 쌓여간다.
- `callee = null;` 과 같이 초기화하여 해지하거나 WeakHashMap 또는 WeakReference를 이용하여 GC의 회수대상이 되도록 한다.

    ```java
    public class Callee {
        private String msg;
        private WeakReference<Consumer<Callee>> callback;
    
        public Callee(){
            this.msg = "";
            this.callback = null;
        }
    
        public String getMsg(){
            return msg;
        }
    
        public void setCallback(Consumer<Callee> callback) {
            this.callback = new WeakReference<>(callback);
        }
    
        public void onInputMessage(){
            Scanner scanner = new Scanner(System.in);
            this.msg = "";
            System.out.print("메세지를 입력하세요 : ");
            this.msg = scanner.nextLine();
    
            Objects.requireNonNull(this.callback.get()).accept(Callee.this);
    
        }
    ```

    - 클라이언트로 부터 참조가 끊어지면 콜백객체와 WeakReference 객체는 약한 참조로 인해 바로 GC의 회수 대상이 된다.

## 다 쓴 참조를 해제하는 가장 좋은 방법

- 그 참조를 담은 변수를 유효 범위(scope) 밖으로 밀어내는 방식.