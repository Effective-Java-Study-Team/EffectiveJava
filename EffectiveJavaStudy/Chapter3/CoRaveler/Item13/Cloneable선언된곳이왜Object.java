package CoRaveler.Item13;

public class Cloneable선언된곳이왜Object {
    public static void main(String[] args) {
        Parent p = new Parent(42);
//        Parent copy = p.clone();  // protected 라서 호출이 안된다!
    }

    static class Parent implements Cloneable {
        private int parentField;

        public Parent(int parentField) {
            this.parentField = parentField;
        }

        public int getParentField() {
            return parentField;
        }
    }

}
