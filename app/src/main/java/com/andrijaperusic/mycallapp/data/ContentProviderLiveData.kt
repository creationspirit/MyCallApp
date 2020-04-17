package com.andrijaperusic.mycallapp.data

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

/**
 * Abstract [LiveData] to observe Android's Content Provider changes.
 * Provide a [uri] to observe changes and implement [getContentProviderValue]
 * to provide data to post when content provider notifies a change.
 */
abstract class ContentProviderLiveData<T>(
    private val context: Context,
    private val uri: Uri
) : MutableLiveData<T>() {

    private lateinit var observer: ContentObserver

    private var contentProviderJob = Job()

    private val ioScope = CoroutineScope(Dispatchers.IO + contentProviderJob)

    override fun onActive() {
        observer = object : ContentObserver(null) {
            override fun onChange(self: Boolean) {
                ioScope.launch {
                    postValue(getContentProviderValue())
                }
            }
        }
        // Initiate first load of data
        observer.onChange(true)
        context.contentResolver.registerContentObserver(uri, true, observer)
    }

    override fun onInactive() {
        context.contentResolver.unregisterContentObserver(observer)
        contentProviderJob.cancel()
    }

    /**
     * Implement if you need to provide [T] value to be posted
     * when observed content is changed.
     */
    abstract suspend fun getContentProviderValue(): T
}