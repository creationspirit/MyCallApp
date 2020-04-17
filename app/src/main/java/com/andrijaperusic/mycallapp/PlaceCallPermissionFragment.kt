package com.andrijaperusic.mycallapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andrijaperusic.mycallapp.util.MY_PERMISSIONS_REQUEST_CALL_PHONE
import com.google.android.material.snackbar.Snackbar

open class PlaceCallPermissionFragment: Fragment() {

    protected lateinit var lastPhoneUri: Uri

    protected fun placeCall(uri: Uri) {
        lastPhoneUri = uri
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED) {
            val telecomManager = requireActivity().getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            telecomManager.placeCall(uri, Bundle())
        } else {
            requestCallPhonePermission()
        }
    }

    private fun requestCallPhonePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                R.string.call_phone_required,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CALL_PHONE),
                        MY_PERMISSIONS_REQUEST_CALL_PHONE
                    ) }.show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.CALL_PHONE),
                MY_PERMISSIONS_REQUEST_CALL_PHONE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                placeCall(lastPhoneUri)
            }
        }
    }
}