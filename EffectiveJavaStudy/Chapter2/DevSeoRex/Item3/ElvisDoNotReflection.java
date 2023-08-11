package ch2.Item3;

import java.util.Objects;

public class ElvisDoNotReflection {

    private static final ElvisDoNotReflection INSTANCE = new ElvisDoNotReflection();

    // 객체를 인스턴스화 하지 못하도록 방지 -> Reflection 방어
    private ElvisDoNotReflection() {
        // INSTANCE가 한번 생성된 이력이 있으면 객체를 생성하지 못하도록 막아서 싱글턴을 보장
        if (!Objects.isNull(INSTANCE)) {
            throw new IllegalArgumentException("객체를 직접 생성할 수 없는 클래스 입니다.");
        }
    }

    public static ElvisDoNotReflection getInstance() {return INSTANCE;}

    public void leaveTheBuilding() {}
}


