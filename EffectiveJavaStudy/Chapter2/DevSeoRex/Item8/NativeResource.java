package ArrayProblem;

import java.lang.ref.Cleaner;

public class NativeResource implements AutoCloseable {
    private long nativePeer;

    private static final Cleaner cleaner = Cleaner.create();

    private final Cleaner.Cleanable cleanable;


    public NativeResource() {
        this.nativePeer = initNativePeer();
        this.cleanable = cleaner.register(this, new NativeResourceCleaner(this.nativePeer));
    }

    private static class NativeResourceCleaner implements Runnable {
        private long nativePeer;

        public NativeResourceCleaner(long nativePeer) {
            this.nativePeer = nativePeer;
        }

        @Override
        public void run() {
            // 네이티브 피어를 사용하여 자원을 안전하게 해제하는 작업 수행
            releaseNativePeer(this.nativePeer);
        }
    }

    private native long initNativePeer();
    private static native void releaseNativePeer(long nativePeer);

    @Override
    public void close() {
        cleanable.clean();
    }

}


