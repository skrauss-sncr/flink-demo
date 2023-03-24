package com.pluralsight.streaming;

public class CarPojo {

    public String brand;
    public String model;
    public String year;
    public Integer price;
    public Integer miles;

    public CarPojo() {

    }

    public CarPojo(String brand, String model, String year, Integer price, Integer miles) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.miles = miles;
    }

    @Override
    public String toString() {
        return brand + ", " + model + ", " + year + ", " + price + ", " + miles;
    }
}
