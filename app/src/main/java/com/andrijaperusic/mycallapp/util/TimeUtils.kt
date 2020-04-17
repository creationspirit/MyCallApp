package com.andrijaperusic.mycallapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

/**
 * Util functions used for general time formatting tasks
 */

private val DAY_IN_MILLISECONDS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(time: Long): String {
    val oneDayAgo = System.currentTimeMillis() - DAY_IN_MILLISECONDS
    if (time > oneDayAgo) {
        return SimpleDateFormat("HH:mm")
            .format(time).toString()
    }
    return SimpleDateFormat("dd/MM/yyyy")
        .format(time).toString()
}

fun convertSecondsToFormatted(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds - hours * 3600) / 60
    val secondsLeft = seconds - hours * 3600 - minutes * 60

    if (hours > 0) {
        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${secondsLeft.toString().padStart(2, '0')}"
    }
    return "${minutes.toString().padStart(2, '0')}:${secondsLeft.toString().padStart(2, '0')}"
}