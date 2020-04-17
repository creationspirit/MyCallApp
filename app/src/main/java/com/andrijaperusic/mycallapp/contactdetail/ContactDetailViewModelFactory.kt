package com.andrijaperusic.mycallapp.contactdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactDetailViewModelFactory(
    private val lookupKey: String,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactDetailViewModel::class.java)) {
            return ContactDetailViewModel(lookupKey, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}