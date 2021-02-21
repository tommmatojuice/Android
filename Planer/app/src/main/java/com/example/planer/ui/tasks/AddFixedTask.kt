package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.planer.R
import com.example.planer.adapters.TabsAllAdapter
import com.example.planer.adapters.TabsGroupAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_add_fixed_task.view.*

class AddFixedTask  : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_fixed_task, container, false)

        initUI(view)

        return view
    }

    private fun initUI(view: View){
        when(arguments?.getString("category")){
            "rest" ->{
                this.context?.let { ContextCompat.getColor(it, R.color.dark_green) }?.let { view.begin_work_button.setBackgroundColor(it) }
            }
            "other" ->{
                view.begin_work_button.setBackgroundColor(Color.parseColor("#F89406"))
            }
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}