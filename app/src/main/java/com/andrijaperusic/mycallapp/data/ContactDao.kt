package com.andrijaperusic.mycallapp.data

import androidx.lifecycle.LiveData
import com.andrijaperusic.mycallapp.data.models.Contact

interface ContactDao {
    fun observeContacts(): LiveData<List<Contact>>

    fun observeContactDetail(lookupKey: String): LiveData<Contact>
}