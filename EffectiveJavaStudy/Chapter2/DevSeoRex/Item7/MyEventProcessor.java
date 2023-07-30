package ch2.item7;

import java.lang.ref.WeakReference;

public class MyEventProcessor {
    private WeakReference<MyCallback> callback;

    // 콜백을 등록하는 메서드
    void registerCallback(MyCallback callback) {
        this.callback = new WeakReference<>(callback);
    }

    // 어떤 이벤트가 발생했을 때 콜백을 호출하는 메서드
    void doSomethingAndNotify(String message) {
        // 이벤트 처리 로직...
        // 이벤트가 발생하면 콜백을 호출하여 결과를 전달
        if (callback != null) {
            callback.get().onEventOccur(message);
        }
    }

}



