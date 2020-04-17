package com.andrijaperusic.mycallapp.util

import android.net.Uri
import android.telecom.PhoneAccount

fun buildPhoneUri(number: String): Uri {
    return Uri.fromParts(PhoneAccount.SCHEME_TEL, number, null)
}