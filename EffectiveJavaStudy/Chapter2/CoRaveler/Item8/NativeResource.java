package CH2.Item8;

import java.lang.ref.Cleaner;

public class NativeResource {
    // 네이티브 자원에 대한 참조변수
    private final long nativeHandle;

    // Cleanable 을 유지하기 위한 참조
    private final Cleaner.Cleanable cleanable;

    public NativeResource() {
        // 네이티브 자원 할당
        this.nativeHandle = allocate();

        // Cleaner 설정
        this.cleanable = Cleaner.create().register(this, new State(this.nativeHandle));
    }

    // JNI 를 통해 네이티브 코드를 호출하여 자원을 할당, 해제합니다.
    // 실제로는 allocate, free 에 해당하는 네이트브 코드를 작성하고
    // 이를 JVM 실행시 (일반적으로) 정적 초기화 블록을 통해 등록해야 합니다.
    private static native long allocate();
    private static native void free(long nativeHandle);

    private static class State implements Runnable {
        private final long nativeHandle;

        private State(long nativeHandle) {
            this.nativeHandle = nativeHandle;
        }

        @Override
        public void run() {
            free(nativeHandle);
        }
    }

    public void close() {
        cleanable.clean();
    }
}
