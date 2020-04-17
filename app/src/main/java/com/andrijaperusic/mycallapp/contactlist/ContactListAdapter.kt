package com.andrijaperusic.mycallapp.contactlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrijaperusic.mycallapp.data.models.Contact
import com.andrijaperusic.mycallapp.databinding.ListItemContactBinding
import com.andrijaperusic.mycallapp.databinding.ListItemLetterDividerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_CONTACT = 0
private const val ITEM_VIEW_TYPE_DIVIDER = 1

class ContactListAdapter(val clickListener: ContactListener): ListAdapter<DataItem, RecyclerView.ViewHolder>(ContactListDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addDividersAndSubmitList(list: List<Contact>?) {
        adapterScope.launch {
            val items: List<DataItem> = when (list) {
                null -> listOf()
                else -> mapDividersWithContactList(list)
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    private fun mapDividersWithContactList(list: List<Contact>): List<DataItem> {
        val finalList = mutableListOf<DataItem>()
        var currentLetter: Char? = null
        for (contact in list) {
            if (currentLetter != contact.name[0].toUpperCase()) {
                currentLetter = contact.name[0].toUpperCase()
                finalList.add(DataItem.DividerItem(currentLetter))
            }
            finalList.add(DataItem.ContactItem(contact))
        }
        return finalList
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.ContactItem -> ITEM_VIEW_TYPE_CONTACT
            is DataItem.DividerItem -> ITEM_VIEW_TYPE_DIVIDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_CONTACT -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_DIVIDER -> DividerViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val dataItem = getItem(position) as DataItem.ContactItem
                holder.bind(dataItem.contact, clickListener)
            }
            is DividerViewHolder -> {
                val dataItem = getItem(position) as DataItem.DividerItem
                holder.bind(dataItem.letter)
            }
        }
    }

    class ViewHolder private constructor(
        private val binding: ListItemContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemContactBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Contact, clickListener: ContactListener) {
            binding.contact = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    class DividerViewHolder private constructor(
        private val binding: ListItemLetterDividerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): DividerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemLetterDividerBinding.inflate(layoutInflater, parent, false)
                return DividerViewHolder(binding)
            }
        }

        fun bind(letter: Char) {
            binding.sortLetter.text = letter.toString()
            binding.executePendingBindings()
        }
    }
}

class ContactListDiffCallback: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class ContactListener(val clickListener: (lookupKey: String) -> Unit) {
    fun onClick(contact: Contact) = clickListener(contact.lookupKey)
}

sealed class DataItem {
    data class ContactItem(val contact: Contact): DataItem() {
        override val id = contact.lookupKey
    }

    data class DividerItem(val letter: Char): DataItem() {
        override val id = "divider-${letter}"
    }

    abstract val id: String
}