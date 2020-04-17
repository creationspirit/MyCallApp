package com.andrijaperusic.mycallapp.util

import android.content.res.ColorStateList
import android.provider.CallLog
import android.provider.ContactsContract
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.andrijaperusic.mycallapp.R
import com.andrijaperusic.mycallapp.data.models.Call
import com.andrijaperusic.mycallapp.data.models.Contact
import com.andrijaperusic.mycallapp.data.models.PhoneNumber
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Binding adapter functions for declarative usage in the template system
 */


@BindingAdapter("avatarSrc")
fun CircleImageView.setAvatarImageSource(contact: Contact?) {
    contact?.let {
        if (contact.photoUri == null) {
            setImageResource(R.mipmap.ic_avatar_round)
        } else {
            setImageURI(contact.photoUri)
        }
    }
}

@BindingAdapter("recentCallIcon")
fun ImageView.setRecentCallIcon(call: Call?) {
    call?.let {
        setImageResource(when(call.type) {
            CallLog.Calls.INCOMING_TYPE ->  R.drawable.ic_call_received_black_24dp
            CallLog.Calls.OUTGOING_TYPE -> R.drawable.ic_call_made_black_24dp
            CallLog.Calls.MISSED_TYPE -> R.drawable.ic_call_missed_black_24dp
            CallLog.Calls.REJECTED_TYPE -> R.drawable.ic_close_black_24dp
            else -> 0
        })
        val tint = ContextCompat.getColor(context, when(call.type) {
            CallLog.Calls.MISSED_TYPE -> R.color.colorError
            else -> R.color.colorPrimaryDark
        })
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(tint));
    }
}

@BindingAdapter("recentCallNumberOrName")
fun TextView.setRecentCallNumberOrName(call: Call?) {
    call?.let {
        text = when (call.name) {
            null -> call.number
            else -> call.name
        }
    }
}

@BindingAdapter("recentCallDurationText")
fun TextView.setRecentCallDurationText(call: Call?) {
    call?.let {
        val typeFormatted = when (call.type) {
            CallLog.Calls.INCOMING_TYPE -> context.getString(R.string.incoming)
            CallLog.Calls.OUTGOING_TYPE -> context.getString(R.string.outgoing)
            CallLog.Calls.MISSED_TYPE -> context.getString(R.string.missed)
            CallLog.Calls.REJECTED_TYPE -> context.getString(R.string.rejected)
            else -> context.getString(R.string.other)
        }
        val durationFormatted: String? = when (call.duration) {
            0L -> context.getString(R.string.did_not_connect)
            else -> call.durationFormatted
        }
        text = when (call.type) {
            CallLog.Calls.INCOMING_TYPE, CallLog.Calls.OUTGOING_TYPE ->  "${typeFormatted}, $durationFormatted"
            else -> typeFormatted
        }
    }
}

@BindingAdapter("phoneNumberType")
fun TextView.phoneNumberType(phoneNumber: PhoneNumber?) {
    phoneNumber?.let {
        text = when (phoneNumber.type) {
            ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> context.getString(R.string.type_home)
            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> context.getString(R.string.type_mobile)
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> context.getString(R.string.type_work)
            else -> context.getString(R.string.other)
        }
    }
}

@BindingAdapter("titleImage")
fun ImageView.setTitleImage(contact: Contact?) {
    contact?.let {
        if (contact.photoUri != null) {
            setImageURI(contact.photoUri)
        } else {
            setImageResource(R.drawable.ic_image_black_24dp)
            setBackgroundColor(context.getColor(R.color.colorPrimaryLight))
            val tint = context.getColor(R.color.colorPrimary)
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(tint));
        }
    }
}

@BindingAdapter("goneUnless")
fun View.goneUnless(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}



