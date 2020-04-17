package com.andrijaperusic.mycallapp.data.models

import android.net.Uri
import com.andrijaperusic.mycallapp.util.buildPhoneUri

data class PhoneNumber(
    val number: String,
    val type: Int
) {
    val numberUri: Uri
        get() = buildPhoneUri(number)
}