package com.andrijaperusic.mycallapp

import android.app.Application
import com.andrijaperusic.mycallapp.data.CallsDao
import com.andrijaperusic.mycallapp.data.ContactDao
import timber.log.Timber

class MyCallApplication: Application() {

    val contactDataSource: ContactDao
        get() = ServiceLocator.provideContactsDataSource(this)

    val callsDataSource: CallsDao
        get() = ServiceLocator.provideCallsDataSource(this)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}