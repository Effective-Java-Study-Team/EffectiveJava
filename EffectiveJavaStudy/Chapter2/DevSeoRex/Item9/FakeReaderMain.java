package ArrayProblem;

public class FakeReaderMain {

    public static void main(String[] args) {

        FakeReader fakeReader = new FakeReader();

        try {
            fakeReader.readLine("DFFED");


        } finally {
            try {
                fakeReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                for (Throwable throwable : e.getSuppressed()) System.out.println(throwable.getMessage());
            }
        }

    }
}




