package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_tasks.view.*


class TasksFragment : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)
        mySharePreferences = this.context?.let { MySharePreferences(it) }!!

        if(!mySharePreferences.getAutoPlan()){
            view.one_time_tasks.visibility = View.GONE
            view.fixed_tasks.text = "Разовые задачи"
        }

        initUI()
        initButtons(view)

        return view
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_green) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#13A678")))
    }

    private fun initButtons(view: View)
    {
        val bundle = Bundle()
        view.one_time_tasks.setOnClickListener {
            bundle.putString("type", "one_time")
            Navigation.findNavController(view).navigate(R.id.tasks_types, bundle)
        }

        view.fixed_tasks.setOnClickListener {
            bundle.putString("type", "fixed")
            Navigation.findNavController(view).navigate(R.id.tasks_types, bundle)
        }

        view.routine_tasks.setOnClickListener {
            bundle.putString("type", "routine")
            Navigation.findNavController(view).navigate(R.id.tasks_types, bundle)
        }
    }
}