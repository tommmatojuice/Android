package com.example.lab2_levashova

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager


class MainActivity : FragmentActivity(), recycler_view.OnItemClickListenerMain
{
    val NUM_PAGES = DataStorage.getVersionsList().size
    private lateinit var mPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPager = findViewById(R.id.pager)
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter
    }

    override fun onItemClickMain(position: Int)
    {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag, each_item_fragment().newInstance(position.toString()))
            .commit()
    }

    override fun onBackPressed()
    {
        if (mPager.currentItem == 0)
            super.onBackPressed()
        else
            mPager.currentItem = 0
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm)
    {
        override fun getCount(): Int = NUM_PAGES+1
        override fun getItem(position: Int): Fragment {
            return when(position){
                0 ->  recycler_view()
                else ->  each_item_fragment().newInstance((position-1).toString())
            }
        }
    }
}



