package ArrayProblem;

public class SuperClass {

    int value;

    SuperClass(int value) {
        this(validate(value));
        this.value = value;
    }

    private SuperClass(Void validate) {
        super();
    }

    static Void validate(int value) {
        if (value < 0) {
            throw new IllegalStateException("Negative Super Class Value");
        }

        return null;
    }
}





