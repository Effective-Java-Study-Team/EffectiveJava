# 아이템 10. Equals 는 일반 규약을 지켜 재정의하라

### 1. 기본적으로 Object.equals() 는 Overriding 하지 않는 것이 정답이다.

위 문장을 보고 이런 생각이 들었을 겁니다.
**"뭔 규약을 지켜 재정의하라면서, 애초에 하지 말라니?"**
틀린 말은 아니다만, equals() 오버라이딩은 오류가 나기 쉬운 작업입니다. 

그러기에 Equals 오버라이딩를 고민하고 있는 당신에게
만약 다음 상황들에 쳐해있다면 equals 를 오버라이딩 하지 않는 것을 추천하지 않습니다!

- **각 인스턴스가 고유하다.**
  ex) Thread 클래스
- **인스턴스의 '논리적 동치성' 을 검사할 일이 없다.**
  ex) new Point(1,2), new Point(1,2) 의 경우, 둘이 같다고 해석할 수도 있다.
- **상위 클래스에서 재정의한 equals가 하위 클래스에서도 딱 들어맞는다.**
  ex) AbstractSet -> Set, AbstractList -> List, AbstractMap -> Map
- **클래스가 private 이거나 protected 이고, equals 메서드를 호출할 일이 없다.**

### 이럼에도 불구하고 equal 를 오버라이딩 해야 하는 경우가 있나?

물론입니다.
바로 객체의 동치성, 즉 두 객체가 같은 지에 대한 기준을 따질 때
'논리적 동치성' > '객체 식별성' 인 경우에요.

아마 자바를 처음 배우면서 'My~' 수식어를 붙여가며
자바의 클래스를 본인이 직접 구현하면서 한 두번쯤 코드를 쳐봤을 거에요.
(없다면 있다고 상상하쥬?)

그리고 그 중 여러 기본서에서 많이 드는 예제로는 특정 좌표를 나타내기 위한 Point 클래스를 예로 들어볼께요.
```java
class MyPoint {
  int x, y;

  MyPoint(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

public static void main(String[] args) {
  MyPoint p1 = new MyPoint(1,2);
  MyPoint p2 = new MyPoint(1,2);
  System.out.println(p1.equals(p2));	// 결과는?
}
```

현재 코드상으로는 콘솔창에 `false` 가 나와야 합니다.
하지만 이런 좌표를 나타내는 클래스를 사용할 때, 두 객체의 동치 조건은
보통 `좌표가 같냐` 를 기준으로 합니다.

그렇기에 이런 경우에 MyPoint 는 Object.equals() 를 오버라이딩 해야겠죠.
```java
class MyPoint {
  int x, y;

  MyPoint(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  @Override
 	public boolean equals(Object obj) {	
		if(obj instanceof MyPoint) return false;
    
    // x, y 좌표가 같다면 같다!
    if(this.x == ((MyPoint)obj.x) && this.y == ((MyPoint)obj.y)) return true;	
    
    return false;
  }
}
```

즉, 우리는 equals 를 왠만하면 오버라이딩을 하지 않아야 하지만서도
생각외로 오버라이딩을 해야 하는 순간들이 있습니다.

하지만 위에서 말했듯 equals 오버라이딩은 오류가 날 경우가 많으니
`일반 규약`을 지키며 수행해야 합니다.

### 그럼 일반 규약이 뭔데?

다음은 Object 의 javadoc 에 나온 일반 규약이다
(사진)

1. **반사성(reflexivity)**
   - null 이 아닌 모든 참조 값 x 에 대해, x.equals(x) == true
   - x -> x
2. **대칭성(symmetry)**
   - null 이 아닌 모든 참조 값 x,y 에 대해, x.equals(y) == true 면 y.equals(x) == true 이다
   - x->y 이면 y->x
3. **추이성(transitivity)**
   - null 이 아닌 모든 참조 값 x,y,z 에 대해, x.equals(y) == true, y.equals(z) == true 면 x.equals(z) == true이다
   - x->y, y->z 이면 x->z
4. **일관성(consistency)**
   - null 이 아닌 모든 참조 값 x,y 에 대해, x.equals(y) 를 반복호출 할 때 항상 같은 값(true/false) 를 반환해야 한다.
5. **null-아님 (이 항목은 정확한 명칭이 없다)**
   - null 이 아닌 모든 참조 값 x 에 대해, x.equals(null) == false 이다.

