package com.example.appprogrammingproject;

public class Restaurant {

    private String name;
    private String rating;
    private String address;
    private String restaurantid;

    Restaurant() {

    }

    Restaurant(String name, String address, String rating, String restaurantid) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.restaurantid = restaurantid;
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

    public void setRestaurantid(String restaurantid) {
        this.restaurantid = restaurantid;
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

    public String getRestaurantid() {
        return restaurantid;
    }
}