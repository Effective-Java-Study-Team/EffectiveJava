package ch2.item7;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class SoftAndWeakReferenceTest {

    public static void main(String[] args) {

        Integer weak = 300;
        Integer soft = 400;

        WeakReference<Integer> weakReference = new WeakReference<>(weak);
        SoftReference<Integer> softReference = new SoftReference<>(soft);

        weak = null;
        soft = null;

        System.gc();

        System.out.println(weakReference.get());
        System.out.println(softReference.get());
    }
}
