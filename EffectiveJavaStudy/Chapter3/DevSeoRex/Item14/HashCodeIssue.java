import java.util.ArrayList;
import java.util.List;

public class HashCodeIssue {

    public static void main(String[] args) {


        // 정수 오버플로우로 인한 비교 문제
        HashCodeCompare instance1 = new HashCodeCompare(Integer.MIN_VALUE);
        HashCodeCompare instance2 = new HashCodeCompare(Integer.MAX_VALUE);


        List<HashCodeCompare> compareArrayList = new ArrayList<>();
        compareArrayList.add(instance1);
        compareArrayList.add(instance2);

        System.out.println("compareArrayList = " + compareArrayList);

        compareArrayList.sort(HashCodeCompare.HASH_COMPARATOR);

        System.out.println("compareArrayList = " + compareArrayList);


        // instance1의 해시코드 값이 더 작은데 instance1이 더 크다고 결과가 나온다
        System.out.println(HashCodeCompare.HASH_COMPARATOR.compare(instance1, instance2));
    }

}
