package CH2.Item8;

import java.io.File;

public class FileManagerOnlyClose implements AutoCloseable{
    private File file;

    public FileManagerOnlyClose(String filePath) {
        this.file = new File(filePath);
    }

    @Override
    public void close() throws Exception {
        if(file != null) {
            file.delete();  // 존재
            System.out.println("File Deleted");
            file = null;    // 자원 비할당
        }
    }
}
