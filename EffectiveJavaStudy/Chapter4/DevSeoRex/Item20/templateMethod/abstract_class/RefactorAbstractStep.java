package templateMethod.abstract_class;

public abstract class RefactorAbstractStep {

    /*
    *   하위 클래스에서 공통으로 사용하는 부분은
    *   디폴트 구현을 제공해서, 중복된 코드를 작성하지 않아도 된다.
    *   -> 템플릿 메서드 패턴
    * */

    void setup() {
        step1();
        step2();
        step3();
        step4();
        step5();
    }

    protected void step1() {
        System.out.println("step1");
    }

    protected void step2() {
        System.out.println("step2");
    }

    protected void step3() {
        System.out.println("step3");
    }
    abstract void step4();
    abstract void step5();
}
