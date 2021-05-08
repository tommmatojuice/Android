package com.example.planer.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.planer.ui.tasks.GroupRecyclerFragment

class TabsGroupAdapter(fm: FragmentManager, private val type: String) : FragmentPagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> GroupRecyclerFragment(type, "work")
            1 -> GroupRecyclerFragment(type, "rest")
            else -> {
                return GroupRecyclerFragment(type, "other")
            }
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Работа"
            1 -> "Отдых"
            else -> {
                return "Другое"
            }
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
}