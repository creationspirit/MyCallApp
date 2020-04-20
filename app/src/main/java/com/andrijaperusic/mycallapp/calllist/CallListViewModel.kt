package com.andrijaperusic.mycallapp.calllist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andrijaperusic.mycallapp.data.CallsDao

class CallListViewModel(
    dataSource: CallsDao
): ViewModel() {

    val calls = dataSource.observeCalls()

    class CallListViewModelFactory(
        private val dataSource: CallsDao
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>) =
            (CallListViewModel(dataSource) as T)
    }
}