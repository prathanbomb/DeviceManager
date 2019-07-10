package com.example.devicemanager.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM ItemEntity")
    List<ItemEntity> getAll();

    @Query("SELECT * FROM ItemEntity ORDER BY purchased_date ASC, item_detail DESC")
    List<ItemEntity> getAllOrderByDate();

    @Query("SELECT * FROM ItemEntity ORDER BY :column ASC")
    List<ItemEntity> getItemAsc(String column);

    @Query("SELECT * FROM ItemEntity ORDER BY :column DESC")
    List<ItemEntity> getItemDesc(String column);

    @Query("SELECT * FROM ItemEntity WHERE item_id = :id")
    List<ItemEntity> getProduct(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ItemEntity item);

    @Query("DELETE FROM ItemEntity")
    void delete();
}
