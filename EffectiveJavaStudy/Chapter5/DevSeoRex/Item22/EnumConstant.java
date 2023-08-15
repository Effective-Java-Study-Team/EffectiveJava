public enum EnumConstant {

    AVOGADROS_NUMBER(6.022_140_857e23),
    BOLTZMANN_CONSTANT(1.380_648_52-23),
    ELECTRON_MASS(9.109_383_56e-31);

    private final double number;

    EnumConstant(double number) {
        this.number = number;
    }
}
