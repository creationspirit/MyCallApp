package com.andrijaperusic.mycallapp.contactlist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for ContactListViewModel
 *
 * Provides context to the ContactListViewModel class
 */
class ContactListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
            return ContactListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}