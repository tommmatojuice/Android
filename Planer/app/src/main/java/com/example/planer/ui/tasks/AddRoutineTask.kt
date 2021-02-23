package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_add_fixed_task.view.*
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.*
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.*
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.begin_work_button
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.begin_work_time
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.checkBoxFri
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.checkBoxMon
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.checkBoxSat
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.checkBoxSun
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.checkBoxThu
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.checkBoxTue
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.checkBoxWed
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.end_work_button
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.end_work_time
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.save_button
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.task_description
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.task_title
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AddRoutineTask  : Fragment()
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_routine_task, container, false)

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
        color?.let { view.begin_work_button.setBackgroundColor(it) }
        color?.let { view.end_work_button.setBackgroundColor(it) }
        color?.let { view.save_button.setBackgroundColor(it) }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButtons(view: View)
    {
        view.save_button.setOnClickListener {
            saveTask(view)
        }

        view.begin_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.begin_work_time, it1) }
        }

        view.end_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.end_work_time, it1) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View)
    {
        val taskViewModel = activity?.application?.let { TaskViewModel(it, "work", "fixed") }

        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.begin_work_time.text.isNotEmpty() && view.end_work_time.text.isNotEmpty()){
            val time1 = LocalTime.parse(view.begin_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val time2 = LocalTime.parse(view.end_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if (view.task_title.text.isNotEmpty()){
                if(time1 > time2 || time1 == time2){
                    this.context?.let { ToastMessages.showMessage(it, "Неверно введено время начала или окончания") }
                } else {
                    if (!view.checkBoxMon.isChecked || !view.checkBoxTue.isChecked || !view.checkBoxWed.isChecked || !view.checkBoxThu.isChecked ||
                            !view.checkBoxFri.isChecked || !view.checkBoxSat.isChecked || !view.checkBoxSun.isChecked) {
                        taskViewModel?.insert(Task(
                                "routine",
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
                                false,
                                null,
                                view.begin_work_time.text.toString(),
                                view.end_work_time.text.toString(),
                                group
                            )
                        )
                        arguments?.putString("choice", "all")
                        Navigation.findNavController(view).navigate(R.id.all_tasks, arguments)
                    } else this.context?.let { ToastMessages.showMessage(it, "Необходимо выбрать хотя бы один день недели") }
                }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести время начала и окончания") }
    }
}