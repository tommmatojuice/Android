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
import com.example.planer.util.ToastMessages
import com.google.android.material.tabs.TabLayout

class AllTasksFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_all_tasks, container, false)
        val type: String? = arguments?.getString("type")

        val viewPagerAdapter = type?.let { TabsAdapter(childFragmentManager, it) }

        val viewPager =  view.findViewById<ViewPager>(R.id.myViewPager)
        val tabLayout =  view.findViewById<TabLayout>(R.id.tabLayout)

        viewPager.adapter = viewPagerAdapter

        tabLayout.setupWithViewPager(viewPager)

        initUi()

        return view
    }

    private fun initUi(){
        when(arguments?.getString("type")){
            "one_time" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все разовые задачи" + "</font>"))
            "fixed" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все фиксированные задачи" + "</font>"))
            "routine" -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Все регулярные задачи" + "</font>"))
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}