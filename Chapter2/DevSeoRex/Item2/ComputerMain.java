package ch2.item2;


public class ComputerMain {

    public static void main(String[] args) {
        Computer computer = new Computer();
        computer.setBrand("Intel");
        computer.setColor("Black");
        computer.setGpu("GTX 3050");
        computer.setPrice(400000000);

        ComputerFactory factory1 = new ComputerFactory.FactoryBuilder("컴퓨터공장1", "사장1", 200)
                .moneyLeft(2000)
                .build();

        ComputerFactory factory2 = new ComputerFactory.FactoryBuilder("컴퓨터공장2", "사장2", 3000)
                .clerkCount(300)
                .moneyLeft(-1)
                .build();

    }
}
