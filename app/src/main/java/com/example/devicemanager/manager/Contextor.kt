package com.example.devicemanager.manager

import android.content.Context

/**
 * Created by nuuneoi on 12/6/14 AD.
 */
class Contextor private constructor() {
    var context: Context? = null
        private set

    fun init(context: Context) {
        this.context = context
    }

    companion object {

        private lateinit var instance: Contextor

        fun getInstance(): Contextor {
            if (instance == null)
                instance = Contextor()
            return instance
        }
    }

}
