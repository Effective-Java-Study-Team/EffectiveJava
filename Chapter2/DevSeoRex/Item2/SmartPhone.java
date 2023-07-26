package ch2.item2;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public abstract class SmartPhone {
    public enum Option { BOOSTER, WIFI, BLUETOOTH, DMB, EARPHONE }
    final Set<Option> options;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Option> options = EnumSet.noneOf(Option.class);
        public T addOption(Option option) {
            options.add(Objects.requireNonNull(option));
            return self();
        }

        abstract SmartPhone build();


        protected abstract T self();
    }

    public SmartPhone(Builder<?> builder) {
        // 방어적 복사
        options = builder.options.clone();
    }


}

