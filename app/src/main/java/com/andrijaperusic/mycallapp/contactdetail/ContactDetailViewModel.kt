package com.andrijaperusic.mycallapp.contactdetail

import androidx.lifecycle.*
import com.andrijaperusic.mycallapp.data.ContactDao
import com.andrijaperusic.mycallapp.data.models.Contact

class ContactDetailViewModel(
    dataSource: ContactDao
): ViewModel()  {

    private val _contactLookupKey = MutableLiveData<String>()

    private val _contact = Transformations.switchMap(_contactLookupKey) {
        dataSource.observeContactDetail(it)
    }
    val contact: LiveData<Contact>
        get() = _contact

    private val _navigateToContactList = MutableLiveData<Boolean>(false)
    val navigateToContactList: LiveData<Boolean>
        get() = _navigateToContactList

    fun navigateToContactList() {
        _navigateToContactList.value = true
    }

    fun doneNavigating() {
        _navigateToContactList.value = false
    }

    fun setContactLookupKey(lookupKey: String) {
        if (lookupKey == _contactLookupKey.value) {
            return
        }
        _contactLookupKey.value = lookupKey
    }

    class ContactDetailViewModelFactory(
        private val dataSource: ContactDao
    ) : ViewModelProvider.Factory {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            (ContactDetailViewModel(dataSource) as T)
    }
}