package com.andrijaperusic.mycallapp.calllist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.andrijaperusic.mycallapp.data.CallRepository

class CallListViewModel(
    application: Application
): AndroidViewModel(application) {

    private val repository = CallRepository.getInstance(application)

    val calls = repository.getCalls()

}