package com.andrijaperusic.mycallapp.data

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import com.andrijaperusic.mycallapp.data.models.Contact
import com.andrijaperusic.mycallapp.data.models.PhoneNumber

class ContactRepository(private val context: Context) {

    companion object {

        @Volatile
        private var INSTANCE: ContactRepository? = null

        fun getInstance(context: Context): ContactRepository {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = ContactRepository(context.applicationContext)
                }
                return INSTANCE as ContactRepository
            }
        }
    }

    fun getContacts(): LiveData<List<Contact>> {
        return ContactsLiveData(context)
    }

    fun getContactWithPhoneNumbers(lookupKey: String): LiveData<Contact> {
        return ContactDetailLiveData(lookupKey, context)
    }

    class ContactDetailLiveData(
        private val lookupKey: String,
        private val context: Context
    ) : ContentProviderLiveData<Contact>(
        context,
        Uri.withAppendedPath(CONTACT_URI, lookupKey)) {

        companion object {
            private val CONTACT_URI = ContactsContract.Contacts.CONTENT_LOOKUP_URI
            private val CONTACT_PROJECTION: Array<String> = arrayOf(
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_URI
            )
            private val PHONE_URI = ContactsContract.Data.CONTENT_URI
            private val PHONE_PROJECTION: Array<String> = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE
            )
            private const val PHONE_SELECTION = "${ContactsContract.Data.LOOKUP_KEY} = ? AND " +
                    "${ContactsContract.Data.MIMETYPE} = '${ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE}'"

        }

        override suspend fun getContentProviderValue(): Contact {
            val contentUri = Uri.withAppendedPath(CONTACT_URI, lookupKey);
            val cursor = context.contentResolver.query(
                contentUri,
                CONTACT_PROJECTION,
                null, null, null
            )
            var key: String? = null
            var name: String? = null
            var photoUri: String? = null
            cursor?.apply {
                moveToFirst()
                key = getString(getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                name = getString(getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                photoUri = getString(getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                close()
            }
            val phoneNumberList: MutableList<PhoneNumber> = mutableListOf()
            val phoneCursor = context.contentResolver.query(
                PHONE_URI,
                PHONE_PROJECTION,
                PHONE_SELECTION,
                arrayOf(lookupKey),
                null
            )

            phoneCursor?.apply {
                while (moveToNext()) {
                    val number = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val type = getInt(getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                    phoneNumberList.add(PhoneNumber(number, type))
                }
                close()
            }

            return Contact(key!!, name!!, photoUri?.let { return@let Uri.parse(photoUri) }, phoneNumberList)
        }
    }

    class ContactsLiveData(
        private val context: Context
    ) : ContentProviderLiveData<List<Contact>>(context,
        uri
    ) {

        override suspend fun getContentProviderValue(): List<Contact> {
            val cursor = context.contentResolver.query(
                uri,
                PROJECTION,
                SELECTION,
                null,
                SORT_ORDER
            )
            val contactsList: MutableList<Contact> = mutableListOf()
            cursor?.apply {
                val lookupKeyIndex = getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
                val nameIndex = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val photoIndex = getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)

                while (moveToNext()) {
                    val key = getString(lookupKeyIndex)
                    val name = getString(nameIndex)
                    val photoUri = getString(photoIndex)
                    contactsList.add(
                        Contact(
                            key,
                            name,
                            photoUri?.let { return@let Uri.parse(photoUri) }
                        )
                    )
                }
                close()
            }
            return contactsList
        }

        companion object {

            private val uri =  ContactsContract.Contacts.CONTENT_URI

            private val PROJECTION: Array<out String> = arrayOf(
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
            )

            private const val SELECTION: String = "${ContactsContract.Contacts.HAS_PHONE_NUMBER} = 1"

            private const val SORT_ORDER = ContactsContract.Contacts.DISPLAY_NAME
        }
    }
}