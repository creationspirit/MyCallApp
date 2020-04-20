package com.andrijaperusic.mycallapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrijaperusic.mycallapp.data.models.Call
import kotlinx.coroutines.runBlocking

class FakeCallsLocalDataSource: CallsDao {
    var contentProviderData: LinkedHashMap<String, Call> = LinkedHashMap()

    private val observableCalls = MutableLiveData<List<Call>>()

    private fun refreshCalls() {
        observableCalls.value = contentProviderData.values.toList()
    }

    override fun observeCalls(): LiveData<List<Call>> {
        runBlocking { refreshCalls() }
        return observableCalls
    }

    fun addCalls(vararg calls: Call) {
        for (call in calls) {
            contentProviderData["${call.number}-${call.date}"] = call
        }
        runBlocking { refreshCalls() }
    }
}