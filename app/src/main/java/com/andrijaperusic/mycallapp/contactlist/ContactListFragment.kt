package com.andrijaperusic.mycallapp.contactlist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.andrijaperusic.mycallapp.MyCallApplication
import com.andrijaperusic.mycallapp.R
import com.andrijaperusic.mycallapp.TabsContainerFragmentDirections
import com.andrijaperusic.mycallapp.databinding.FragmentContactListBinding
import com.andrijaperusic.mycallapp.util.MY_PERMISSIONS_REQUEST_READ_CONTACTS
import com.google.android.material.snackbar.Snackbar

class ContactListFragment : Fragment() {

    private val viewModel by viewModels<ContactListViewModel> {
        ContactListViewModel.ContactListViewModelFactory(
            (requireContext().applicationContext as MyCallApplication).contactDataSource
        )
    }

    private lateinit var binding: FragmentContactListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_contact_list, container, false)

        binding.lifecycleOwner = this
        checkReadContactPermissionAndSetupViewModel()
        return binding.root
    }

    private fun checkReadContactPermissionAndSetupViewModel() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED) {
            setupViewModel()
        } else {
            requestReadContactsPermission()
        }
    }

    private fun setupViewModel() {
        val contactListAdapter = ContactListAdapter( ContactListener {
            viewModel.onContactClicked(it)
        })
        binding.contactList.adapter = contactListAdapter

        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            it?.let {
                contactListAdapter.addDividersAndSubmitList(it)
            }
        })

        binding.contactListViewModel = viewModel

        viewModel.navigateToContactDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    TabsContainerFragmentDirections.actionTabsContainerFragmentToContactDetailFragment(it))
                viewModel.doneNavigating()
            }

        })
    }

    private fun requestReadContactsPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                R.string.contact_access_required,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS) }.show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               checkReadContactPermissionAndSetupViewModel()
            }
        }
    }
}
