# 28장 - 배열보다는 리스트를 사용하라

태그: Done

# 배열과 제네릭 타입의 2가지 차이점

1. `배열은 공변이지만, 제네릭은 그렇지 않다.`
    
    <aside>
    💡 공변이란?
    공변성(covariance)이란 하위 타입(subtype)과 상위 타입(supertype) 간의 관계를 
    배열이나 다른 제네릭 타입에 적용할 수 있다는 것을 의미합니다. 
    
    예를 들어, **`Animal`**이라는 타입이 있고, 
    **`Dog`**과 **`Cat`**이 **`Animal`**의 하위 타입이라고 가정해봅시다. 
    공변성이 있다면, **`Dog`**의 배열은 **`Animal`**의 배열로 취급될 수 있습니다.
    
    즉, 타입 **`T`**가 타입 **`U`**의 하위 타입이면, 
    **`Array<T>`**는 **`Array<U>`**의 하위 타입으로 취급될 수 있습니다.
    
    </aside>
    
    공변의 정의에서도 나왔듯이,
    
    배열은 공변이지만 제네릭은 그렇지 않다.
    
    ```java
    Object[] arr <---> Integer[] arr2
    List<Object> list <-x-> List<Integer> list2
    ```
    
2. `배열은 실체화된다.`
    
    배열은 런타임에도 자기가 담을 원소들의 타입을 계속 트랙킹 하고 있다.
    
    하지만 제네릭은 타입 소거의 방식을 가져가기 때문에 컴파일때만 타입을 확인하고
    
    런타임에는 Object 로 바뀌게 된다.
    

# 자바가 제네릭 배열 생성을 막은 이유?

```java
List<String>[] stringLists = new List<String>[1]; // 에러가 나지만, 된다고 가정하자!
List<Integer> intList = List.of(42);
Object[] objects = stringLists;
object[0] = intList;
String s = stringLists[0].get(0); // 캐스팅 오류가 난다.
```

위 코드로만 보면 어지로울 테니, 도식화된 그림을 보자.

![IMG_0159.jpeg](28%E1%84%8C%E1%85%A1%E1%86%BC%20-%20%E1%84%87%E1%85%A2%E1%84%8B%E1%85%A7%E1%86%AF%E1%84%87%E1%85%A9%E1%84%83%E1%85%A1%E1%84%82%E1%85%B3%E1%86%AB%20%E1%84%85%E1%85%B5%E1%84%89%E1%85%B3%E1%84%90%E1%85%B3%E1%84%85%E1%85%B3%E1%86%AF%20%E1%84%89%E1%85%A1%E1%84%8B%E1%85%AD%E1%86%BC%E1%84%92%E1%85%A1%E1%84%85%E1%85%A1%20d00b961c7e5c4031a8c06441c6854896/IMG_0159.jpeg)

위 상황에서, object[0] 에 의해서 stringLists[0] = intList 이다.

이는 명백히 오류이지만, 컴파일 시에 오류가 나지 않는 다.

이는 제네릭이 컴파일 타임 때 실체화 되지 않기 때문에 일어나는 오류이다.

따라서 자바는 막은 것이다.

- 물론 이를 우회하는 방법이 존재하기는 한다.

# 배열로 형변환 해야 한다면, 리스트를 사용해보자

`E[] 보다는 List<E> 를 사용하자는 말이다.`

1. 이는 성능이 나빠질 수는 있지만,
2. 타입 안정성과 상호운용성이 좋아진다.

```java
public class Chooser {
    private final Object[] choiceArray;

    public Chooser(Collection choices) {
        choiceArray = choices.toArray();
    }

    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];    // 클라이언트가 return 물을 casting 해야 하는 번거로움이 있다.
    }
}
```

이와 같이 생성시에 Collection 을 주면

그 중 하나를 랜덤하게 골라주는 클래스이다.

`choose()` 의 return 결과물을 클라이언트가 매번 캐스팅해줘야 하기 때문에

여간 번거로운 게 아니라 제네릭 타입으로 바꿔줘야 함을 느낄 수 있다.

- 방안1 - 제네릭 배열 생성 금지 법칙 우회하기
    
    ```java
    public class GenericChooser<T> {
        private final T[] choiceArray;
    
    		@SuppressWarning("unchecked")
        public GenericChooser(Collection<T> choices) {
            choiceArray = (T[]) choices.toArray();
        }
    
        public T choose() {
            Random rnd = ThreadLocalRandom.current();
            return choiceArray[rnd.nextInt(choiceArray.length)];
        }
    }
    ```
    
    choiceArray 를 `T[]` 로 만들고, 생성자에서 캐스팅 해준다.
    
    작동은 하지만, 이 방법은 choiceArray 의 모든 원소가 T 타입임을 보장하고,
    
    주석으로 이를 적어야 한다.
    
- 방안2 - 리스트 사용하기
    
    ```java
    public class ReturnListGenericChooser<T> {
        private final List<T> choiceList;
    
        public ReturnListGenericChooser(Collection<T> choices) {
            choiceList = new ArrayList<>(choices);
        }
    
        public T choose() {
            Random rnd = ThreadLocalRandom.current();
            return choiceList.get(rnd.nextInt(choiceList.size()));
        }
    }
    ```
    
    제네릭과 호환이 잘 되는 리스트로 바꿔버리면 
    
    잘만 돌아간다.
    
    하지만 말했듯이, 이는 성능과 상호 운용성을 트레이드 오프한 것이다.
    
    상황에 따라서 맞춰서 써야 한다.
    
    다만, 실제로 ArrayList 는 방안 1를 택한다.