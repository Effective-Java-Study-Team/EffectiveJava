package CH2.Item8;

public class FinalizerAttackTest {
    public static void main(String[] args) {
        try {
            new VulnerableClass();
        } catch (Exception e) {
            System.out.println("Exception caught: " + e);
        }
        System.gc();
        System.runFinalization();
        VulnerableClass vc = VulnerableClass.getReference();
        if(vc != null) {
            vc.vulnerableMethod();
        } else {
            System.out.println("VulnerableClass instance is GCed");
        }
    }
}

class VulnerableClass {
    private static VulnerableClass vulnerableObjRef;

    public VulnerableClass() throws Exception {
        System.out.println("VulnerableClass Constructor called");
        throw new Exception("Exception in VulnerableClass Constructor");
    }

    public void vulnerableMethod() {
        System.out.println();
        System.out.println("Vulnerable Method Called");
    }

    protected void finalize() throws Throwable {
        System.out.println("VulnerableClass Finalize Method Called");
        vulnerableObjRef = this;
    }

    public static VulnerableClass getReference() {
        return vulnerableObjRef;
    }
}