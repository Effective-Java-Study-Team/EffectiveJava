package templateMethod.abstract_class;

public abstract class AbstractStep {

    /*
    *   step1, 2, 3는 공통적으로 사용하는 메서드지만
    *   상속받은 하위 클래스마다 중복적으로 재정의 해야한다.
    * */

    void setup() {
        step1();
        step2();
        step3();
        step4();
        step5();
    }

    abstract void step1();
    abstract void step2();
    abstract void step3();
    abstract void step4();
    abstract void step5();
}
