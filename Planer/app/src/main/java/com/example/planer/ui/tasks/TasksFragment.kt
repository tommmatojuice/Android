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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_tasks.view.*
import kotlinx.android.synthetic.main.fragment_tasks_types.view.*

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
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(
            it,
            R.color.dark_green
        ) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                Color.parseColor(
                    "#13A678"
                )
            )
        )
        (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Задачи" + "</font>"))
    }

    private fun initButtons(view: View){

        view.one_time_tasks.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.tasks_types)
//            activity?.supportFragmentManager
//                ?.beginTransaction()
//                ?.addToBackStack(null)
//                ?.replace(R.id.main_frag, IntoTasksTypesFragment())
//                ?.commit()
        }

        view.fixed_tasks.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.tasks_types)
        }

        view.routine_tasks.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.tasks_types)
        }
    }
}