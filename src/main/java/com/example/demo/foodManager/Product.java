package com.example.demo.foodManager;

public class Product {

    private int id;
    private String name;
    private String category;
    private double currentAmountInGram;
    private double necessaryAmountInGram;
    private String status;

    public Product() {
    }

    public Product(int id, String name, String category, double currentAmountInGram, double necessaryAmountInGram) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.currentAmountInGram = currentAmountInGram;
        this.necessaryAmountInGram = necessaryAmountInGram;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCurrentAmountInGram() {
        return currentAmountInGram;
    }

    public void setCurrentAmountInGram(double currentAmountInGram) {
        this.currentAmountInGram = currentAmountInGram;
    }

    public double getNecessaryAmountInGram() {
        return necessaryAmountInGram;
    }

    public void setNecessaryAmountInGram(double necessaryAmountInGram) {
        this.necessaryAmountInGram = necessaryAmountInGram;
    }

    public String getStatus() {
        if(currentAmountInGram == 0){
            return ("MISSING");
        } else if(currentAmountInGram < necessaryAmountInGram){
            return ("LOW");
        } else{
            return ("OK");
        }
    }

}