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

    @Insert
    void insertAll(ItemEntity item);

    @Delete
    void delete(ItemEntity item);
}
