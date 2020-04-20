package com.andrijaperusic.mycallapp.util

import org.junit.Test

import java.text.SimpleDateFormat
import com.google.common.truth.Truth.assertThat


class TimeUtilsTest {

    @Test
    fun convertLongToDateString_recentTime_HHmm() {
        val time = System.currentTimeMillis()
        val actual = convertLongToDateString(time)
        val expected = SimpleDateFormat("HH:mm")
            .format(time).toString()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun convertLongToDateString_formerTime_DDMMYYYY() {
        val time = 969660000000L
        val actual = convertLongToDateString(time)
        val expected = "23/09/2000"
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun convertSecondsToFormatted_seconds_mmss() {
        val seconds = 1432L
        val actual = convertSecondsToFormatted(seconds)
        assertThat(actual).isEqualTo("23:52")
    }

    @Test
    fun convertSecondsToFormatted_zeroSeconds_0000() {
        val seconds = 0L
        val actual = convertSecondsToFormatted(seconds)
        assertThat(actual).isEqualTo("00:00")
    }
}