package com.example.devicemanager.manager

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.example.devicemanager.room.AppDatabase
import com.example.devicemanager.room.ItemDao
import com.example.devicemanager.room.ItemEntity
import java.util.*
import java.util.concurrent.ExecutionException

class LoadData(context: Context) {
    private val itemDao: ItemDao
    private val db: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()

    val item: List<ItemEntity>
        get() {
            var itemEntities: List<ItemEntity> = ArrayList()
            try {
                itemEntities = GetItemAsyncTask(itemDao).execute().get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return itemEntities
        }

    val orderedItem: List<ItemEntity>
        get() {
            var itemEntities: List<ItemEntity> = ArrayList()
            try {
                itemEntities = GetOrderedItemAsyncTask(itemDao).execute().get()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return itemEntities
        }

    init {
        itemDao = db.itemDao()
    }

    fun insert(itemEntity: ItemEntity): Int? {
        var status = 0
        try {
            status = InsertAsyncTask(itemDao).execute(itemEntity).get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return status
    }

    fun updateLastUpdate(lastUpdate: String, id: Int) {
        UpdateLastUpdate(itemDao, lastUpdate, id).execute()
    }

    fun updateItem(lastUpdate: String, ownedName: String, ownerId: String, brand: String, serial_no: String, item_detail: String,
                   model: String, warrantyDate: String, purchasedPrice: String, purchased_date: String, price: String, note: String,
                   forwardDepreciation: String, depreciationRate: String, depreciationYear: String, accumulatedDepreciation: String,
                   forwardBudget: String, id: Int) {
        UpdateItem(itemDao, lastUpdate, ownedName, ownerId, brand, serial_no, item_detail, model, warrantyDate, purchasedPrice,
                purchased_date, price, note, forwardDepreciation, depreciationRate, depreciationYear, accumulatedDepreciation,
                forwardBudget, id).execute()
    }

    fun deleteTable(): Int? {
        var status: Int? = 0
        try {
            status = DeleteAsyncTask(itemDao).execute().get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return status
    }

    fun selectData(id: String): List<ItemEntity>? {
        var itemEntities: List<ItemEntity>? = null
        try {
            itemEntities = SelectAsyncTask(itemDao, id).execute().get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return itemEntities
    }

    fun selectProductByType(type: String, order: String): List<ItemEntity> {
        var itemEntities: List<ItemEntity> = ArrayList()
        try {
            itemEntities = getAllProductByType(itemDao, type, order).execute().get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return itemEntities
    }

    private class InsertAsyncTask internal constructor(private val itemDao: ItemDao) : AsyncTask<ItemEntity, Void, Int>() {

        override fun doInBackground(vararg itemEntities: ItemEntity): Int? {
            itemDao.insertAll(itemEntities[0])
            return 1
        }

    }

    private class GetItemAsyncTask internal constructor(private val itemDao: ItemDao) : AsyncTask<ItemEntity, Void, List<ItemEntity>>() {

        override fun doInBackground(vararg itemEntities: ItemEntity): List<ItemEntity> {
            return itemDao.all
        }
    }

    private class GetOrderedItemAsyncTask internal constructor(private val itemDao: ItemDao) : AsyncTask<ItemEntity, Void, List<ItemEntity>>() {

        override fun doInBackground(vararg itemEntities: ItemEntity): List<ItemEntity> {
            return itemDao.allOrderByDate
        }
    }

    private class DeleteAsyncTask internal constructor(private val itemDao: ItemDao) : AsyncTask<ItemEntity, Void, Int>() {

        override fun doInBackground(vararg itemEntities: ItemEntity): Int? {
            itemDao.delete()
            return 1
        }

    }

    private class SelectAsyncTask internal constructor(private val itemDao: ItemDao, private val id: String) : AsyncTask<ItemEntity, Void, List<ItemEntity>>() {

        override fun doInBackground(vararg itemEntities: ItemEntity): List<ItemEntity> {
            return itemDao.getProduct(id)
        }
    }

    private class getAllProductByType internal constructor(private val itemDao: ItemDao, private val type: String, private val order: String) : AsyncTask<ItemEntity, Void, List<ItemEntity>>() {

        override fun doInBackground(vararg itemEntities: ItemEntity): List<ItemEntity>? {
            return if (order.matches("DateAsc".toRegex())) {
                itemDao.getAllProductByTypeOrderDateAsc(type)
            } else if (order.matches("DateDesc".toRegex())) {
                itemDao.getAllProductByTypeOrderDateDesc(type)
            } else if (order.matches("BrandAsc".toRegex())) {
                itemDao.getAllProductByTypeOrderBrandAsc(type)
            } else if (order.matches("BrandDesc".toRegex())) {
                itemDao.getAllProductByTypeOrderBrandDesc(type)
            } else {
                null
            }
        }
    }

    private inner class UpdateLastUpdate internal constructor(private val itemDao: ItemDao, internal var lastUpdate: String, internal var id: Int) : AsyncTask<String, Int, Int>() {


        override fun doInBackground(vararg strings: String): Int? {
            itemDao.updateLastUpdate(lastUpdate, id)
            return null
        }
    }

    private inner class UpdateItem(internal var itemDao: ItemDao, internal var lastUpdate: String, internal var ownedName: String, internal var ownerId: String, internal var brand: String, internal var serial_no: String,
                                   internal var item_detail: String, internal var model: String, internal var warrantyDate: String, internal var purchasedPrice: String, internal var purchased_date: String,
                                   internal var price: String, internal var note: String, internal var forwardDepreciation: String, internal var depreciationRate: String, internal var depreciationYear: String,
                                   internal var accumulatedDepreciation: String, internal var forwardBudget: String, internal var id: Int) : AsyncTask<String, Int, Int>() {

        override fun doInBackground(vararg strings: String): Int? {
            itemDao.updateDataFromAdd(lastUpdate, ownedName, ownerId, brand, serial_no, item_detail, model, warrantyDate, purchasedPrice, purchased_date,
                    price, note, forwardDepreciation, depreciationRate, depreciationYear, accumulatedDepreciation, forwardBudget, id)
            return null
        }
    }
}
