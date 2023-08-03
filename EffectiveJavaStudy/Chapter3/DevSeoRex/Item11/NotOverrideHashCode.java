import java.util.HashMap;

public class NotOverrideHashCode {

    public static void main(String[] args) {
        Student student1 = new Student(100, 100, 100);
        Student student2 = new Student(100, 100, 100);

        System.out.println("student1.equals(student2) ?  = " + (student1.equals(student2)));
        System.out.println("student1.hashCode() == student2.hashCode() ? " + (student1.hashCode() == student2.hashCode()));

        // 해시코드가 다르다.
        System.out.println(student1.hashCode()); // 804564176
        System.out.println(student2.hashCode()); // 1421795058

        HashMap<Student, String> map = new HashMap<>();
        map.put(student1, "REX");

        // 논리적 동치지만 다른 인스턴스이기 때문에 null 반환
        System.out.println(map.get(student2));
    }

}
