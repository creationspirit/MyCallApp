package com.andrijaperusic.mycallapp.contactdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andrijaperusic.mycallapp.data.FakeContactsLocalDataSource
import com.andrijaperusic.mycallapp.data.models.Contact
import com.andrijaperusic.mycallapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class ContactDetailViewModelTest {

    private lateinit var contactsDataSource: FakeContactsLocalDataSource

    private lateinit var contactDetailViewModel: ContactDetailViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        contactsDataSource = FakeContactsLocalDataSource()
        val contact1 = Contact("contact1", "Contact1", null)
        contactsDataSource.addContacts(contact1)

        contactDetailViewModel = ContactDetailViewModel(contactsDataSource)
    }

    @Test
    fun setContactLookupKey_setsActiveContact() {
        contactDetailViewModel.setContactLookupKey("contact1")
        val contact = contactDetailViewModel.contact.getOrAwaitValue()
        assertThat(contact.lookupKey).isEqualTo("contact1")
    }

    @Test
    fun navigateToContactList_setsNavigateToContactListEvent() {
        contactDetailViewModel.navigateToContactList()

        val value = contactDetailViewModel.navigateToContactList.getOrAwaitValue()
        assertThat(value).isTrue()

        contactDetailViewModel.doneNavigating()

        val doneValue = contactDetailViewModel.navigateToContactList.getOrAwaitValue()
        assertThat(doneValue).isFalse()
    }
}
