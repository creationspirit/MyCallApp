package com.andrijaperusic.mycallapp.data

import android.content.Context
import android.provider.CallLog
import androidx.lifecycle.LiveData
import com.andrijaperusic.mycallapp.data.models.Call
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CallRepository(private val context: Context) {

    companion object {

        @Volatile
        private var INSTANCE: CallRepository? = null

        fun getInstance(context: Context): CallRepository {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = CallRepository(context.applicationContext)
                }
                return INSTANCE as CallRepository
            }
        }
    }

    fun getCalls(): LiveData<List<Call>> {
        return CallsLiveData(context)
    }

    class CallsLiveData(
        private val context: Context
    ) : ContentProviderLiveData<List<Call>>(context, URI) {

        override suspend fun getContentProviderValue(): List<Call> {
            val currentTime = System.currentTimeMillis()
            val halfYearAgoTime = currentTime - TimeUnit.MILLISECONDS.convert(365, TimeUnit.DAYS)

            val cursor = context.contentResolver.query(
                URI,
                PROJECTION,
                SELECTION,
                arrayOf(halfYearAgoTime.toString()),
                SORT_ORDER
            )
            Timber.i("${cursor?.count} calls fetched")
            val callList: MutableList<Call> = mutableListOf()
            cursor?.apply {
                while (moveToNext()) {
                    Timber.i("$position")
                    val number = getString(getColumnIndex(CallLog.Calls.NUMBER))
                    val type = getInt(getColumnIndex(CallLog.Calls.TYPE))
                    val date = getLong(getColumnIndex(CallLog.Calls.DATE))
                    val duration = getLong(getColumnIndex(CallLog.Calls.DURATION))
                    val name = getString(getColumnIndex(CallLog.Calls.CACHED_NAME))
                    val lookupUri = getString(getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI))

                    Timber.i("$number $name ${duration} ${date} ${type} ${lookupUri}")

                    callList.add(Call(number, date, type, duration, name, lookupUri))
                }
                close()
            }
            Timber.i("Call list, $callList")
            return callList
        }

        companion object {

            private val URI =  CallLog.Calls.CONTENT_URI

            private val PROJECTION: Array<out String> = arrayOf(
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.CACHED_LOOKUP_URI
            )

            private val SELECTION = "${CallLog.Calls.TYPE} IN " +
                    "(${CallLog.Calls.OUTGOING_TYPE}, ${CallLog.Calls.INCOMING_TYPE}, " +
                    "${CallLog.Calls.MISSED_TYPE}, ${CallLog.Calls.REJECTED_TYPE}) " +
                    "AND ${CallLog.Calls.DATE} > ? AND ${CallLog.Calls.NUMBER} is not null AND ${CallLog.Calls.NUMBER} != ''"

            private const val SORT_ORDER = "${CallLog.Calls.DATE} DESC"
        }
    }
}