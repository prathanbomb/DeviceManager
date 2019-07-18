package com.example.devicemanager.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {

    @get:Query("SELECT * FROM ItemEntity")
    val all: List<ItemEntity>

    @get:Query("SELECT * FROM ItemEntity ORDER BY purchased_date ASC, item_detail DESC")
    val allOrderByDate: List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE item_id = :id ")
    fun getProduct(id: String): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY purchased_date ASC")
    fun getAllProductByTypeOrderDateAsc(type: String): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY purchased_date DESC")
    fun getAllProductByTypeOrderDateDesc(type: String): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY brand ASC")
    fun getAllProductByTypeOrderBrandAsc(type: String): List<ItemEntity>

    @Query("SELECT * FROM ItemEntity WHERE item_type = :type ORDER BY brand DESC")
    fun getAllProductByTypeOrderBrandDesc(type: String): List<ItemEntity>

    @Insert
    fun insertAll(item: ItemEntity)

    @Query("UPDATE itementity SET lastUpdated = :lastUpdate WHERE autoId = :id")
    fun updateLastUpdate(lastUpdate: String, id: Int)

    @Query("UPDATE itementity SET " +
            "lastUpdated = :lastUpdate ,owner_name = :ownedName ,placeId = :ownerId,brand = :brand ,serial_no = :serial_no ," +
            "item_detail = :item_detail ,model = :model ,warrantyDate = :warrantyDate ,purchasedPrice = :purchasedPrice ," +
            "purchased_date = :purchased_date ,price = :price ,note = :note ,forwardDepreciation = :forwardDepreciation ," +
            "depreciationRate = :depreciationRate ,depreciationYear = :depreciationYear ," +
            "accumulatedDepreciation = :accumulatedDepreciation ,forwardBudget = :forwardBudget WHERE autoId = :id")
    fun updateDataFromAdd(lastUpdate: String, ownedName: String, ownerId: String, brand: String, serial_no: String, item_detail: String,
                          model: String, warrantyDate: String, purchasedPrice: String, purchased_date: String, price: String, note: String,
                          forwardDepreciation: String, depreciationRate: String, depreciationYear: String, accumulatedDepreciation: String,
                          forwardBudget: String, id: Int)

    @Query("DELETE FROM ItemEntity")
    fun delete()
}
