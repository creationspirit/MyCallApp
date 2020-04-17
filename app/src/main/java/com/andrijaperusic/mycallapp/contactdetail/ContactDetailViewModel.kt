package com.andrijaperusic.mycallapp.contactdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.andrijaperusic.mycallapp.data.ContactRepository
import com.andrijaperusic.mycallapp.data.models.Contact

class ContactDetailViewModel(
    lookupKey: String,
    application: Application
): AndroidViewModel(application)  {


    private val repository = ContactRepository.getInstance(application)

    val contact = MediatorLiveData<Contact>()

    private val _navigateToContactList = MutableLiveData<Boolean>()
    val navigateToContactList: LiveData<Boolean>
        get() = _navigateToContactList

    init {
        _navigateToContactList.value = false
        contact.addSource(repository.getContactWithPhoneNumbers(lookupKey), contact::setValue)
    }

    fun navigateToContactList() {
        _navigateToContactList.value = true
    }

    fun doneNavigating() {
        _navigateToContactList.value = false
    }
}