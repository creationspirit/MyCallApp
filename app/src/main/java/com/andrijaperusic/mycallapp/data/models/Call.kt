package com.andrijaperusic.mycallapp.data.models

import com.andrijaperusic.mycallapp.util.convertLongToDateString
import com.andrijaperusic.mycallapp.util.convertSecondsToFormatted

data class Call(
    val number: String,
    val date: Long,
    val type: Int,
    val duration: Long,
    val name: String?,
    val lookupUri: String?
) {

    val dateFormatted: String
        get() = convertLongToDateString(date)

    val durationFormatted: String
        get() = convertSecondsToFormatted(duration)
}