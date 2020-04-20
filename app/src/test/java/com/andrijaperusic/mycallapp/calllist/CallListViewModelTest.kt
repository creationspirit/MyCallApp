package com.andrijaperusic.mycallapp.calllist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.andrijaperusic.mycallapp.data.FakeCallsLocalDataSource
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class CallListViewModelTest {
    private lateinit var callListViewModel: CallListViewModel

    private lateinit var callsDataSource: FakeCallsLocalDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        callsDataSource = FakeCallsLocalDataSource()
        callListViewModel = CallListViewModel(callsDataSource)
    }
}