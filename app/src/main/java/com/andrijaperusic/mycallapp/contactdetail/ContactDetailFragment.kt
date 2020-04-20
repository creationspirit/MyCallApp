package com.andrijaperusic.mycallapp.contactdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.andrijaperusic.mycallapp.MyCallApplication
import com.andrijaperusic.mycallapp.PlaceCallPermissionFragment
import com.andrijaperusic.mycallapp.R
import com.andrijaperusic.mycallapp.data.ContactsLocalDataSource
import com.andrijaperusic.mycallapp.databinding.FragmentContactDetailBinding

class ContactDetailFragment : PlaceCallPermissionFragment() {

    private val viewModel by viewModels<ContactDetailViewModel> {
        ContactDetailViewModel.ContactDetailViewModelFactory(
            (requireContext().applicationContext as MyCallApplication).contactDataSource
        )
    }

    private val args: ContactDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentContactDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_contact_detail, container, false)
        binding.lifecycleOwner = this

        val phoneNumberListAdapter = PhoneNumberListAdapter(PhoneNumberListener {
            placeCall(it)
        })

        binding.phoneNumberList.adapter = phoneNumberListAdapter

        binding.contactDetailViewModel = viewModel

        viewModel.setContactLookupKey(args.lookupKey)

        viewModel.contact.observe(viewLifecycleOwner, Observer {
            it?.let {
                phoneNumberListAdapter.submitList(it.phoneNumbers)
            }
        })

        viewModel.navigateToContactList.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                findNavController().navigateUp()
                viewModel.doneNavigating()
            }
        })
        return binding.root
    }
}
