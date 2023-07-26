package ch2.item2;

import java.time.LocalDateTime;

public class Computer {

    private int price;
    private LocalDateTime regDate;
    private int repairCount;
    private String brand;
    private String gpu;
    private String hdd;
    private String ssd;
    private String kindOf;

    // 선택적 매개변수 목록
    private String color;
    private int weight;
    private int height;

    public Computer(int price, LocalDateTime regDate, int repairCount, String brand, String gpu,
                    String hdd, String ssd, String kindOf, String color, int weight, int height) {
        this(price, regDate, repairCount, brand, gpu, hdd, ssd, kindOf);
        this.color = color;
        this.weight = weight;
        this.height = height;
    }

    public Computer(int price, LocalDateTime regDate, int repairCount, String brand, String gpu,
                    String hdd, String ssd, String kindOf) {
        this.price = price;
        this.regDate = regDate;
        this.repairCount = repairCount;
        this.brand = brand;
        this.gpu = gpu;
        this.hdd = hdd;
        this.ssd = ssd;
        this.kindOf = kindOf;
    }

    public Computer(int price, LocalDateTime regDate, int repairCount, String brand, String gpu,
                    String hdd, String ssd, String kindOf, String color){
        this(price, regDate, repairCount, brand, gpu, hdd, ssd, kindOf);
        this.color = color;
    }

    public Computer(int price, LocalDateTime regDate, int repairCount, String brand, String gpu,
                    String hdd, String ssd, String kindOf, int height, int weight){
        this(price, regDate, repairCount, brand, gpu, hdd, ssd, kindOf);
        this.height = height;
        this.weight = weight;
    }

    public Computer() {}

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setRepairCount(int repairCount) {
        this.repairCount = repairCount;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public void setHdd(String hdd) {
        this.hdd = hdd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public void setKindOf(String kindOf) {
        this.kindOf = kindOf;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

