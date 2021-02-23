package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_add_fixed_task.view.*
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.*
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.*
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.save_button
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.task_description
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.task_title
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddOneTimeOtherTask  : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_one_time_other_task, container, false)

        initUI(view)
        initButtons(view)

        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initUI(view: View)
    {
        var color: Int? = this.context?.let { ContextCompat.getColor(it, R.color.blue) }
        when(arguments?.getString("category")){
            "rest" ->{
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_green) }!!
            }
            "other" ->{
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_orange) }!!
            }
        }
        color?.let { view.save_button.setBackgroundColor(it) }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun initButtons(view: View)
    {
        view.save_button.setOnClickListener {
            saveTask(view)
        }
    }

    private fun saveTask(view: View){
        val taskViewModel = activity?.application?.let { TaskViewModel(it, "", "") }

        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.task_title.text.isNotEmpty()){
            if (!view.checkBoxMon.isChecked || !view.checkBoxTue.isChecked || !view.checkBoxWed.isChecked || !view.checkBoxThu.isChecked ||
                    !view.checkBoxFri.isChecked || !view.checkBoxSat.isChecked || !view.checkBoxSun.isChecked){
                taskViewModel?.insert(Task(
                        "one_time",
                        view.task_title.text.toString(),
                        view.task_description.text.toString(),
                        arguments?.getString("category").toString(),
                        null,
                        null,
                        null,
                        view.checkBoxMon.isChecked ,
                        view.checkBoxTue.isChecked ,
                        view.checkBoxWed.isChecked ,
                        view.checkBoxThu.isChecked ,
                        view.checkBoxFri.isChecked ,
                        view.checkBoxSat.isChecked ,
                        view.checkBoxSun.isChecked ,
                        view.priority.isChecked,
                        null,
                        null,
                        null,
                        group
                    )
                )
                arguments?.putString("choice", "all")
                Navigation.findNavController(view).navigate(R.id.all_tasks, arguments)
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо выбрать хотя бы один день недели") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
    }
}