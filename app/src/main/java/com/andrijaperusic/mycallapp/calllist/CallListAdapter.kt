package com.andrijaperusic.mycallapp.calllist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andrijaperusic.mycallapp.data.models.Call
import com.andrijaperusic.mycallapp.databinding.ListItemCallBinding

class CallListAdapter(
    val clickListener: CallListener
): ListAdapter<Call, CallListAdapter.ViewHolder>(CallListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder private constructor(
        private val binding: ListItemCallBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCallBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Call, clickListener: CallListener) {
            binding.call = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }
}

class CallListDiffCallback: DiffUtil.ItemCallback<Call>() {
    override fun areItemsTheSame(oldItem: Call, newItem: Call): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: Call, newItem: Call): Boolean {
        return oldItem == newItem
    }

}

class CallListener(val clickListener: (number: String) -> Unit) {
    fun onClick(call: Call) = clickListener(call.number)
}