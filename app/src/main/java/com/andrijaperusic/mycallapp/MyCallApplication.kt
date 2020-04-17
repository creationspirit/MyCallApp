package com.andrijaperusic.mycallapp

import android.app.Application
import timber.log.Timber

class MyCallApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}