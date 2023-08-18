package CoRaveler.Item24;

import java.util.ArrayList;

public class UseLinkedListReversal {
    public static void main(String[] args) {
        ArrayList<LinkedListReversal.Node> nodes = new ArrayList<>();
        LinkedListReversal list = new LinkedListReversal();

        // 1~5 node 생성
        for (int i = 1; i <= 5; i++) {
            nodes.add(new LinkedListReversal.Node(i, null));
        }

        // 1~4 의 next 설정
        for (int i = 0; i <= 3; i++) {
            nodes.get(i).next = nodes.get(i + 1);
        }

        list.head = nodes.get(0);

        list.reverse();

        System.out.println("list.head = " + list.head);
    }
}
