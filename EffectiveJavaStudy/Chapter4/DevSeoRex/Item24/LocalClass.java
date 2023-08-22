public class LocalClass {

    void print() {

        class LocalPrinter {

            static int age = 20;

            void println() {
                System.out.println("println!");
            }
        }

        LocalPrinter localPrinter = new LocalPrinter();
        localPrinter.println();
    }
}
