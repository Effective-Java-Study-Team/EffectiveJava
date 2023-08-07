package CoRaveler.Item15;

import java.util.Arrays;

class PublicStaticFinal필드수정하기 {
    public static void main(String[] args) {
        System.out.println("(before) PublicStaticFinal필드테스트.VALUES = " + Arrays.toString(PublicStaticFinal필드테스트.VALUES));
        PublicStaticFinal필드테스트.VALUES[0] = 10;
        System.out.println("(before) PublicStaticFinal필드테스트.VALUES = " + Arrays.toString(PublicStaticFinal필드테스트.VALUES));
    }
}
