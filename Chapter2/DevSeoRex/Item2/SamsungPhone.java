package ch2.item2;

import java.util.Objects;

public class SamsungPhone extends SmartPhone {
    public enum Kind { BASIC, PREMIUM }
    private final Kind kind;


    public static class Builder extends SmartPhone.Builder<Builder> {
        private final Kind kind;

        public Builder(Kind kind) {
            this.kind = Objects.requireNonNull(kind);
        }


        @Override
        public SamsungPhone build() {
            return new SamsungPhone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private SamsungPhone(Builder builder) {
        super(builder);
        kind = builder.kind;
    }


}
