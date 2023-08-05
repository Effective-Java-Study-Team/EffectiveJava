import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DoubleComparisonExample {

    public static void main(String[] args) {
        List<Double> doubleList = new ArrayList<>();
        doubleList.add(1.1321231324213);
        doubleList.add(1.1233333333333312);
        doubleList.add(1.1233333333333322);

        // 부동 소수점 계산 방식으로 인해 값의 손실을 불러와 결과가 제대로 나오지 않는다.
//        Collections.sort(doubleList, new DoubleComparator());

        // Double.compare 이나 Comparator.comparingDouble 메서드를 사용하면 정확한 비교를 할 수 있다.
        Collections.sort(doubleList, new RefactorComparator());

        Comparator<Object> hashCodeOrder = (c1, c2) -> Integer.compare(c1.hashCode(), c2.hashCode());

        for (Double num : doubleList) {
            System.out.println(num);
        }
    }
}

class DoubleComparator implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        return (int) (o1 - o2); // 부동 소수점 연산으로 인한 문제
    }
}

class RefactorComparator implements Comparator<Double> {
    @Override
    public int compare(Double o1, Double o2) {
        return Double.compare(o1, o2);
    }
}