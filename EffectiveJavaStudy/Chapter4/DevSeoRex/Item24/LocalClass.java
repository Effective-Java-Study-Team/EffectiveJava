public class LocalClass {

    void print() {

        class LocalPrinter {
            void println() {
                System.out.println("println!");
            }
        }

        LocalPrinter localPrinter = new LocalPrinter();
        localPrinter.println();
    }
}
