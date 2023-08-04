# 아이템14 - Comparable을 구현할지 고려해라

상태: Done

# compareTo 메서드 일반 규약

![스크린샷 2023-08-04 오후 3.28.08.png](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item14/pictures/compareTo%EC%9D%BC%EB%B0%98%EA%B7%9C%EC%95%BD.png?raw=true)

<aside>
💡 compareTo 메서드 일반 규약

- `대칭성`
Comparable을 구현한 클래스는 
모든 x, y에 대해 sgr(x.compareTo(y)) == -sgn(y. compareTo(x))여야 한다.
(따라서 x.compareTo(y)는 y.compareTo(x)가 예외를 던질 때에 한해 예외를 던져야 한다).

- `추이성`
Comparable을 구현한 클래스는 `추이성`을 보장해야 한다. 
즉, (x.compareTo(y) > 0 && y.compareTo(z) > 0)이면 x.compareTo(z) > 0이다’.
- `그냥 지키자!`
Comparable을 구현한 클래스는 모든 z에 대해 
x.compareTo(y) == 0 이면 sgn(x. compareTo(z)) == sgn(y.compareTo(z)) 이다.

- `equals 와 compareTo의 결과는 왠만하면 똑같이!`
(x.compareTo(y) = 0) = (x. equals(y))여야 한다. 
**Comparable을 구현하고 이 권고를 지키지 않는 모든 클래스는 그 사실을 명시해야 한다.** 
다음과 같이 명시하면 적당할 것이다.
“주의: 이 클래스의 순서는 equals 메서드와 일관되지 않다.”
</aside>

사실 equals 를 사용할 때

다른 객체와의 비교를 할 때의 지켜야 할점이 비슷하다.

알아야하는 것은 compareTo 에는 `비교` 라는 속성이 하나 더 들아간다는 것.

그리고 마지막 규약처럼, `equals` 와 `compareTo` 의 결과가 다른 경우로 

BigDecimal 이 나오는 데, 둘의 결과가 다르기 때문에 명시를 해주고 있다.

- 코드예시
    
    ```java
    public class BicDecimalCollectionTest {
        public static void main(String[] args) {
            BigDecimal bd1 = new BigDecimal("1.0");
            BigDecimal bd2 = new BigDecimal("1.00");
            BigDecimal bd3 = new BigDecimal("-3.0");
            BigDecimal bd4 = new BigDecimal("999999999");
            Set<BigDecimal> hashSet = new HashSet<>();
            Set<BigDecimal> treeSet = new TreeSet<>();
    
            System.out.println("(bd1 == bd2) = " + (bd1 == bd2));
            System.out.println("(bd1.equals(bd2)) = " + (bd1.equals(bd2)));
            System.out.println("bd1.hashCode() = " + bd1.hashCode());
            System.out.println("bd2.hashCode() = " + bd2.hashCode());
            System.out.println("(bd1.compareTo(bd2)) = " + (bd1.compareTo(bd2)));
            System.out.println("(bd1.compareTo(bd4)) = " + (bd1.compareTo(bd4)));
    
            System.out.println();
            hashSet.add(bd1);
            hashSet.add(bd2);
    
            treeSet.add(bd1);
            treeSet.add(bd2);
    
            System.out.println("hashSet = " + hashSet);
            System.out.println("treeSet = " + treeSet);
        }
    }
    
    // (bd1 == bd2) = false
    // (bd1.equals(bd2)) = false
    // bd1.hashCode() = 311
    // bd2.hashCode() = 3102
    // (bd1.compareTo(bd2)) = 0
    // (bd1.compareTo(bd4)) = -1
    //
    // hashSet = [1.0, 1.00]
    // treeSet = [1.0]
    ```
    

먼저 `BigDecimal` 에 대해 알아야 하는 게, BigDecimal 은 크게 2가지로 구성이 되어 있다.

1. `Unscaled value`
숫자의 정수 부분을 BigInteger 형태로 저장합니다.
123.456 의 경우, 123456이 Unscaled Value 입니다.
2. `Scale`
소수점 이하의 자릿수를 int 로 저장합니다.
123.456 의 경우, 3 이 Scale 이욉니다.

이때 equals 의 경우 

![스크린샷 2023-08-04 오후 4.12.10.png](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item14/pictures/BigDecimalEquals.png?raw=true)

hashCode 의 경우

![스크린샷 2023-08-04 오후 4.12.50.png](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item14/pictures/BigDecimalHashCode.png?raw=true)

