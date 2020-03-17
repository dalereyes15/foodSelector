package com.example.appprogrammingproject;

import androidx.room.RoomDatabase;

public abstract class RestaurantDatabase extends RoomDatabase {

    public abstract RestaurantDAO restaurantDAO();

}
