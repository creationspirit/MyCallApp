package com.andrijaperusic.mycallapp.contactdetail

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrijaperusic.mycallapp.data.models.PhoneNumber
import com.andrijaperusic.mycallapp.databinding.ListItemPhoneNumberBinding

class PhoneNumberListAdapter(
    val clickListener: PhoneNumberListener
): ListAdapter<PhoneNumber, PhoneNumberListAdapter.ViewHolder>(PhoneNumberListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder private constructor(
        private val binding: ListItemPhoneNumberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPhoneNumberBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: PhoneNumber, clickListener: PhoneNumberListener) {
            binding.phoneNumber = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }
}

class PhoneNumberListDiffCallback: DiffUtil.ItemCallback<PhoneNumber>() {
    override fun areItemsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: PhoneNumber, newItem: PhoneNumber): Boolean {
        return oldItem == newItem
    }

}

class PhoneNumberListener(val clickListener: (phoneNumber: Uri) -> Unit) {
    fun onClick(phoneNumber: PhoneNumber) = clickListener(phoneNumber.numberUri)
}