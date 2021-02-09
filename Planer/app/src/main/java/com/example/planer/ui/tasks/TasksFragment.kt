package com.example.planer.ui.tasks

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.planer.ui.profile.ProfileViewModel
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
import com.example.planer.MainActivity
import com.example.planer.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class TasksFragment : Fragment() {

    private lateinit var tasksViewModel: TasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)

        val textView: TextView = view.findViewById(R.id.tasks_text)
        tasksViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        initUI()

        return view
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_green) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#13A678")))
        (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Задачи" + "</font>"))
    }
}