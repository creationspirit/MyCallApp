package com.andrijaperusic.mycallapp.calllist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for CallListViewModel
 *
 * Provides context to the CallListViewModel class
 */
class CallListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallListViewModel::class.java)) {
            return CallListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}