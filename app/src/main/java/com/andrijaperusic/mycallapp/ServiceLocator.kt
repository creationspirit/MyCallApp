package com.andrijaperusic.mycallapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.andrijaperusic.mycallapp.data.CallsDao
import com.andrijaperusic.mycallapp.data.CallsLocalDataSource
import com.andrijaperusic.mycallapp.data.ContactDao
import com.andrijaperusic.mycallapp.data.ContactsLocalDataSource

object ServiceLocator {

    private val lock = Any()

    @Volatile
    var contactsLocalDataSource: ContactDao? = null
        @VisibleForTesting set

    @Volatile
    var callsLocalDataSource: CallsDao? = null
        @VisibleForTesting set

    fun provideContactsDataSource(context: Context): ContactDao {
        synchronized(this) {
            return contactsLocalDataSource ?: createContactsDataSource(context)
        }

    }

    private fun createContactsDataSource(context: Context): ContactDao {
        val newDataSource = ContactsLocalDataSource(context)
        contactsLocalDataSource = newDataSource
        return newDataSource
    }

    fun provideCallsDataSource(context: Context): CallsDao {
        synchronized(this) {
            return callsLocalDataSource ?: createCallsDataSource(context)
        }

    }

    private fun createCallsDataSource(context: Context): CallsDao {
        val newDataSource = CallsLocalDataSource(context)
        callsLocalDataSource = newDataSource
        return newDataSource
    }

    @VisibleForTesting
    fun resetDataSources() {
        synchronized(lock) {
            contactsLocalDataSource = null
            callsLocalDataSource = null
        }
    }
}