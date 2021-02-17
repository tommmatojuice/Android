package com.example.planer.ui.tasks

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.planer.R
import com.example.planer.adapters.TabsAdapter
import com.google.android.material.tabs.TabLayout

class AllTasksFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_tasks, container, false)

        val viewPagerAdapter = activity?.supportFragmentManager?.let { TabsAdapter(it) }
        val viewPager =  view.findViewById<ViewPager>(R.id.myViewPager)
        val tabLayout =  view.findViewById<TabLayout>(R.id.tabLayout)

        viewPager.adapter = viewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        initUi()

        return view
    }

    private fun initUi(){
        when(arguments?.getInt("type")){
            1 -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все разовые задачи" + "</font>"))
            2 -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все фиксированные задачи" + "</font>"))
            3 -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все регулярные задачи" + "</font>"))
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}