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
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_add_fixed_task.view.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AddFixedTask()  : Fragment(), DatePickerDialog.OnDateSetListener
{
    private val taskViewModel: TaskViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_fixed_task, container, false)
        val task = arguments?.getSerializable("task") as Task?

        initUI(view)
        initButtons(view, task)
        initTask(view, task)

        return view
    }

    private fun initTask(view: View, task: Task?){
        if(task != null) {
            view.task_title.setText(task.title)
            view.task_description.setText(task.description)
            view.begin_work_time.text = task.begin
            view.end_work_time.text = task.end
            view.date.text = task.date
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initUI(view: View)
    {
        var color: Int? = this.context?.let { ContextCompat.getColor(it, R.color.blue) }
        when(arguments?.getString("category")){
            "rest" -> {
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_green) }!!
            }
            "other" -> {
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_orange) }!!
            }
        }
        color?.let { view.begin_work_button.setBackgroundColor(it) }
        color?.let { view.end_work_button.setBackgroundColor(it) }
        color?.let { view.deadline_button.setBackgroundColor(it) }
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

        view.deadline_button.setOnClickListener {
            val datePicker = this.context?.let { it1 ->
                DatePickerDialog(it1,
                        this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
            }
            datePicker?.show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int)
    {
        val newMonth = month+1
        if(newMonth < 10){
            if (day < 10){
                view?.date?.text = "$year-0$newMonth-0$day"
            } else view?.date?.text = "$year-0$newMonth-$day"
        } else {
            if (day < 10){
                view?.date?.text = "$year-$newMonth-0$day"
            } else view?.date?.text = "$year-$newMonth-$day"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View, task: Task?)
    {
        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.begin_work_time.text.isNotEmpty() && view.end_work_time.text.isNotEmpty()){
            val date1 = LocalTime.parse(view.begin_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val date2 = LocalTime.parse(view.end_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if (view.task_title.text.isNotEmpty()){
                if(date1 > date2 || date1 == date2){
                    this.context?.let { ToastMessages.showMessage(it, "Неверно введено время начала или окончания") }
                } else {
                    if (view.date.text.isNotEmpty())
                    {
                        if(task != null){
                            task.title = view.task_title.text.toString()
                            task.description = view.task_description.text.toString()
                            task.date = view.date.text.toString()
                            task.begin = view.begin_work_time.text.toString()
                            task.end = view.end_work_time.text.toString()
                            task.let { taskViewModel.update(it) }
                        } else {
                            taskViewModel.insert(Task(
                                    "fixed",
                                    view.task_title.text.toString(),
                                    view.task_description.text.toString(),
                                    arguments?.getString("category").toString(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    false,
                                    view.date.text.toString(),
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
                    } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести дату события") }
                }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести время начала и окончания") }
    }
}