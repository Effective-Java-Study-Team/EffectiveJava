package ch2.item7;

public class CallBackTest {

    public static void main(String[] args) {
        MyEventProcessor eventProcessor = new MyEventProcessor();

        eventProcessor.registerCallback(message -> System.out.println("콜백 실행 됨 : " + message));

        eventProcessor.doSomethingAndNotify("이벤트 발생!");

        eventProcessor = null;

    }
}

