package CoRaveler.Item12;

import java.util.HashMap;
import java.util.Map;

public class ParseToString {
    public static void main(String[] args) {
        Test t = new Test(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        String toStringRes = t.toString();
        System.out.println(toStringRes);

        String[] splitRes = toStringRes.replace("Test{", "").replace("}", "").split(", ");
        Map<String, Integer> map = new HashMap<>();

        for (String s : splitRes) {
            String[] keyValue = s.split("=");
            map.put(keyValue[0], Integer.parseInt(keyValue[1]));
        }

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}


class Test {
    private int a, b, c, d, e, f, g, h, i, j, k;

    public Test(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, int k) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    @Override
    public String toString() {
        return "Test{" + "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + ", g=" + g + ", h=" + h + ", i=" + i + ", j=" + j + ", k=" + k + '}';
    }
}
