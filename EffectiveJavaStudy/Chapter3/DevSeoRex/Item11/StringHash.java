public class StringHash {

    public static void main(String[] args) {

        String word1 = "apple";
        String word2 = "eppla";


        // 곱셈 없이 String의 hashCode를 구현한다면 모든 아나그램의 해시코드가 같아진다.
        System.out.println(hashCode(word1));
        System.out.println(hashCode(word2));

        System.out.println();

        // 곱셈 연산을 통한 HashCode 도출이 충돌을 막는다.
        System.out.println(multiplyHashcode(word1));
        System.out.println(multiplyHashcode(word2));
    }

    static int hashCode(String s) {
        int result = 0;

        for (int i=0; i<s.length(); i++) {
            result += Character.hashCode(s.charAt(i));
        }

        return result;
    }

    static int multiplyHashcode(String s) {
        int result = 0;
        char[] charArray = s.toCharArray();

        result = Character.hashCode(charArray[0]);

        if (charArray.length > 1) {
            for (int i=1; i<charArray.length; i++) {
                result = 31 * result + Character.hashCode(charArray[i]);
            }
        }

        return result;
    }
}
