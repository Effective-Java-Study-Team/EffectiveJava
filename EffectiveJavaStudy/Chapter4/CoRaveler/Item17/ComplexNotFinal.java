package CoRaveler.Item17;

public class ComplexNotFinal {
    private final double re;
    private final double im;

    ComplexNotFinal(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public static ComplexNotFinal valueOf(double re, double im) {
        return new ComplexNotFinal(re, im);
    }

    //...
}

class ComplexNotFinalExtend extends ComplexNotFinal {
    private final double extraValue;

    public ComplexNotFinalExtend(double re, double im, double extraValue) {
        super(re, im);
        this.extraValue = extraValue;
    }

    //...
}
