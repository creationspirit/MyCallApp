package com.andrijaperusic.mycallapp

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.andrijaperusic.mycallapp.contactdetail.ContactDetailFragment
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @Test
    fun testNavigationToFromDetailScreen() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.nav_graph)

        val detailScenario = launchFragmentInContainer<ContactDetailFragment>()

        // Set the NavController property on the fragment
        detailScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(ViewMatchers.withId(R.id.back_arrow)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.tabsContainerFragment)
    }
}