package com.example.devicemanager.room;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM ItemEntity")
    List<ItemEntity> getAll();

    @Query("SELECT * FROM ItemEntity WHERE item_id = :id")
    List<ItemEntity> getProduct(int id);

    @Insert
    void insertAll(ItemEntity item);

    @Query("DELETE FROM ItemEntity")
    void delete();
}
