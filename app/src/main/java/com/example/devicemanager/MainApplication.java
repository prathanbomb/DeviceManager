package com.example.devicemanager;

import android.app.Application;

import com.example.devicemanager.manager.Contextor;


public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
