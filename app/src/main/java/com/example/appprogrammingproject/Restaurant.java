package com.example.appprogrammingproject;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName="Restaurants")
public class Restaurant {

    public Restaurant() {

    }

    @ColumnInfo(name = "name")
    public int name;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "rating")
    public double rating;

}
