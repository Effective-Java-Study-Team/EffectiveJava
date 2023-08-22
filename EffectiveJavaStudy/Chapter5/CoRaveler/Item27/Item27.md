# 27장 - 비검사 경고를 제거하라

태그: In progress

# 1. 비검사 경고란?

<aside>
💡 제네릭을 사용할 때 컴파일러는 타입 단정성을 확보하려고 하지만,
이를 확신할 수 없을 때 비검사 경고(이하 unchecked warning) 을 일으킨다.

</aside>

# 2. unchecked warning 제거의 장점

1. 타입 안정성 확보
2. 즉, 런타임에 ClassCastException 이 발생할 일이 없다.

# 3. warning 을 무시해도 되는 경우와, 그 방법

1. `타입이 안전하면 SuppressWarning 달고 주석을 붙이자!`
- 경고를 제거할 수는 없지만, `타입이 안전하다고 확신`할 수 있다면
`@SuppresWarning(”unchecked”) 를 달고` 경고를 숨기자
- 이때 이 경고를 무시해도 `안전한 이유를 항상 주석`으로 남겨야 한다.
    
    
    다음은 `Collections` 의 `reverse` 메서드이다.
    
    ```java
    @SuppressWarnings({"rawtypes", "unchecked"}) 
    public static void reverse(List<?> list) {
            int size = list.size();
            
    				if (size < REVERSE_THRESHOLD || list instanceof RandomAccess) { // RandomAccess 는 임의접근이 가능한 
    																																				// 자료구조들의 마커 인터페이스
                for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
                    swap(list, i, j);
            } else {
                // instead of using a raw type here, it's possible to capture
                // the wildcard but it will require a call to a supplementary
                // private method
                ListIterator fwd = list.listIterator(); // rawtypes
                ListIterator rev = list.listIterator(size); // rawtypes
                for (int i=0, mid=list.size()>>1; i<mid; i++) {
                    Object tmp = fwd.next();
                    fwd.set(rev.previous()); // unchecked
                    rev.set(tmp); // unchecked
    				}
    		}
    }
    ```
    
    ```java
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void swap(List<?> list, int i, int j) { // 직접 인덱스를 통해 교체, -> 임의접근
          // instead of using a raw type here, it's possible to capture
          // the wildcard but it will require a call to a supplementary
          // private method
          final List l = list; // rawtypes
          l.set(i, l.set(j, l.get(i))); // unchecked
    }
    ```
    
    Collections 의 reverse 메서드는 코드의 간결성을 위해  
    
    와일드카드 캡쳐 타입 대신 raw type 을 사용하고, 
    
    이에 대한 설명을 주석으로 남긴 것을 확인할 수 있다.
    
    - Collections.reverse 에 대한 설명
        
        ```java
        int size = list.size();
        
        if (size < REVERSE_THRESHOLD || list instanceof RandomAccess) {
        		for (int i=0, mid=size>>1, j=size-1; i<mid; i++, j--)
                 swap(list, i, j);
        }
        ```
        
        <aside>
        💡 일반적으로 프로그래밍 및 개발에서 "threshold"라는 용어나 변수는
        
        `임계값`이라는 의미를 갖습니다. 
        
        이 임계값은 어떤 조건이나 특성에 따라 
        다른 동작을 수행하기 위한 기준점을 설정하는 데 사용됩니다.
        
        임계값은 다양한 문맥에서 사용될 수 있습니다. 여기 몇 가지 일반적인 예시를 들면:
        
        1. **`이미지 처리`**: 픽셀의 밝기가 임계값보다 높으면 하얀색으로, 낮으면 검은색으로 바꾸는 이진화(binary thresholding) 과정에서 사용됩니다.
        2. **`성능 최적화`**: 특정 연산의 효율을 최적화하기 위해 데이터의 크기나 갯수가 임계값을 초과하면 다른 알고리즘 또는 접근 방식을 사용하는 경우가 있습니다.
        3. **`시스템 모니터링`**: 서버의 CPU 사용량이 임계값을 초과하면 알림을 보내거나 다른 조치를 취하는 시스템에서 사용됩니다.
        4. **`데이터 처리`**: 노이즈 제거나 이상치 탐지에서 임계값보다 크거나 작은 값을 필터링하는 데 사용될 수 있습니다.
        
        따라서 "threshold"라는 변수나 설정은 주어진 문맥에서 
        `어떤 값이나 조건에 도달`했을 때 `특별한 동작을 시작하거나 변경할 기준점`을 제공합니다.
        
        </aside>
        
        1. **`REVERSE_THRESHOLD`**의 경우에는 리스트의 크기가 이 값보다 작으면 
        
        직접적인 방식으로 원소들의 순서를 뒤집는 것이 더 효율적이라고 판단한 것으로 추정할 수 있다.
        
        1. 또는 RandomAccess 가 가능한 경우 `swap` 을 호출한다.
            
            이는 `ArrayList` 같이 내부적으로 배열로 자료룰 구현하는 경우, 
            
            인덱스로 바로 접근이 가능하기 때문에 swap 이 훨씬 빠르다.
            
            하지만 `LinkedList` 의 경우 연결리스트의 형식이라서 일일히 주소를 타야하기 때문에
            
            RandomAccess 가 불가능하기에 `Iterator` 를 사용해야 한다.
            
        
    
1. `SuppressWarnings 애너테이션은 항상 좁은 범위에 적용하자!`
    
    ```java
    		// ArrayList.java
    		@SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            if (a.length < size)
                // Make a new array of a's runtime type, but my contents:
                return (T[]) Arrays.copyOf(elementData, size, a.getClass()); // T[] 라는 정해지지 않은 타입으로의 casting -> unchecked warning
            System.arraycopy(elementData, 0, a, 0, size);
            if (a.length > size)
                a[size] = null;
            return a;
        }
    ```
    
    위에서 첫 번째 if 문의 return 에서의 unchecked warning 이 나오기 때문에
    
    메서드에다 @SuppressWarning 애너테이션을 달았다.
    
    하지만 이는 사실 위 경우를 따지면 
    
    return 에다가만 warning 을 suppress 하려고 한 것이다.
    
    이를 위해 지역변수를 통해 SuppressWarning 의 범위를
    
    좀 더 줄일 수 있다.
    
    ```java
        public <T> T[] toArray(T[] a) {
            if (a.length < size){
    						@SuppressWarning("unchecked") T[] result = (T[]) Arrays.copyOf(elementData, size, a.getClass());
                return result;
    				}
            System.arraycopy(elementData, 0, a, 0, size);
            if (a.length > size)
                a[size] = null;
            return a;
        }
    ```
    
    이렇게 하면 result 부분에다만 국한적으로 
    
    @SuppressWarning 을 달아 명시적으로 정할 수 있다.