package com.example.ch2.item2.telescoping;

/**
* 점층적 생성자 패턴
**/
public class Company {
      private final String name;
      private final String address;
      private final int businessNumber;
      private final String industry;
      private final int employeeCount;
      private final String phoneNumber;


    public Company(String name, String address, int businessNumber) {
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.industry = "NONE";
        this.employeeCount = 0;
        this.phoneNumber = "NONE";
    }

    public Company(String name, String address, int businessNumber, String industry) {
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.industry = industry;
        this.employeeCount = 0;
        this.phoneNumber = "NONE";
    }

    public Company(String name, String address, int businessNumber, String industry, int employeeCount, int phoneNumber) {
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.industry = industry;
        this.employeeCount = employeeCount;
        this.phoneNumber = "NONE";
    }

}
