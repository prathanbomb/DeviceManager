package com.example.devicemanager

import android.app.Application

import com.crashlytics.android.Crashlytics
import com.example.devicemanager.manager.Contextor

import io.fabric.sdk.android.Fabric


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Contextor.getInstance().init(applicationContext)
    }

}
