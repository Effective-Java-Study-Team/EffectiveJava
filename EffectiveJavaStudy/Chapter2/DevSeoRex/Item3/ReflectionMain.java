package ch2.Item3;

import java.lang.reflect.Constructor;

public class ReflectionMain {

    public static void main(String[] args) throws Exception {
        Elvis elvis = Elvis.INSTANCE;
        Elvis elvis2 = Elvis.INSTANCE;

        // Reflection API를 이용한 객체 생성(private 생성자를 호출(공격) 해보기)

        // Elvis 클래스 정보를 얻어온다.
        Class<Elvis> target = Elvis.class;

        // Elvis 클래스의 기본 생성자를 얻어온다.
        Constructor<Elvis> constructor = target.getDeclaredConstructor();

        // Elvis 클래스의 기본 생성자의 접근이 가능하도록 설정한다.
        constructor.setAccessible(true);

        // Elvis 클래스의 객체 생성
        Elvis reflectionElvis = constructor.newInstance();

        // Elvis 클래스의 싱글턴 인스턴스와 Elvis 클래스의 private 생성자로 새로 생성한 객체는 다르다.
        System.out.println(elvis.equals(reflectionElvis));

        // Elvis 클래스 내부의 싱글턴 인스턴스는 언제 리턴 받아도 같은 객체임을 보장한다.
        System.out.println(elvis.equals(elvis2));
        System.out.println();

        // Reflection API를 이용한 private 생성자를 사용하지 못하도록 방어 로직 작성 후 재시도

        // 싱글턴으로 얻어올때는 에러가 발생하지 않음
        ElvisDoNotReflection elvisDoNotReflect = ElvisDoNotReflection.getInstance();
        ElvisDoNotReflection elvisDoNotReflect2 = ElvisDoNotReflection.getInstance();

        Constructor<ElvisDoNotReflection> elvisConstructor = ElvisDoNotReflection.class.getDeclaredConstructor();
        elvisConstructor.setAccessible(true);
        ElvisDoNotReflection elvisDoNotReflection = elvisConstructor.newInstance();


    }
}
