
public class Student {

    private int kor;
    private int eng;
    private int math;

    public Student(int kor, int eng, int math) {
        this.kor = kor;
        this.eng = eng;
        this.math = math;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof Student)) return false;

        Student student = (Student) obj;

        return student.math == math &&
                student.eng == eng && student.kor == kor;
    }
}
