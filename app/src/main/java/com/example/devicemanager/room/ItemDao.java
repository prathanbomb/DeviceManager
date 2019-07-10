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

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY purchased_date ASC")
    List<ItemEntity> getAllProductByTypeOrderDateAsc(String type);

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY purchased_date DESC")
    List<ItemEntity> getAllProductByTypeOrderDateDesc(String type);

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY brand ASC")
    List<ItemEntity> getAllProductByTypeOrderBrandAsc(String type);

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY brand DESC")
    List<ItemEntity> getAllProductByTypeOrderBrandDesc(String type);

    @Insert
    void insertAll(ItemEntity item);

    @Query("DELETE FROM ItemEntity")
    void delete();
}
