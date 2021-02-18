package com.example.planer.ui.tasks

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.planer.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_tasks.view.*


class TasksFragment : Fragment() {

    private lateinit var tasksViewModel: TasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)

        initUI()
        initButtons(view)

        return view
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_green) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#13A678")))
        (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Задачи" + "</font>"))
    }

    private fun initButtons(view: View){
        val bundle = Bundle()

        view.one_time_tasks.setOnClickListener {
//            bundle.putInt("type", 1)
            bundle.putString("type", "one_time")
            Navigation.findNavController(view).navigate(R.id.tasks_types, bundle)
        }

        view.fixed_tasks.setOnClickListener {
            bundle.putString("type", "fixed")
//            bundle.putInt("type", 2)
            Navigation.findNavController(view).navigate(R.id.tasks_types, bundle)
        }

        view.routine_tasks.setOnClickListener {
            bundle.putString("type", "routine")
//            bundle.putInt("type", 3)
            Navigation.findNavController(view).navigate(R.id.tasks_types, bundle)
        }
    }
}