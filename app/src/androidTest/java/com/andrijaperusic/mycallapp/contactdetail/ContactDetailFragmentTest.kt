package com.andrijaperusic.mycallapp.contactdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.andrijaperusic.mycallapp.R
import com.andrijaperusic.mycallapp.ServiceLocator
import com.andrijaperusic.mycallapp.data.FakeAndroidContactsLocalDataSource
import com.andrijaperusic.mycallapp.data.models.Contact
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.common.truth.Truth

@RunWith(AndroidJUnit4::class)
@MediumTest
class ContactDetailFragmentTest {

    private lateinit var contactsDataSource: FakeAndroidContactsLocalDataSource

    @Before
    fun initDataSource() {
        contactsDataSource = FakeAndroidContactsLocalDataSource()
        ServiceLocator.contactsLocalDataSource = contactsDataSource
    }

    @After
    fun cleanupDataSources() {
        ServiceLocator.resetDataSources()
    }

    @Test
    fun activeContactDetails_DisplayedInUI() {
        val activeContact = Contact("activeContact", "Active Contact", null)
        contactsDataSource.saveContact(activeContact)

        val bundle = ContactDetailFragmentArgs(activeContact.lookupKey).toBundle()
        launchFragmentInContainer<ContactDetailFragment>(bundle, R.style.AppTheme)

        onView(withId(R.id.contact_name)).check(matches(isDisplayed()))
        onView(withId(R.id.contact_name)).check(matches(withText("Active Contact")))
    }

    @Test
    fun testNavigationToFromDetailScreen() {
        val activeContact = Contact("activeContact", "Active Contact", null)
        contactsDataSource.saveContact(activeContact)

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)

        val bundle = ContactDetailFragmentArgs(activeContact.lookupKey).toBundle()
        val detailScenario = launchFragmentInContainer<ContactDetailFragment>(bundle, R.style.AppTheme)

        // Set the NavController property on the fragment
        detailScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.back_arrow)).perform(click())
        Truth.assertThat(navController.currentDestination?.id).isEqualTo(R.id.tabsContainerFragment)
    }

}