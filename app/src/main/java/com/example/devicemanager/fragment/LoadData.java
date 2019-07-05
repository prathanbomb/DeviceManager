package com.example.devicemanager.fragment;

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

import java.util.List;

public class LoadData {
    private ItemDao itemDao;

    public LoadData(Context context){
        AppDatabase db = AppDatabase.getInstance(context);
        itemDao = db.itemDao();
    }

    public void insert(ItemEntity itemEntity){
        new InsertAsyncTask(itemDao).execute(itemEntity);
    }

    /*public List<ItemEntity> getItem(){
        return new
    }*/

    private static class InsertAsyncTask extends AsyncTask<ItemEntity, Void, Integer>{

        private ItemDao itemDao;

        InsertAsyncTask(ItemDao itemDao){
            this.itemDao = itemDao;
        }

        @Override
        protected Integer doInBackground(ItemEntity... itemEntities) {
            itemDao.insertAll(itemEntities[0]);
            return 1;
        }
    }

    private static class GetItemAsyncTask extends AsyncTask<ItemEntity, Void, Integer>{

        private ItemDao itemDao;

        @Override
        protected Integer doInBackground(ItemEntity... itemEntities) {
            itemDao.insertAll(itemEntities[0]);
            return 1;
        }
    }
}
