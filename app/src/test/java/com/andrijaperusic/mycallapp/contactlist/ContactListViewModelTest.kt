package com.andrijaperusic.mycallapp.contactlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrijaperusic.mycallapp.data.ContactDao
import com.andrijaperusic.mycallapp.data.FakeContactsLocalDataSource
import com.andrijaperusic.mycallapp.data.models.Contact
import com.andrijaperusic.mycallapp.getOrAwaitValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import org.junit.Before

class ContactListViewModelTest {

    private lateinit var contactsDataSource: FakeContactsLocalDataSource

    private lateinit var contactListViewModel: ContactListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        contactsDataSource = FakeContactsLocalDataSource()
        val contact1 = Contact("contact1", "Contact1", null)
        val contact2 = Contact("contact2", "Contact2", null)
        val contact3 = Contact("contact3", "Contact3", null)
        contactsDataSource.addContacts(contact1, contact2, contact3)

        contactListViewModel = ContactListViewModel(contactsDataSource)
    }

    @Test
    fun onContactClicked_setsNavigateToContactDetailEvent() {
        contactListViewModel.onContactClicked("TestLookupKey")

        val value = contactListViewModel.navigateToContactDetail.getOrAwaitValue()
        assertThat(value).isEqualTo("TestLookupKey")

        contactListViewModel.doneNavigating()

        val doneValue = contactListViewModel.navigateToContactDetail.getOrAwaitValue()
        assertThat(doneValue).isNull()
    }
}