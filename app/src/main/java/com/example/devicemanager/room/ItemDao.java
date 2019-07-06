package com.example.devicemanager.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM ItemEntity")
    List<ItemEntity> getAll();

    @Query("SELECT * FROM ItemEntity WHERE item_id = :id")
    ItemEntity getProduct(String id);

    @Insert
    void insertAll(ItemEntity item);

    @Query("DELETE FROM ItemEntity")
    void delete();
}
