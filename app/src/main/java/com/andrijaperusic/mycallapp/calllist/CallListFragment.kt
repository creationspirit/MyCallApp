package com.andrijaperusic.mycallapp.calllist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.andrijaperusic.mycallapp.PlaceCallPermissionFragment
import com.andrijaperusic.mycallapp.R
import com.andrijaperusic.mycallapp.databinding.FragmentCallListBinding
import com.andrijaperusic.mycallapp.util.MY_PERMISSIONS_REQUEST_CALL_PHONE
import com.andrijaperusic.mycallapp.util.MY_PERMISSIONS_REQUEST_READ_CALL_LOG
import com.andrijaperusic.mycallapp.util.buildPhoneUri
import com.google.android.material.snackbar.Snackbar


class CallListFragment : PlaceCallPermissionFragment() {

    private lateinit var binding: FragmentCallListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_call_list, container, false)

        binding.lifecycleOwner = this

        checkCallLogPermissionAndSetupViewModel()

        return binding.root
    }

    private fun checkCallLogPermissionAndSetupViewModel() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG) ==
            PackageManager.PERMISSION_GRANTED) {
            setupViewModel()
        } else {
            requestReadCallLogPermission()
        }
    }

    private fun setupViewModel() {
        val application = requireNotNull(this.activity).application
        val viewModel: CallListViewModel by viewModels {
            CallListViewModelFactory(application)
        }

        val callListAdapter = CallListAdapter(CallListener {
            startActivity(Intent(Intent.ACTION_DIAL, buildPhoneUri(it)))
        })
        binding.callList.adapter = callListAdapter

        viewModel.calls.observe(viewLifecycleOwner, Observer {
            it?.let {
                callListAdapter.submitList(it)
            }
        })
    }

    private fun requestReadCallLogPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG)) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                R.string.read_call_log_required,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_CALL_LOG),
                        MY_PERMISSIONS_REQUEST_READ_CALL_LOG) }.show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CALL_LOG),
                MY_PERMISSIONS_REQUEST_READ_CALL_LOG)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CALL_LOG) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkCallLogPermissionAndSetupViewModel()
            }
        }
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                placeCall(lastPhoneUri)
            }
        }
    }
}
