### 아이템 33 - 타입 안전 이종 컨테이너를 고려하라

### 타입 안전 이종 컨테이너(type safe heterogeneous container)?

`타입 안전 이종 컨테이너`란 컨테이너 대신 키를 매개변수화한 다음, 컨테이너에 값을 넣거나 뺄 때 매개변수화한 키를 함께 제공하는 방식을 뜻한다. <br>
이렇게 하면 얻을 수 있는 장점은, `값의 타입`이 `키`와 같음을 `보장`해준다는 점이다.

```java
public class Favorites {

    private Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(Objects.requireNonNull(type), instance);
    }

    public <T> T getFavorite(Class<T> type) {
        return type.cast(favorites.get(type));
    }
}
```

이 코드를 보면 키값으로 `Class<?>`를 사용하고 있다. `class 리터럴`의 타입이 Class가 아닌 `Class<T>`이기 때문이다. <br>
`컴파일타임 타입 정보`와 `런타임 타입 정보`를 알아내기 위해서 `메서드끼리 주고받는 class 리터럴`을 `타입 토큰` 이라고 부른다.

```java
Favorites favorites = new Favorites();

favorites.putFavorite(String.class, "Java");
favorites.putFavorite(Integer.class, 0xcafebabe);
favorites.putFavorite(Class.class, Favorites.class);

String favoriteString = favorites.getFavorite(String.class);
int favoriteInteger = favorites.getFavorite(Integer.class);
Class<?> favoriteClass = favorites.getFavorite(Class.class);
```

위의 코드는 문제없이 전부 동작한다. <br>
`String` 타입의 값을 요청했는데 `Integer`를 반환하는 일은 절대 일어나지 않는다. 모든 키의 값이 제각각이기 때문에 `여러가지 타입`의 `원소`를 넣을수도 있다.

`map`의 원소가 `Object` 타입으로 선언되어 있기 때문에 어떤 타입도 들어올 수 있지만, <br>
주어진 `키`와 들어오는 `원소`가 관계를 맺고 있기 때문에 `getFavorite 메서드`에서 되살릴 수 있다.

`getFavorite` 메서드는 `Object` 타입의 원소를 반환하므로, `T 타입`으로의 `형변환`이 반드시 필요하다. <br>
`비검사 형변환`을 시도해서 컴파일 경고를 일으키는 것보다는 `cast` 메서드를 사용하는 것이 좋다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/ba079299-833b-44c4-ad35-7a2292a65c45)

물론 내부적으로 `cast` 메서드도 `비검사 형변환`을 시도하기 때문에 경고를 제거하는 것을 볼 수 있다. <br>
`isInstance` 메서드는 `instanceof 연산자`의 `동적 버전`이라고 생각하면 쉽다.

지금의 구현방법은 두 가지 한계점이 있다.

- **악의적인 목적으로 키 값을 로타입으로 삽입**

```java
favorites.putFavorite((Class)Integer.class, "Not Integer");
```

악의적인 클라이언트가 Class 객체를 로 타입으로 넘기면 타입 안전성을 무너뜨릴 수 있다. <br>
이 문제는 아주 간단하게 해결할 수 있다.

```java
public <T> void putFavorite(Class<T> type, T instance) {
    favorites.put(Objects.requireNonNull(type), type.cast(instance));
}
```

`map`에 값을 넣을때도 `cast 메서드`를 호출해 `올바른 타입`의 값을 넣고 있는지 `검사`하면 된다. <br>
이런 방법을 활용하고 있는 컬렉션이 `checked`가 붙은 `컬렉션`들이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/68a975f2-3d2f-4055-9135-babc8969477c)

`CheckedCollection`은 내부적으로 클래스 타입의 타입 정보를 가지고 있고, 원소를 넣을때 `typeCheck` 메서드를 내부적으로 호출해서 타입 검사를한다.

```java
Set set = Collections.checkedSet(new HashSet<>(), String.class);
set.add("aa");
set.add(1);
```

`로 타입`의 변수를 이용해도 지정한 타입의 원소 이외의 값을 넣게되면 `예외`가 발생하게된다.

- **실체화 불가 타입에는 사용불가**

`String`이나 `String[ ]`과 같은 타입은 저장할 수 있지만 `List<String>`, `List<List<String>>`과 같은 타입은 넣을 수 없다는 점이다.

```java
Favorites f = new Favorites();
f.put(List<String>.class, List.of("1")); // Compile Error
```

