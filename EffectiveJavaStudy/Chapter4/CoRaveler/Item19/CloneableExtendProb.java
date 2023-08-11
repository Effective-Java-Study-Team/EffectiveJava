package CoRaveler.Item19;

public class CloneableExtendProb {
    public static void main(String[] args) throws CloneNotSupportedException {
        DerivedCloneableClass original = new DerivedCloneableClass("Original Data");
        DerivedCloneableClass cloned = (DerivedCloneableClass) original.clone();

        System.out.println("original = " + original);
        System.out.println("cloned = " + cloned);
    }
}

class DeepStructure {
    String data;

    DeepStructure(String data) {
        this.data = data;
    }

    void modify(String newData) {
        this.data = newData;
    }

    @Override
    public String toString() {
        return data;
    }
}

class CloneableClass implements Cloneable {
    DeepStructure deepStructure;

    CloneableClass(String data) {
        this.deepStructure = new DeepStructure(data);
    }

    @Override
    protected CloneableClass clone() throws CloneNotSupportedException {
        CloneableClass cloned = (CloneableClass) super.clone();
        cloned.setup();
        return cloned;
    }

    // 재정의 가능한 메서드
    void setup() {
        deepStructure = new DeepStructure("default");
    }

    @Override
    public String toString() {
        return deepStructure.toString();
    }
}

class DerivedCloneableClass extends CloneableClass {

    // DeepStructure deepStructure

    DerivedCloneableClass(String data) {
        super(data);
    }

    // setup 오버라이드
    @Override
    void setup() {
        deepStructure.modify("Derived Modified");
    }
}