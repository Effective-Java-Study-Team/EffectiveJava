public class ParentClass implements Cloneable {

    int a;


    public ParentClass(int a) {
        this.a = a;
    }

    @Override
    public ParentClass clone() throws CloneNotSupportedException {
        someLogic();
        return (ParentClass) super.clone();
    }

    // 하위 클래스에서 재정의 할 수 있는 메서드는 부모 클래스의 clone 또는 생성자에서 호출하면 안된다.
    protected void someLogic() {}
}
