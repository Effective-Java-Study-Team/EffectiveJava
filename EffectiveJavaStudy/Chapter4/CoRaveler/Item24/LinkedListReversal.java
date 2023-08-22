package CoRaveler.Item24;

// 기존의 LinkedList 를 역순으로 만드는 클래스
public class LinkedListReversal {
    static class Node {
        int data;
        Node next;

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        public Node(int data) {
            this.data = data;
            next = null;
        }

        @Override
        public String toString() {
            return "data : " + data;
        }
    }

    // LinkedList 의 시작점
    Node head;

    public void reverse() {
        head = reverseRecursive(head);
    }

    private Node reverseRecursive(Node current) {
        // 지역 클래스 정의
        class ReverseHelper {
            Node reverse(Node node) {
                System.out.println("node = " + node);
                if (node == null || node.next == null)
                    return node;

                Node rest = reverse(node.next);
                System.out.println("rest = " + rest);
                node.next.next = node;
                node.next = null;
                System.out.println();

                return rest;
            }
        }

        ReverseHelper helper = new ReverseHelper();
        return helper.reverse(current);
    }
}
