package ch2.item2;

public class IPhone extends SmartPhone {

    public enum CameraCount { ONE, TWO, THREE }
    private final CameraCount cameraCount;


    public static class Builder extends SmartPhone.Builder<Builder> {

        private final CameraCount cameraCount;

        public Builder(CameraCount cameraCount) {
            this.cameraCount = cameraCount;
        }

        @Override
        public IPhone build() {
            return new IPhone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private IPhone(Builder builder) {
        super(builder);
        this.cameraCount = builder.cameraCount;
    }
}
