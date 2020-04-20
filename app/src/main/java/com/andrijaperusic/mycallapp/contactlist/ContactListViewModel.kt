package com.andrijaperusic.mycallapp.contactlist

import androidx.lifecycle.*
import com.andrijaperusic.mycallapp.data.ContactDao
import com.andrijaperusic.mycallapp.data.models.Contact

class ContactListViewModel(
    dataSource: ContactDao
): ViewModel() {

    val contacts: LiveData<List<Contact>> = dataSource.observeContacts()

    val showProgressBar: LiveData<Boolean> = Transformations.map(contacts) {
        it == null
    }

    private val _navigateToContactDetail = MutableLiveData<String>()
    val navigateToContactDetail: LiveData<String>
        get() = _navigateToContactDetail

    fun onContactClicked(lookupKey: String) {
        _navigateToContactDetail.value = lookupKey
    }

    fun doneNavigating() {
        _navigateToContactDetail.value = null
    }

    class ContactListViewModelFactory(
        private val dataSource: ContactDao
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            (ContactListViewModel(dataSource) as T)
    }
}