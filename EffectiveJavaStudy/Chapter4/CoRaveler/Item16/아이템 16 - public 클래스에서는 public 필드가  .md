# 아이템 16 - public 클래스에서는 public 필드가 아닌 접근자 메서드를 사용해라

태그: In progress

<aside>
💡 public class 의 모든 필드는 private 으로 선언하고,
public 접근자(getter), 수정자(setter) 를 제공하자

</aside>

# 예외

## 1. default 클래스 혹은 private 중첩 클래스

위에서 얘기한 2 가지 경우중 하나라면

필드가 public 이여도 상관없다.

클라이언트 코드 (해당 클래스를 가져다 쓰는 코드) 가

클래스의 내부에 표현이 묶이기는 하지만 

그래봤자 클라이언트도 같은 패키지라서

상관이 없다.

> 개인적인 생각이지만 private 중첩 클래스일 때야 상관없지만

default 클래스라면 그래도 private 필드가 좋을 것 같다.
> 

## 2. public 클래스의 필드가 불변(final)일 경우

`public int num` 같은 필드보다 `불변성 라는 점 때문에`

단점은 줄어들지만, 여전히 좋지 않다.

~~(그냥 public 클래스면 private 박자)~~

Time 클래스 코드 예시를 보자

- 예시 코드
    
    ```java
    public class MyTime {
        public final int hour;   // 0 ~ 23
        public final int minute; // 0 ~ 59
    
        public Time(int hour, int minute) {
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                throw new IllegalArgumentException("Invalid time");
            }
            this.hour = hour;
            this.minute = minute;
        }
    }
    
    // MyTime 클래스의 표현 표준이 아래와 같다면
    MyTime time = new MyTime(15, 30);
    System.out.println("Hour : " + time.hour);
    System.out.println("Minute : " + time.minute);
    // 이렇게 외부코드가 작성이 된다.
    
    // 만약 만약 표준이 초 단위로만 바뀌어야 한다면?
    // 내부, 외부 코드가 다 바뀌어야 한다.
    ```
    
    ```java
    public class BetterTime {
        private final int hour;   // 0 ~ 23
        private final int minute; // 0 ~ 59
    
        public BetterTime(int hour, int minute) {
            // ... (생략)
        }
    
        public int getHour() {
            System.out.println("Hour field was accessed!");  // 로깅 부수 작업
            return hour;
        }
    
        public int getMinute() {
            return minute;
        }
    
    		public int getTime() {
    			//...
    		}
    }
    
    // MyTime 클래스의 표현 표준을 나타내기 위해
    System.out.println(getTime());
    // 이렇게 했는 데
    
    // 추후 MyTime 클래스 표현 표준이 바뀌었다?
    // 위 코드는 그대로 냅두고, BetterTime 만 바뀌어도 된다.
    ```