이 문제는 `슈퍼 타입 토큰`을 이용해서 어느정도 한계를 돌파할 수 있다. <br>
슈퍼 타입 토큰은 `List<String>` 과 같은 실체화 불가 타입은 타입 토큰을 얻어올 수 없고, `로 타입`인 `List`의 `클래스 리터럴`을 얻어오면 값이 덮어 씌워지는 문제를 해결하기 위해 등장한 개념이다.

```java
public class SuperTypeToken {

    static class Sup<T> {
        T value;
    }

    static class Sub extends Sup<Map<List<?>, Set<String>>> {
    }

    public static void main(String[] args) throws Exception {
        Sub b = new Sub();
        Type t = b.getClass().getGenericSuperclass();
        ParameterizedType pType = (ParameterizedType)t;
        System.out.println(pType.getActualTypeArguments()[0]); // java.util.Map<java.util.List<?>, java.util.Set<java.lang.String>>
    }
}
```

`제네릭`은 `런타임`에 타입 정보가 사라지기 때문에 `실체화 불가 타입`의 타입 정보를 유지하기 위해 고안된 방법이다. <br>
`Sub` 클래스는 슈퍼 클래스를 `제네릭 클래스`로 하기 때문에 `런타임`에 접근할 수 있다.

이 방식은 상속을 이용해서 코드가 복잡해질 수 있다. <br>
`추상 클래스`를 이용하는 방식으로 변경하면 클래스를 상속받지 않고도 `슈퍼 타입 토큰`처럼 사용할 수 있다.

`익명 클래스`는 생성하려는 익명 객체의 타입을 `상속`받아 만들어지는 클래스이기 때문이다.

```java
public abstract class TypeReference<T> {

    private Type type;

    public TypeReference() {
        this.type = ((ParameterizedType)getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public Type type() {
        return this.type;
    }
}
```

따라서 이렇게 바꿔주면 `슈퍼타입 토큰`으로서 역할을 해준다.

```java
RefactorFavorite map = new RefactorFavorite();
TypeReference<List<String>> list = new TypeReference<>() {};
map.putFavorite(list, List.of(1, 2, 3));

System.out.println(map.get(list)); // [1, 2, 3]
```

`Favorites` 클래스도 `Type`을 `Key`로 받도록 변경하면 된다.

```java
public class RefactorFavorite {

    private Map<Type, Object> favorites = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void putFavorite(TypeReference<?> typeReference, T instance) {
        favorites.put(Objects.requireNonNull(typeReference.type()), instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(TypeReference<T> typeReference){
        Type type = typeReference.type();

        if(type instanceof Class<?>) {
            return ((Class<T>) type).cast(favorites.get(type));
        }

        return ((Class<T>)((ParameterizedType)type).getRawType())
                .cast(favorites.get(type));
    }
}
```

### 애너테이션 API

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/a72ca47d-49ce-4a9d-a82b-d67dcd77763c)

`런타임`에 `애너테이션`의 정보를 읽어올 수 있는 `getAnnotation` 메서드는 `한정적 타입 토큰`을 인수로 받는다. <br>
`Class<?>` 타입의 객체를 `한정적 타입 토큰`을 받는 `메서드`의 인자로 제공하려면 어떻게 해야할까?

`Class<? extends Annotation>` 으로 형변환하는 것도 하나의 방법이지만 `비검사 형변환 경고`가 뜰 것이다. <br>
따라서 `Class` 클래스의 `asSubClass` 메서드를 호출해 사용하면 된다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/6fd9800d-3227-4e31-aeb7-e27d152a81a6)

물론 내부적으로는 `비검사 형변환 경고`를 제거한 `메서드`다.

이 `메서드`를 살펴보면 형변환이 가능한지 `isAssignableFrom` 메서드로 확인하고 형변환이 가능하다면, <br>
형변환해 반환하고, 그렇지 않다면 예외를 던진다.

- **정리**
1. `컨테이너` 자체가 아닌 `키`를 `타입 매개변수`로 바꾸면 제약이 없는 `타입 안전 이종 컨테이너`를 만들 수 있다.
2. 타입 안전 이종 컨테이너는 `Class`를 `키`로 쓴다.
3. 키로 쓰이는 `Class 객체`를 `타입 토큰`이라고 한다, `Class` 뿐만 아니라 `직접 구현한 키 타입`을 써도 된다.
