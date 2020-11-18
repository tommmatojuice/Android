package com.example.lab2_levashova

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.lang.reflect.Array.newInstance

class MainActivity : FragmentActivity(), Recycler.OnItemClickListenerMain
{
    private var android = DataStorage.getVersionsList()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag, Recycler())
            .commit()
    }

    override fun onItemClickMain(position: Int)
    {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag, ViewPager().newInstance(position))
            .commit()
    }

}





