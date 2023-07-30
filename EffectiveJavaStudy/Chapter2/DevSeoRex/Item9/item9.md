### 아이템 9 - try-finally 보다는 try-with-resources를 사용하라

- 자바 라이브러리에는 close 메서드를 호출해 직접 닫아줘야 하는 자원이 많다.
    - InputStream, OutputStream, java.sql.Connection 등이 해당한다.

자원 닫기는 클라이언트가 놓치지 쉬워서 예측할 수 없는 성능 문제를 야기하기도 한다. <br>
이런 자원 중 상당수가 finalizer를 안전망으로 활용하고 있지만 그리 믿음직한 방법은 아니다.

전통적으로 자원이 제대로 닫힘을 보장하는 수단으로 try-finally가 쓰였다.<br>
예외가 발생하거나 메서드에서 반환되어도 실행이 보장되기 때문이다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/dafc28ea-2b27-41f3-9340-997a58f6fdbd)

만약 자원이 둘 이상이라면 어떨까? 

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/cd8093d0-ae73-4423-bed6-fc25443c13f6)

자원이 둘 이상이라면 try-finally 방식은 너무 코드가 지저분하다. <br>
물론 코드의 가독성이 떨어지는 것 이외에도 많은 문제가 내포되어 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/46071be6-0c71-4dfa-8504-ef50587866d2)

이 코드에서 문자열을 입력하면 Integer.parseInt 메서드의 예외가 발생한다. <br>
예외는 발생했지만 finally 블럭에서 BufferedReader를 닫아주게 되고, IOException을 던지게 된다. <br>
그렇다면 어떤 일이 일어나는지 확인해보자.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/7e160d4c-70d2-4c61-9bb3-41a051022b1c)

두 번째 예외만 출력되고  첫 번째 예외는 완전히 집어삼켜 버린다.<br>
그렇게 되면 첫 번째 예외에 관한 정보는 남지 않게 되어, 디버깅을 몹시 어렵게하는 주범이 된다.

이 문제를 어떻게 해결할 수 있을까?<br>
AutoCloseable 인터페이스를 구현하는 것으로 간단히 해결할 수 있다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/b86c1aa9-b97e-4dd2-8c8e-74e0d0ad3076)

FakeReader 라는 간단한 클래스를 작성해주었다.<br>
readLine 메서드에서 문자를 강제로 정수 타입으로 형변환하다 에러가 발생하게 되고,<br>
close 메서드를 호출하면 RuntimeException에 감싸져있는 IOException도 터지게 된다.

이 경우 아까와 같이 try-finally 블럭을 이용해 어떻게 동작하는지 보면

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/50b29868-9106-4a34-84ab-7eccdb5b3039)

문자열을 숫자로 강제변환 하다가 생긴 NumberFormatException은 기록되지 않고 두 번째 예외가 <br>
첫 번째 예외를 삼켜버린 것을 알 수 있다.

try-with-resources 구문으로 변경해서 다시 실행해보면

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/32107892-b34c-4b05-aaed-ff38ee764280)

문자열 입력으로 인한 NumberFormatException과 닫는 중 발생한 예외인 IOException이 정확히 콘솔에 찍혀있는 것을 확인할 수 있다.

물론 숨겨진 예외를 확인하는 방법도 존재한다.

![image](https://github.com/Effective-Java-Study-Team/EffectiveJava/assets/91787050/1fca762f-eb6f-4b84-9c37-54b133aebe9e)

Throwable 객체의 getSuppresed 메서드를 활용해서 숨겨진 예외들의 정보도 출력할 수 있다. <br>
이 방법은 try문을 내부에 중첩해야 한다는 점에서 **AutoCloseable을 구현한 try-with-resources 보다** <br>
**좋은 방법은 아니다.**

정리 → 꼭 회수해야 하는 자원을 다룰때는 try-with-resources를 사용하자.<br>

try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, try-with-resources는 <br>
정확하고 쉽게 자원을 회수할 수 있다.

try-with-resources를 사용하는 것 만으로 개발자의 실수로 close를 명시적으로 호출하지 않아, <br>
안전망이 자원을 회수해줄 것으로 기대만 하고 있는 상황은 생기지 않을 것이다.






