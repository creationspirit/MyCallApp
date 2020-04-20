package com.andrijaperusic.mycallapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.andrijaperusic.mycallapp.data.models.Contact
import kotlinx.coroutines.runBlocking

class FakeContactsLocalDataSource: ContactDao {

    var contentProviderData: LinkedHashMap<String, Contact> = LinkedHashMap()

    private val observableContacts = MutableLiveData<List<Contact>>()

    private fun refreshContacts() {
        observableContacts.value = contentProviderData.values.toList()
    }

    override fun observeContacts(): LiveData<List<Contact>> {
        runBlocking { refreshContacts() }
        return observableContacts
    }

    override fun observeContactDetail(lookupKey: String): LiveData<Contact> {
        runBlocking { refreshContacts() }
        return Transformations.map(observableContacts) { contacts ->
            contacts.firstOrNull() { it.lookupKey == lookupKey }
        }
    }

    fun addContacts(vararg contacts: Contact) {
        for (contact in contacts) {
            contentProviderData[contact.lookupKey] = contact
        }
        runBlocking { refreshContacts() }
    }
}