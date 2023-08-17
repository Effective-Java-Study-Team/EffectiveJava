# 아이템 8. finalizer와 cleaner 사용을 피하라

## finalizer란?

- Object 클래스에 포함된 메서드로 객체 소멸자라고도 한다.
- 가비지 컬렉션이 수행될 때 더 이상 사용하지 않는 자원에 대한 정리 작업을 진행 하기 위해 호출되는 종료자 메서드이다.
- 예측할 수 없고, 상황에 따라 위험할 수 있다고 한다. 이펙티브 자바 저자는 ‘쓰지 말아야 한다’라고 표현했다.
- 자바 9에서 사용 자제(deprecated) API로 지정되었다.

<br/>

## cleaner란?

- 자바 9에서 도입된 소멸제로, finalizer가 사용 자제 API로 지정된 후 대안으로 소개되었다.
- finalizer보다는 덜 위험하지만, 여전히 예측할 수 없고, 느리고, 일반적으로 불필요하다고 한다.


<br/>

## 즉시 수행을 보장 받을 수 없다.

- finalizer와 cleaner로는 제때 실행되어야 하는 작업은 절대 할 수 없다.
- finalizer와 cleaner를 얼마나 신속히 수행항지는 전적을 GC 알고리즘에 달렸으며,
  이는 GC 구현마다 천차만별이다.

<br/>

## 수행 여부 조차 보장하지 않는다.

- 자바 언어 명세(JLS)는 finalizer와 cleaner의 수행 시점 뿐 아니라 수행 여부조차 보장하지 않는다.
- 접근할 수 없는 일부 객체에 딸린 종료작업을 전혀 수행하지 못한채 프로그램이 중단될 수도 있다.

  → 이러한 이유로 프로그램 생애주기와 상관없는, 상태를 영구적으로 수정하는 작업에서는 finalizer와 cleaner에 의존해서는 안 된다.

    - ex) 데이터베이스 같은 공유 자원의 영구 락(lock) 해제를 finalizer와 cleaner에 맡겨 놓으면 분산 시스템이 서서히 멈출 것이다.
- System.gc나 System.runFinalization 메서다가 finalizer와 cleaner의 실행 가능성을 높여줄 수 있으나, 보장해주지는 않는다.
    - 이를 보장해주겠다는 메서드들이 있지만 심각한 결함이 있다(ThreadStop).
      ( System.runFinalizersOnExit, Runtime.runFinalizersOnExite)

<br/>

## 동작중에 발생한 예외를 무시되며,  처리할 작업이 남았더라도 종료된다.

- finalizer의 부작용으로 잡지 못한 예외 때문에 해당 객체는 자칫 마무리가 덜 된 상태로 남을 수 있다.
- 보통 예외가 스레드를 중단시키고 스택 추적 내역까지 출력하지만, finalizer에서 일어난다면 경고조차 발생하지 않는다.
- cleaner를 사용하는 라이브러리는 자신의 스레들를 통제하기 때문에 이러한 문제가 발생하지 않는다.

<br/>

## 심각한 성능 문제를 동반한다.

- AutoCloseable 객체를 생성하고 GC가 수거하기까지의 시간보다 finalizer를 사용해 객체를 생성하고 파괴한 시간이 약 50배나 느리게 소요된다.
    - finalizer가 가비저 컬렉터의 효율을 떨어뜨리기 때문이다.
    - 안전망형태로 사용하면 훨씬 빨라진다고 한다. 하지만 이러한 방식도 AutoCloseable 객체 방식에 비해 약 5배 정도 느리다.

<br/>


## 심각한 보안 문제를 일으킬 수도 있다.

- 생성자나 직렬화 과정에서 예외가 발생하면, 이 생성되다 만 객체에서 악의적인 하위 클래스의 finalizer가 수행될 수 있게된다. 이 finalizer는 정적 필드에 자신의 참조를 할당하여 가비지 컬랙터가 수집하지 못하게 막을 수 있다.(finelizer 공격)
- final이 아닌 클래스를 finalizer 공격으로부터 방어하려면 아무 일도 하지 않는 finalize 메서드를 만들고 final로 선언하자.
    - final 클래스들은 그 누구도 하위 클래스를 만들 수 없으니 이 공격에서 안전하다.

<br/>

## AutoCloseable을 사용한 객체 소멸

- 파일이나 스레드 등 종료해야 할 자원을 담고 있는 객체의 클래스에서 finalizer와 cleaner를 대신하기 위해 AutoCloseable을 구현해주고, 클라이언트에서 인스턴스를 다 쓰고 나면 close메서드를 호출하면 된다.

### **Auto closeable 객체란?**

- AutoCloseable인터페이스 구현한 객체이다. try-with-resources를 이용하여 close메서드를 자동으로 호출할 수 있다.
- close 메서드 구현시 구체적인 exception 을 throw 하고, close 동작이 전혀 실패할 리가 없을 때는 exception 을 던지지 않도록 구현하는 좋다.
- close 메서드에서 InterruptedException 을 던지는 것은 좋지 않다. InterruptedExeption 은 쓰레드의 인터럽트 상태와 상호작용하므로 이예외가 억제되었을 때 런타임에서 잘못된 동작이 발생할 수 있기 때문이다.

  > 일반적으로 예외가 억제 되었을 때 문제를 야기시킨다면 그 예외를 던지지 않아야 한다.

- AutoCloseable.close 메서드는 멱등성을 유지하는 것이 필수 적이지는 않다. 즉, 이 메서드가 최초 호출 이후 다시 호출했을 때 side effect 가 발생할 수 있다는 의미이다. 이러한 이유로 되도록이면 멱등성을 유지할 수 있도록 메서드를 구현하는 것 이 좋다.

  > 연산을 여러 번 적용하더라도 결과가 달라지지 않는 성질을 멱등성(idempotent)이라 한다.

<br/>


## finalizer와 cleaner의 쓰임새

1. 자원의 소유자가 close 메서드를 호출하지 않는 것에 대비한 안전망 역할을 한다.
    - 대표적인 사용처 : ~~FileInputStream~~, ~~FileOutputStream~~, ThreadPoolExecutor
        - FileInputStream, FileOutputStream은 close 메서드 구현하는 방식으로 변경됨.
2. 네이티브 피어(native peer)와 연결된 객체에서 사용된다.
    - 네이티브 피어란 일반 자바 객체가 네이티브 메서드를 통해 기능을 위임한 네이티브 객체를 말한다.
    - 네이티브 객체는 자바 객체가 아님을 GC가 그 존재를 알지 못한다. 이러한 이유로 cleaner나 finalizer를 이용한 GC가 회수할 수 있도록 해준다.
    - 성능 저하를 감당 할 수 없거나 네이티브 피어가 사용하는 자원을 즉시 회수해야 한다면 cleaner나 finalizer를 대신하여 close 메서드를 사용해야한다.

<br/>

## 정리

cleaner(자바 8까지는 finalizer)는 안전망 역할이나 중요하지 않은 네이트비 자원 회수용으로만 사용하자. 사용시 불확실성과 성능 저하에 주의해야 한다.