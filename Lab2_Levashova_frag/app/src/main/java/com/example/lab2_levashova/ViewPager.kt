package com.example.lab2_levashova

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

class ViewPager : Fragment()
{
//    val NUM_PAGES = DataStorage.getVersionsList().size
//    private lateinit var mPager: ViewPager
//    private var position1: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        return view
    }

//    fun newInstance(position: String): com.example.lab2_levashova.ViewPager {
//        val fragment = ViewPager()
//        val args: Bundle = Bundle()
//        args.putString("POSITION", position)
//        fragment.arguments = args
//        return fragment
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_view_pager)
//
//        position1 = intent.extras?.get("position") as Int
//        mPager = findViewById(R.id.pager)
//        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
//        mPager.adapter = pagerAdapter
//    }
//
////    override fun onBackPressed() {
////        if (mPager.currentItem == 0) {
////            super.onBackPressed()
////        } else {
////            mPager.currentItem = mPager.currentItem - 1
////        }
////    }
//
//    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
//        override fun getCount(): Int = NUM_PAGES
//
//        override fun getItem(position: Int): Fragment = each_item_fragment().newInstance(position.toString())
//    }
}