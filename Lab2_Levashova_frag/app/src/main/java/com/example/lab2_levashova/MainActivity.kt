package com.example.lab2_levashova

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.*
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

//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.main_frame, com.example.lab2_levashova.ViewPager())
//            .commit()



//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.recycler, com.example.lab2_levashova.ViewPager())
//            .commit()
    }

    override fun onItemClickMain(position: Int)
    {
        Log.d("@@@@@@@@@@@@@@@@@@@@@", "Click on item!!!")

        val fragment = each_item_fragment().newInstance(position.toString())
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag, fragment)
            .commit()

//        supportFragmentManager
//            .beginTransaction()
//            .addToBackStack(null)
//            .replace(R.id.recycler, ViewPager())
//            .commit()
//
//        mPager = findViewById(R.id.pager)
//        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
//        mPager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager.currentItem = 0
        }
    }

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES+1
//        override fun getItem(position: Int): Fragment = each_item_fragment().newInstance(position.toString())
        override fun getItem(position: Int): Fragment{
            return when(position){
                0 ->  recycler_view()
                else ->  each_item_fragment().newInstance((position-1).toString())
            }
        }
    }
}


