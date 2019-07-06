package com.example.devicemanager.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.devicemanager.room.AppDatabase;
import com.example.devicemanager.room.ItemDao;
import com.example.devicemanager.room.ItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoadData {
    private ItemDao itemDao;
    private AppDatabase db;

    public LoadData(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "app_database").build();
        itemDao = db.itemDao();
    }

    public void insert(ItemEntity itemEntity) {
        new InsertAsyncTask(itemDao).execute(itemEntity);
    }

    public void deleteTable() {
        new DeleteAsyncTask(itemDao).execute();
    }

    public ItemEntity selectData(String id) {
        ItemEntity itemEntities = null;
        try {
            itemEntities = new SelectAsyncTask(itemDao,id).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return itemEntities;
    }

    public List<ItemEntity> getItem() {
        List<ItemEntity> itemEntities = new ArrayList<>();
        try {
            itemEntities = new GetItemAsyncTask(itemDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return itemEntities;
    }

    private static class InsertAsyncTask extends AsyncTask<ItemEntity, Void, Integer> {

        private ItemDao itemDao;

        InsertAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Integer doInBackground(ItemEntity... itemEntities) {
            itemDao.insertAll(itemEntities[0]);
            return 1;
        }
    }

    private static class GetItemAsyncTask extends AsyncTask<ItemEntity, Void, List<ItemEntity>> {

        private ItemDao itemDao;

        GetItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected List<ItemEntity> doInBackground(ItemEntity... itemEntities) {
            return itemDao.getAll();
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<ItemEntity, Void, Void> {
        private ItemDao itemDao;

        public DeleteAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(ItemEntity... itemEntities) {
            itemDao.delete();
            return null;
        }
    }

    private class SelectAsyncTask extends AsyncTask<ItemEntity, Void, ItemEntity>{
        private ItemDao itemDao;
        String id;
        public SelectAsyncTask(ItemDao itemDao , String id) {
            this.itemDao = itemDao;
            this.id = id;
        }

        @Override
        protected ItemEntity doInBackground(ItemEntity... itemEntities) {
            return itemDao.getProduct(id);
        }
    }
}
