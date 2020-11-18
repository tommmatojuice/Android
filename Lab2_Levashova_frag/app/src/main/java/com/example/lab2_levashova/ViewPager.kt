package com.example.lab2_levashova

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

class ViewPager : Fragment()
{
    private lateinit var mPager: ViewPager
    private var android = DataStorage.getVersionsList()
    var pos:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        pos = arguments?.getInt("POSITION") ?: 0
        mPager = view.findViewById(R.id.pager)
        val pagerAdapter = activity?.supportFragmentManager?.let {FragmentAdapter(it, android) }
        mPager.adapter = pagerAdapter
        mPager.currentItem = pos

        return view
    }

    fun newInstance(position: Int): com.example.lab2_levashova.ViewPager
    {
        val fragment = ViewPager()
        val args: Bundle = Bundle()
        args.putInt("POSITION", position)
        fragment.arguments = args
        return fragment
    }

    class FragmentAdapter(
        fragmentManager: FragmentManager,
        private val arrayList: List<Android>
    ) : FragmentStatePagerAdapter(fragmentManager)
    {
        override fun getItem(position: Int): Fragment
        {
            val versions = when {
                arrayList.isNotEmpty() && position <= count - 1 -> arrayList[position]
                else -> null
            }
            return versions.run { EachItemFragment().newInstance(this) }
        }

        override fun getCount(): Int {
            return arrayList.size
        }
    }
}