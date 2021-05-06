package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.MySharePreferences
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_tasks_types.view.*

class TasksTypesFragment : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_tasks_types, container, false)
        mySharePreferences = this.context?.let { MySharePreferences(it) }!!

        initButtons(view)
        initUi()

        return view
    }

    private fun initButtons(view: View){
        view.all_tasks.setOnClickListener {
            arguments?.putString("choice", "all")
            Navigation.findNavController(view).navigate(R.id.all_tasks, arguments)
        }

        view.group_tasks.setOnClickListener {
            arguments?.putString("choice", "group")
            Navigation.findNavController(view).navigate(R.id.all_tasks, arguments)
        }
    }

    private fun initUi()
    {
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_green) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#13A678")))

        (activity as AppCompatActivity).supportActionBar?.title = "Разовые задачи"
        when(arguments?.getString("type")){
            "one_time" -> (activity as AppCompatActivity).supportActionBar?.title = "Разовые задачи"
            "fixed" -> {
                if(mySharePreferences.getAutoPlan()){
                    (activity as AppCompatActivity).supportActionBar?.title = "Фиксированные задачи"
                } else {
                    (activity as AppCompatActivity).supportActionBar?.title = "Разовые задачи"
                }
            }
            "routine" -> (activity as AppCompatActivity).supportActionBar?.title = "Регулярные задачи"
        }
    }
}