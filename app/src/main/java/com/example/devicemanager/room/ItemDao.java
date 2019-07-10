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

    @Query("SELECT * FROM ItemEntity WHERE item_id = :id ")
    List<ItemEntity> getProduct(String id);

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY :order ASC")
    List<ItemEntity> getAllProductByTypeOrderAsc(String type,String order);

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY :order DESC")
    List<ItemEntity> getAllProductByTypeOrderDesc(String type,String order);

    @Insert
    void insertAll(ItemEntity item);

    @Query("DELETE FROM ItemEntity")
    void delete();
}
