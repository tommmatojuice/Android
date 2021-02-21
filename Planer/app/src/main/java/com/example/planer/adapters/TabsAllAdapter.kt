package com.example.planer.adapters

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.planer.ui.tasks.TaskRecyclerFragment

class TabsAllAdapter(fm: FragmentManager, private val type: String) : FragmentPagerAdapter(fm)
{
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TaskRecyclerFragment(type, "work")
            1 -> TaskRecyclerFragment(type, "rest")
            else -> {
                return TaskRecyclerFragment(type, "other")
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
        Log.d("@@@@@@@@@@@@@@@@", type.toString())
        return super.getItemPosition(`object`)
    }
}