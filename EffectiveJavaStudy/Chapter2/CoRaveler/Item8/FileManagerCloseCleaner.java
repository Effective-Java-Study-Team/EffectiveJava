package CH2.Item8;

import java.io.File;
import java.lang.ref.Cleaner;

public class FileManagerCloseCleaner implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    private static class State implements Runnable {
        private File file;

        State(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            file.delete();
            System.out.println("File deleted");
            file = null;
        }
    }

    private final State state;
    private final Cleaner.Cleanable cleanable;

    public FileManagerCloseCleaner(String filePath) {
        state = new State(new File(filePath));
        cleanable = cleaner.register(this, state);
    }

    @Override
    public void close() {
        cleanable.clean();
    }
}
