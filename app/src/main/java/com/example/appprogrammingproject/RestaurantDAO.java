package com.example.appprogrammingproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RestaurantDAO {

    @Query("SELECT * FROM restaurants")
    LiveData<List<Restaurant>> getAll();

    @Insert
    void insert(Restaurant... restaurants);

    @Update
    void update(Restaurant... restaurants);

    @Delete
    void delete(Restaurant... restaurants);

    @Query("DELETE FROM restaurants WHERE rowid = :name")
    void delete(String name);

}
