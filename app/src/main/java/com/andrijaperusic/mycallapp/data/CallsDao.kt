package com.andrijaperusic.mycallapp.data

import androidx.lifecycle.LiveData
import com.andrijaperusic.mycallapp.data.models.Call

interface CallsDao {
    fun observeCalls(): LiveData<List<Call>>
}