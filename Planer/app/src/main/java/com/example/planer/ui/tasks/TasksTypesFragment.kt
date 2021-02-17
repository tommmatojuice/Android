package com.example.planer.ui.tasks

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_tasks_types.view.*

class TasksTypesFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_tasks_types, container, false)

        initButtons(view)
        initUi()

        return view
    }

    private fun initButtons(view: View){
        view.all_tasks.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.all_tasks, arguments)
        }

        view.group_tasks.setOnClickListener {

        }
    }

    private fun initUi(){
        when(arguments?.getInt("type")){
            1 -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Разовые задачи" + "</font>"))
            2 -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Фиксированные задачи" + "</font>"))
            3 -> (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Регулярные задачи" + "</font>"))
        }
    }
}