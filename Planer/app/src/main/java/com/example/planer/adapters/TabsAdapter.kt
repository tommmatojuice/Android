package com.example.planer.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.planer.ui.tasks.OtherRecyclerFragment
import com.example.planer.ui.tasks.RestRecyclerFragment
import com.example.planer.ui.tasks.WorkRecyclerFragment

@Suppress("DEPRECATION")
class TabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                WorkRecyclerFragment()
            }
            1 -> RestRecyclerFragment()
            else -> {
                return OtherRecyclerFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Работа"
            1 -> "Отдых"
            else -> {
                return "Другое"
            }
        }
    }
}