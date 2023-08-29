import java.util.List;

public class VarargsMain {

    public static void main(String[] args) {
//        String[] attributes = VarargsTest.pickTwo("좋은", "빠른", "저렴한");
        List<String> attributes2 = VarargsTest.pickTwo2("좋은", "빠른", "저렴한");

        List<String> list1 = List.of("좋은", "빠른", "저렴한");
        List<String> list2 = List.of("좋은", "빠른", "저렴한");
        List<String> list3 = List.of("좋은", "빠른", "저렴한");

        List<String> unionList = VarargsTest.flatten(list1, list2, list3);
        List<String> unionList2 = VarargsTest.flatten(List.of(list1, list2, list3));

        System.out.println("unionList = " + unionList);
        System.out.println("unionList2 = " + unionList2);
    }
}
