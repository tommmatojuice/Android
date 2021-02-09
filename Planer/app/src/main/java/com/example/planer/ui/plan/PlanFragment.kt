package com.example.planer.ui.plan

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.planer.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PlanFragment : Fragment() {

    private lateinit var planViewModel: PlanViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        planViewModel = ViewModelProvider(this).get(PlanViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_plan, container, false)

        val textView: TextView = root.findViewById(R.id.text_home)
        planViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        initUI()

        return root
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_blue) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2574A9")))
        (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Сегодня" + "</font>"))
    }
}