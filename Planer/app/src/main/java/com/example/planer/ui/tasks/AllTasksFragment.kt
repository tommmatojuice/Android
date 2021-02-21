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
import com.example.planer.adapters.TabsAllAdapter
import com.example.planer.adapters.TabsGroupAdapter
import com.google.android.material.tabs.TabLayout

class AllTasksFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_all_tasks, container, false)
        val type: String? = arguments?.getString("type")

        val viewPagerAdapterAll = type?.let { TabsAllAdapter(childFragmentManager, it) }
        val viewPagerAdapterGroup = type?.let { TabsGroupAdapter(childFragmentManager, it) }

        val viewPager =  view.findViewById<ViewPager>(R.id.myViewPager)
        val tabLayout =  view.findViewById<TabLayout>(R.id.tabLayout)

        if(arguments?.getString("choice") == "all"){
            viewPager.adapter = viewPagerAdapterAll
        } else {
            viewPager.adapter = viewPagerAdapterGroup
        }

        tabLayout.setupWithViewPager(viewPager)

        initUi()

        return view
    }

    private fun initUi(){
        if(arguments?.getString("choice") == "all"){
            when(arguments?.getString("type")){
                "one_time" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все разовые задачи" + "</font>"))
                "fixed" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все фиксированные задачи" + "</font>"))
                "routine" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все регулярные задачи" + "</font>"))
            }
        } else {
            when(arguments?.getString("type")){
                "one_time" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Составные разовые задачи" + "</font>"))
                "fixed" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Составные фиксированные задачи" + "</font>"))
                "routine" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Составные регулярные задачи" + "</font>"))
            }
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}