package com.example.devicemanager.room;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    Long insertTask(ItemEntity itemEntity);

    @Query("SELECT * FROM ItemEntity WHERE autoId =:id")
    List<ItemEntity> getAll(int id);

    @Insert
    void insertAll(ItemEntity item);

    @Delete
    void delete(ItemEntity item);
}
