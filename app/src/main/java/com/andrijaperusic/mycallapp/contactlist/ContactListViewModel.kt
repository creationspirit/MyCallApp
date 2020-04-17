package com.andrijaperusic.mycallapp.contactlist

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.andrijaperusic.mycallapp.data.ContactRepository
import com.andrijaperusic.mycallapp.data.models.Contact
import timber.log.Timber

class ContactListViewModel(
    application: Application
): AndroidViewModel(application) {

    private val repository = ContactRepository.getInstance(application)

    val contacts: LiveData<List<Contact>> = repository.getContacts()

    private val _navigateToContactDetail = MutableLiveData<String>()
    val navigateToContactDetail
        get() = _navigateToContactDetail

    val showProgressBar: LiveData<Boolean> = Transformations.map(contacts) {
        it == null || it.isEmpty()
    }

    fun onContactClicked(lookupKey: String) {
        _navigateToContactDetail.value = lookupKey
    }

    fun doneNavigating() {
        _navigateToContactDetail.value = null
    }
}