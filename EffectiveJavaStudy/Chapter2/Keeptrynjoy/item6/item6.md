# 아이템 6. 불필요한 객체 생성을 피하라

## 불필요한 객체 생성을 피하는 방법

- 생성자 대신 **정적 팩토리 메서드를 사용해 불필요한 객체 생성을 피하기**
    - 예 : `Boolean(String)` 생성자 대신 `Boolean.valueOf(String)` 팩토리 메서드 사용하는 것이 좋다.
    - 생성자는 호출할 때마다 새로운 객체를 만들지만, 팩토리 메서드는 재사용을 보장한다.
    - 불변 객체만이 아니라 가변 객체라 해도 사용 중에 변경되지 않을 것임을 안다면 재사용할 수 있다.
    - 생성 비용이 ‘비싼 객체’가 반복해서 필요하다면 캐싱하여 재사용하자.
        - Exception 의 생성 비용은 비싸다. trace를 가지지 않는 Custum exception은 경우 캐싱하여 재사용가능하도록 하는 것이 좋다.

            ```java
            public class CustomException extends RuntimeException {
                public static final CustomException INVALID_NICKNAME = new CustomException(ResponseType.INVALID_NICKNAME);
                public static final CustomException INVALID_PARAMETER = new CustomException(ResponseType.INVALID_PARAMETER);
                public static final CustomException INVALID_TOKEN = new CustomException(ResponseType.INVALID_TOKEN);
                //...
            }
            ```

            ```java
            if (StringUtils.isBlank(parameter)) {
                throw WebtoonCoreException.INVALID_PARAMETER;
            }
            ```

          참고 : https://meetup.nhncloud.com/posts/47

        - `Pattern` 은 입력받은 정규표현식에 해당하는 유한 상태 머신(finite state machine)을 만들기 때문에 인스턴스 생성 비용이 높다. 성능 개성을 위해 Pattern인스턴스를 클래스 초기화(정적초기화) 과정에서 직접 생성해 캐싱해두고, 이를 제공하는 메서드가 호출될 때마다 이 인스턴스를 재사용한다.

            ```java
            import java.util.regex.Pattern;
            
            public class EmailChecker {
                private static final Pattern EMAIL = Pattern
                        .compile("^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\\\.[a-z]+$");
            
                static boolean isEmail(String e){
                    return EMAIL.matcher(e).matches();
                }
            }
            ```

            - `isEmail` 메서드를 지연초기화로 불필요한 초기화를 없앨 수 있지만, 이는 코드를 복잡하게 만들며, 성능은 크게 개선되지 않을 때가 많다.

## 불필요한 객체를 만드는 방식

### 방식 1. **불필요한 어댑터 객체를 생성**

- 어댑터(뷰(view)라고도 한다.)는 실제 작업은 뒷단 객체에 위임하고,
  자신은 제2의 인터페이스 역할을 해주는 객체다.

```java
Map<String, Object> map = new HashMap<>();
        
map.put("food","burger");
map.put("cloths","t-shirt");

Set<String> set1 = map.keySet();
Set<String> set2 = map.keySet();

System.out.println(set1.equals(set2)); // true

set1.remove("food");

System.out.println(set1.size()); // 1
System.out.println(set2.size()); // 1
```

- Map 인터페이스의 keySet메서드는 Map 객체 안의 키 전부를 담은 Set 뷰를 반환한다.
- 위 코드에서 반환된 set1과 set2는 같은 인스턴스로, set1에서 변경된사항이 set2에도 반영된 것을 확인할 수 있다.
- 위 결과를 통해 반환된 객체들이 모두 같은 Map 인스턴스를 대변하며, keySet이 뷰 객체를 여러개 만드는 것은 아무런 이득이 없다는 것을 확인할 수 있다.

### 방식 2. 의도치않은 오토박싱으로 불필요한 객체 생성

**오토박싱(auto boxing)**

- 개발자가 기본 타입과 박싱된 기본 타입을 섞어 쓸 때 자동으로 상호 변환해주는 기술
- 오토박싱은 기본 타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.

```java
private static long sum() { 
		Long sum = 0L;
		for (long i = 0; i <= Integer.MAX__VALUE; i++)
				sum += i; // long타입인 i가 Long타입이 sum에 더해질 때마다 Long 인스턴스 생성

		return sum; 
}
```

- sum 변수 선언시 `long`이 아닌 `Long` 으로 선언해서 `Long` 인스턴스가  2^31개나 생성되었다.
- 위와 같이 의도치않은 오토박싱이 숨어들지 않도록 주의해야 한다.

## 추가 내용

### 단순히 객체 생성을 피하기 위한 커스텀 객체 풀(pool)생성 자제

- 최근의 JVM에서는 별다른 일을 하지 않는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다.
- 프로그램의 명확성, 간결성, 기능을 위해서 객체를 추가로 생성하는 것이라면 일반적으로 좋은일이다.
- 데이터 베이스 연결 같은 생성 비용 많이 비싼 경우 객체 풀을 만들어 재사용하는 편이 좋다. 하지만 일반적으로는 자체 객체 풀은 코드를 헷갈리게 만들고 메모리 사용량을 늘리고 성능을 떨어 뜨린다.

### 방어적 복사가 필요한 상황에서의 객체 재사용에 유의

- 불필요한 객체 생성은 코드의 형태와 성능에만 영향을 끼치지만, 방어적 복사가 실패하면 버고와 보안 구멍으로 이어진다.
- 즉, 불필요한 객체 생성을 줄이는 것보다 방어적 복사가 우선시 되어야 한다.