즉 value 와 scale 둘다 고려한 값을 토대로 진행을 하게 된다.

하지만 compareTo 와 같은 경우

![스크린샷 2023-08-04 오후 4.14.13.png](https://github.com/Effective-Java-Study-Team/EffectiveJava/blob/main/EffectiveJavaStudy/Chapter3/CoRaveler/Item14/pictures/BigDecimalCompareTo.png?raw=true)

scale 이 달라도 value 만 같다면 동치라고 판단이 된다고 나와있다.

위 규약처럼 equals 와 compareTo 의 결과값이 다르다고 명시해줌을 알 수 있다.

# compareTo 메서드 작성 요령

1. `객체 참조 필드가 하나뿐인 경우`
2. `핵심 필드가 여러개인 경우`
    1. `if 중첩`
        
        무엇을 먼저 비교하냐가 중요해진다.
        
        이때는 if 중첩을 통해, 사전에 return 0 이 안 나올때까지
        
        코드를 진행시키면 된다.
        
        ```java
        public int compareTo(PhoneNumber pn) {
        	int result = Short.compareTo(areaCode, pn.areaCode);
        	if(result == 0) {
        		result = Short.compareTo(prefix, pn.prefix);
        		if(result == 0) {
        			result = Short.compareTo(lineNum, pn.lineNum);
        		}
        	}
        	return result;
        }
        ```
        
    2. `비교자 정적 메서드 사용`
        1. `기본 타입 필드`
            
            ```java
            static import java.util.Comparator.comparingInt;
            
            public class PhoneNumber {
            		// logics...
            		private static final Comparator<PhoneNumber> COMPARATOR = 
                        comparingInt((PhoneNumber pn) -> pn.areaCode)
                                .thenComparingInt(pn -> pn.prefix)
                                .thenComparingInt(pn -> pn.lineNum);
            
                public int compareTo(PhoneNumber pn) {
                    return COMPARATOR.compare(this, pn);
                }
            ```
            
            장점 : 훨씬 깔끔하다
            
            단점 : 성능이 안 좋다.
            
        2. `객체 참조용`
            
            각각의 함수는 파라미터로 받는 경우가 다음과 같다.
            
            1. `comparing`
                1. 키 추출자를 받아, 자연적 순서이용
                2. 키 추출자+비교자
            2. `thenComparing`
                1. 비교자 
                2. 키 추출자를 받아, 자연적 순서이용
                3. 키 추출자+비교자
    3. `해시코드 값의 차를 기준으로 하는 비교자`
        1. 잘못된 경우 - 추이성 위배
            
            ```java
            static Comparator<Object> hashCodeOrder = new Comparator<>(){
            		public int compare(Object o1, Object o2) {
            				return o1.hashCode() - o2.hashCode();
            		}
            }
            ```
            
            해싱값을 비교할 때 위처럼 비교하게 되면
            
            다음 2가지 문제가 존재한다.
            
            1. `오버플로우`
                
                ```java
                o1.hashCode() == Integer.MAX_VALUE // true
                o2.hashCode() == Integer.MIN_VALUE // true
                ```
                
                라고 할 때, 위 코드에 넣을 경우
                
                오버플로우가 일어나 제대로 된 순서가 나오지 않을 것이다.
                
            2. `부동소수점 계산 오류`
                
                o1, o2 가 다음과 같다고 해보자.
                
                ```java
                double o1 = 3.4;
                double o2 = 3.400;
                ```
                
                위에서의 예시처럼 compareTo 의 경우 둘은 동치라고 할 것이다.
                
                하지만 둘의 hashCode는 value, scale 둘다 고려하기 때문에
                
                hashCode() 의 값이 다르게 나올 것이다.
                
                따라서 hashCode() 에 따른 결과는 다르다고 나오지만
                
                자체 compareTo() 는 둘이 같다고 할 것이다.
                
        2. 정적 compare 사용 (ex. int → Integer)
            
            ```java
            static Comparator<Object> hashCodeOrder = new Comparator<>(){
            		public int compare(Object o1, Object o2) {
            				return Integer.compare(o1.hashCode(), o2.hashCode());
            		}
            }
            ```
            
        3. 비교자 생성 메서드 (ex. Comparator.comparingInt)
            
            ```java
            static Comparator<Object> hashCodeOrder = 
            				Comparator.comparingInt(o -> o.hashCode());
            ```