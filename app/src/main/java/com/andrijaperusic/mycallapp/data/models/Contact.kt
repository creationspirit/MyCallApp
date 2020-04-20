package com.andrijaperusic.mycallapp.data.models

import android.net.Uri

data class Contact(
    val lookupKey: String,
    val name: String,
    val photoUri: Uri?,
    val phoneNumbers: List<PhoneNumber> = listOf()
)