package com.andrijaperusic.mycallapp.contactdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.andrijaperusic.mycallapp.PlaceCallPermissionFragment
import com.andrijaperusic.mycallapp.R
import com.andrijaperusic.mycallapp.databinding.FragmentContactDetailBinding

class ContactDetailFragment : PlaceCallPermissionFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentContactDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_contact_detail, container, false)

        val phoneNumberListAdapter = PhoneNumberListAdapter(PhoneNumberListener {
            placeCall(it)
        })

        binding.phoneNumberList.adapter = phoneNumberListAdapter

        binding.lifecycleOwner = this

        val application = requireNotNull(this.activity).application

        val arguments = ContactDetailFragmentArgs.fromBundle(requireArguments())
        val viewModel: ContactDetailViewModel by viewModels {
            ContactDetailViewModelFactory(arguments.lookupKey, application)
        }

        binding.contactDetailViewModel = viewModel

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
