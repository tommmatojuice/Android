package com.example.planer.ui.tasks

import android.annotation.SuppressLint
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
import com.example.planer.util.MySharePreferences
import com.google.android.material.tabs.TabLayout

class AllTasksFragment : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_all_tasks, container, false)
        mySharePreferences = this.context?.let { MySharePreferences(it) }!!
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

    private fun initUi()
    {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        if(arguments?.getString("choice") == "all"){
            when(arguments?.getString("type")){
                "one_time" -> (activity as AppCompatActivity).supportActionBar?.setTitle("Все разовые задачи")
                "fixed" -> {
                    if(mySharePreferences.getAutoPlan()){
                        (activity as AppCompatActivity).supportActionBar?.setTitle("Все фиксированные задачи")
                    } else {
                        (activity as AppCompatActivity).supportActionBar?.setTitle("Все разовые задачи")
                    }
                }
                "routine" -> (activity as AppCompatActivity).supportActionBar?.setTitle("Все регулярные задачи")
            }
        } else {
            when(arguments?.getString("type")){
                "one_time" -> (activity as AppCompatActivity).supportActionBar?.setTitle("Составные разовые задачи")
                "fixed" -> {
                        if(mySharePreferences.getAutoPlan()){
                            (activity as AppCompatActivity).supportActionBar?.setTitle("Составные фиксированные задачи")
                        } else {
                            (activity as AppCompatActivity).supportActionBar?.setTitle("Составные разовые задачи")
                        }
                    }
                "routine" -> (activity as AppCompatActivity).supportActionBar?.setTitle("Составные регулярные задачи")
            }
        }
    }
}