package com.example.appprogrammingproject;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName="Restaurants")
public class Restaurant {

    public Restaurant() {

    }

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "name")
    public String restaurantid;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "rating")
    public String rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRestaurantid() {
        return restaurantid;
    }

    public void setRestaurantid(String restaurantid) {
        this.restaurantid = restaurantid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