(갈 짤)

뭐 이리 규약이 많고 실제로 지키기나 하나? 라는 생각이 든다면 정상입니다.
(사실 필자의 생각)
너무 많기도 하고, 일단은 각각이 뭔지 자세히 알기보다는 `왜` 필요한지에 대해 알아볼까요?

하지만 위 규약들을 지켜야 하는 이유는 String, 여러 Collection 과 클래스들은
원소로 들어오는 클래스들이 위 규약들이 지켜졌다는 가정하에 설계가 되었다는 것입니다.

### 먼저 내부 코드로 보는 일반 규약을 지켜야 하는 이유를 보자

(사진)

위 코드는 ArrayList 의 contains 내부 코드이다.

contains 메서드에서 indexOfRange 메서드까지 가게 되고
이는 결국 파라미터로 들어온 객체의 equals() 를 통해 해당 ArrayList 가 
특정 원소를 포함하고 있는 지 확인하게 됩니다.

만약 파라미터로 들어온 객체의 equals 가 일반 규약을 제대로 지키지 않아
equals 가 예측하지 못한, 즉 이상한 결과값을 내게 된다면
contains 메서드의 결과물 역시 또한 신뢰하지 못할 결과값을 도출해겠죠.

즉, 처음과 더불어 지금까지의 내용정리는 아래와 같습니다.

1. 기본적으로 Object.equals() 는 Overriding 하지 않는 것이 정답이다.
1. 하지만 해야 한다면, 일반 규약을 지키자.

자 그럼 이제 5 가지 일반 규약에 대해 자세히 알아보자.

### 일반 규약, 그 5 가지에 대해서

1. **반사성 (reflexivity)**

   x -> x, x.equals(x) == true 여야 한다는 규약입니다.

   책에서도 그렇고 사실 이 규약은 안 지키기가 어렵다고 생각합니다....
   ```java
   @Override
   public static equals(Object o) {
     if(this == o) return false;
     //...
   }
   ```

   일부러 이런 코드를 넣지 않는 이상
   만약 좋은 예시가 있다면 댓글 달아주세요!

2. **대칭성 (symmetry)**

   x -> y 이면 y -> x 의 관계가 성립해야 합니다.

   아까의 위에서 Point 클래스만을 생각하면 대칭성도 
   그렇게 어기기가 쉽지 않은 경우라고 생각하기 쉽습니다.
   예시로 어겨버린 경우를 한번 보겠습니다.

   ```java
   import java.util.Calendar;
   import java.util.Date;
   
   class MyDate {
       private int year, month, day;
   
       MyDate(int year, int month, int day) {
           this.year = year;
           this.month = month;
           this.day = day;
       }
   
       @Override
       public boolean equals(Object obj) {
           if (this == obj) {
               return true;
           }
           if (!(obj instanceof MyDate)) {
               // obj가 Date 인스턴스이고 년, 월, 일이 일치하는지 확인합니다.
               if (obj instanceof Date) {
                   Calendar cal = Calendar.getInstance();
                   cal.setTime((Date) obj);
                   return cal.get(Calendar.YEAR) == this.year 
                       && cal.get(Calendar.MONTH) + 1 == this.month 
                       && cal.get(Calendar.DAY_OF_MONTH) == this.day;
               }
               return false;
           }
           // obj가 MyDate 인스턴스이면, 년, 월, 일만 확인합니다.
           MyDate other = (MyDate) obj;
           return year == other.year && month == other.month && day == other.day;
       }
       // 그러나 이 equals 메서드는 대칭성 원칙을 위반합니다.
       // 즉, 같은 날짜를 가진 MyDate 인스턴스 m과 Date 인스턴스 d에 대해,
       // m.equals(d)는 true를 반환할 수 있지만, d.equals(m)는 false를 반환합니다.
       // 왜냐하면, Date 클래스의 equals 메서드는 시간까지 고려하기 때문에,
       // 날짜는 같지만 시간이 다를 수 있습니다.
   }
   
   ```

   MyDate 클래스는 java.util.Date 인스턴스를 파라미터로 넣을 수 있고
   연, 월, 일이 같다면 true 같다고 판단합니다

   하지만 java.util.Date 의 equals 메서드는 MyDate 와는 다르게
   밀리 세컨드까지 같아야 true 를 return 합니다
   (Date equals 코드)

   

여기까지 길이 길어져, 다음 글에서 이어서 작성하겠습니다!