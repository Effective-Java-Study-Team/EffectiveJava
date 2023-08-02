# 아이템11 - equals를 재정의 하려거든 hashCode도 재정의해라

상태: In progress

<aside>
💡 **equals 를 재정의한 클래스 모두에서 hashCode 로 재정의 해야 한다.**

</aside>

# hashCode() 를 재정의 안하면 생기는 문제

---

1. HashMap, HashSet 과 같은 Collection의 원소들은 제대로 hashCode 가 오버라이딩 되지 않으면, 사용시 문제를 일으킴
2. 위 Collection 들은 **hashCode 일반 규약**을 지키도록 되어 있다.

![IMG_0147.jpeg](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item11/pictures/hashCode%EA%B7%9C%EC%95%BD%EB%B0%91%EC%A4%84.png?raw=true)

<aside>
💡 hashCode() 일반 규약

1. equals 비교에 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 
그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 한다. 
단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없다.

**2. equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 한다.**

3. equals(Object)가 두 객체를 다르다고 판단했더라도, 
두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없다. 
단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블 의 성능이 좋아진다.

</aside>

주로 2번 규칙을 지키지 않아 문제가 많이 터지게 된다.

2번 규칙을 좀 더 쉽게 풀자면

논리적으로 동치인 객체라서 `equals()` 가 true 를 반환한다면

`hashCode()` 도 같은 값을 반환해야 한다는 것이다.

```java
Map map = new HashMap();
map.put(new PhoneNumber(111,222,333));
map.get(new PhoneNumber(111,222,333)); // null
```

논리적으로 동치, 즉 동일한 `new PhoneNumber(111,222,333)` 을 넣었지만

둘의 hashCode 는 다른 값을 반환하기 때문에 null 이 반환됐다.

# hashCode() 구현법

---

1. **그냥 정수 return 하기 (추천도: , 그냥 하지 말아야 한다)**
    
    ```java
    @Override
    public int hashCode() {return 42;}
    ```
    
    hashCode() 실행 속도는 빠를 지언정,
    
    위 Hashing 을 사용하는 Collection 들에서 객체간 구분이 안간다.
    
2. **전형적인 hashCode 메서드 (추천도: ⭐️⭐️⭐️⭐️)**
    
    ```java
    class PhoneNumber {
    	short areaCode, prefix, lineNum;
    	
    	// constructors, logics, ...
    
    	@Override
    	public int hashCode() {
    		int result = Short.hashCode(areaCode);
    		result = 31 * result + Short.hashCode(prefix);
    		result = 31 * result + Short.hashCode(lineNum);
    		return result;
    	}
    }
    ```
    
    **이때 파생 필드, 즉 다른 필드들로 추론이 가능한 필드들**
    
    **혹은 equals 에 사용되지 않은 필드들은 제외해야 한다!** 
    
    **→ 안 지키게 된다면 불필요한 요소가 해시값을 들어가**
    
    **hashCode 2번 규약을 어기게 된다!**
    
    ### 왜 굳이 31을 곱할까?
    
    1. 홀수여서
    2. 소수여서
    
    `31` 이라는 소수는 경험적 근거에 의해 효율적이라서 사용한다고 나와 있다.
    
    소수인건 받아들일 수 있는 데, 홀수인건 왜 도움이 될까?
    
    일단 여기서 말하는 홀수, 쯕 짝수가 아닌 경우를 사용해야 한다는 건
    
    2의 거듭제곱인 수들을 사용하지 말라는 것이다
    
    이는 컴퓨터가 짝수/홀수 를 곱할 때 내부적으로 작동하는 원리를 알아야 한다.
    
    사칙연산은 내부적으로 시프트연산+덧셈의 형태로 바뀌어 진행을 하게 되는 데
    
    곱셈의 경우 2의 거듭제곱을 곱하게 되면 단순 시프트 연산이기 때문에
    
    오버플로가 나는 경우, 비트들이 넘어가 비트(책에서는 이 부분을 비트라고 함)들이
    
    넘어간 만큼 0으로 채워진다
    
    하지만 31의 경우
    
    `5 * 31 = 155` 을 예시로 보자.
    
    ```java
    5  = 00000101 // 이진수
    31 = 10100000
    
    5 * 31 = 5 * (32 - 1)
    			 = 5 * 2^5 - 5 
           = 5 * 2^5 + (-5)
    
    5 * 2^5 = 10100000
    5       = 00000101
    -5      = 11111011 // 5의 2의 보수
    
    이제 여기서 5 * 2^5 + (-5) 를 계산해주면
      10100000
    + 11111011
      --------
      10011011 = 155
    ```
    
3. **Objects.hash 를 이용한 한 줄 짜리 hashCode (추천도: ⭐️⭐️⭐️, 성능⬇️)**
    
    ```java
    @Override
    public int hashCode() {
    	return Obejcts.hash(areaCode, prefix, lineNum);
    }
    ```
    
    가장 깔끔한 방법이지만, 성능이 좋지 않아
    
    성능이 중요하지 않는 경우에만 사용하즌 것이 바람직하다.
    
    ![스크린샷 2023-08-02 오후 3.48.24.png](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item11/pictures/hashCode%EC%84%B1%EB%8A%A5%EB%B9%84%EA%B5%90.png?raw=true)