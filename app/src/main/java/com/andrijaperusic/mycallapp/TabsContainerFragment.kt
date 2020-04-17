package com.andrijaperusic.mycallapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andrijaperusic.mycallapp.calllist.CallListFragment
import com.andrijaperusic.mycallapp.contactlist.ContactListFragment
import com.andrijaperusic.mycallapp.databinding.FragmentTabsContainerBinding
import com.google.android.material.tabs.TabLayoutMediator
import timber.log.Timber

class TabsContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTabsContainerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tabs_container, container, false)
        binding.lifecycleOwner = this

        binding.viewPager.adapter = TabsStateAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.apply {
                text = when(position) {
                    0 -> getString(R.string.tab_text_contacts)
                    1 -> getString(R.string.tab_text_recent)
                    else -> throw IllegalArgumentException("Illegal tab position")
                }
                setIcon(when(position) {
                    0 -> R.drawable.ic_person_black_24dp
                    1 -> R.drawable.ic_call_black_24dp
                    else -> throw IllegalArgumentException("Illegal tab position")
                })
            }
        }.attach()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Timber.i("Tab fragment resumed")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("Tab fragment paused")
    }

    class TabsStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> ContactListFragment()
                1 -> CallListFragment()
                else -> throw IllegalArgumentException("Illegal tab position")
            }
        }
    }
}
