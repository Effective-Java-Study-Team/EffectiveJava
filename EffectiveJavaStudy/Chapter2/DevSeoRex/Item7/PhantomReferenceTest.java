package ch2.item7;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {

    public static void main(String[] args) {

        Integer number = 400;

        ReferenceQueue<Integer> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Integer> phantomReference =
                new PhantomReference<>(number, referenceQueue);


        number = null;

        System.gc();

        // GC 동작시 이미 도달할 수 없는 객체의 경우 Enqueue 되지 않기 때문에 false
        System.out.println(phantomReference.isEnqueued());
        System.out.println(phantomReference.get());
    }
}
