package com.example.appprogrammingproject;

public class Restaurant {

    private String name;
    private String rating;
    private String address;


    Restaurant() {

    }

    Restaurant(String name, String address, String rating) {
        this.name = name;
        this.address = address;
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getRating() {
        return rating;
    }

}