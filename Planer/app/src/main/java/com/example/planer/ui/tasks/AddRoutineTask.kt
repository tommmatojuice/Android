package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
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

class AddRoutineTask  : Fragment()
{
    private val taskViewModel: TaskViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_routine_task, container, false)
        val task = arguments?.getSerializable("task") as Task?

        initUI(view)
        initButtons(view, task)
        initTask(view, task)

        return view
    }

    @SuppressLint("NewApi")
    private fun initTask(view: View, task: Task?){
        if(task != null) {
            view.task_title.setText(task.title)
            view.task_description.setText(task.description)
            view.begin_work_time.text = task.begin
            view.end_work_time.text = task.end
            view.checkBoxMon.isChecked = task.monday!!
            view.checkBoxTue.isChecked = task.tuesday!!
            view.checkBoxWed.isChecked = task.wednesday!!
            view.checkBoxThu.isChecked = task.thursday!!
            view.checkBoxFri.isChecked = task.friday!!
            view.checkBoxSat.isChecked = task.saturday!!
            view.checkBoxSun.isChecked = task.sunday!!
        }
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
    private fun initButtons(view: View, task: Task?)
    {
        view.save_button.setOnClickListener {
            saveTask(view, task)
        }

        view.begin_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.begin_work_time, it1) }
        }

        view.end_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.end_work_time, it1) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View, task: Task?)
    {
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
                        if(task != null){
                            task.title = view.task_title.text.toString()
                            task.description = view.task_description.text.toString()
                            task.begin = view.begin_work_time.text.toString()
                            task.end = view.end_work_time.text.toString()
                            task.monday =view.checkBoxMon.isChecked
                            task.tuesday =view.checkBoxTue.isChecked
                            task.wednesday = view.checkBoxWed.isChecked
                            task.thursday = view.checkBoxThu.isChecked
                            task.friday = view.checkBoxFri.isChecked
                            task.saturday = view.checkBoxSat.isChecked
                            task.sunday = view.checkBoxSun.isChecked
                            task.let { taskViewModel.update(it) }
                        } else {
                            taskViewModel.insert(Task(
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
                        }

                        val navBuilder = NavOptions.Builder()
                        if (group == null) {
                            arguments?.putString("choice", "all")
                            val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.all_tasks, true).build()
                            Navigation.findNavController(view).navigate(R.id.all_tasks, arguments, navOptions)
                        } else {
                            arguments?.putString("choice", "groups")
                            val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.group_tasks, true).build()
                            Navigation.findNavController(view).navigate(R.id.group_tasks, arguments, navOptions)
                        }
                    } else this.context?.let { ToastMessages.showMessage(it, "Необходимо выбрать хотя бы один день недели") }
                }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести время начала и окончания") }
    }
}