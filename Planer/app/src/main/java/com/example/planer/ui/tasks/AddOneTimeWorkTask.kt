package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.SeekBar
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
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.*
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.*
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.checkBoxFri
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.checkBoxMon
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.checkBoxSat
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.checkBoxSun
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.checkBoxThu
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.checkBoxTue
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.checkBoxWed
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.deadline_button
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.save_button
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.task_description
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.task_title
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AddOneTimeWorkTask : Fragment(), DatePickerDialog.OnDateSetListener, SeekBar.OnSeekBarChangeListener
{
    private val taskViewModel: TaskViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_one_time_work_task, container, false)
        val task = arguments?.getSerializable("task") as Task?

        initUI(view)
        initButtons(view, task)
        initTask(view, task)

        return view
    }

    @SuppressLint("NewApi", "SetTextI18n")
    private fun initTask(view: View, task: Task?){
        if(task != null) {
            val hours: String = if(task.duration?.div(60)!! < 10)
                "0" + task.duration?.div(60)
            else "0" + task.duration?.div(60)
            val minutes: String = if(task.duration?.rem(60)!! < 10)
                "0" + task.duration?.rem(60)
            else "0" + task.duration?.rem(60)

            view.task_title.setText(task.title)
            view.task_description.setText(task.description)
            view.work_time.text = "$hours:$minutes"
            view.count.text = task.complexity.toString()
            view.deadline.text = task.deadline
            task.complexity?.let { view.difficulty.setProgress(it, false) }
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
        color?.let { view.work_button.setBackgroundColor(it) }
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

        view.work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.work_time, it1) }
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

        view.difficulty.setOnSeekBarChangeListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int)
    {
        val newMonth = month+1
        if(newMonth < 10){
            if (day < 10){
                view?.deadline?.text = "$year-0$newMonth-0$day"
            } else view?.deadline?.text = "$year-0$newMonth-$day"
        } else {
            if (day < 10){
                view?.deadline?.text = "$year-$newMonth-0$day"
            } else view?.deadline?.text = "$year-$newMonth-$day"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View, task: Task?)
    {
        val time = LocalTime.parse(view.work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.task_title.text.isNotEmpty()){
            if (view.work_time.text.isNotEmpty()){
                    if (view.deadline.text.isNotEmpty()){
                        if (view.checkBoxMon.isChecked || view.checkBoxTue.isChecked || view.checkBoxWed.isChecked || view.checkBoxThu.isChecked ||
                                view.checkBoxFri.isChecked || view.checkBoxSat.isChecked || view.checkBoxSun.isChecked) {
                            if(task != null){
                                task.title = view.task_title.text.toString()
                                task.description = view.task_description.text.toString()
                                task.duration = time.hour*60 + time.minute
                                task.complexity = view.count.text.toString().toInt()
                                task.deadline = view.deadline.text.toString()
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
                                        "one_time",
                                        view.task_title.text.toString(),
                                        view.task_description.text.toString(),
                                        arguments?.getString("category").toString(),
                                        view.deadline.text.toString(),
                                        time.hour*60 + time.minute,
                                        view.count.text.toString().toInt(),
                                        view.checkBoxMon.isChecked ,
                                        view.checkBoxTue.isChecked ,
                                        view.checkBoxWed.isChecked ,
                                        view.checkBoxThu.isChecked ,
                                        view.checkBoxFri.isChecked ,
                                        view.checkBoxSat.isChecked ,
                                        view.checkBoxSun.isChecked ,
                                        false,
                                        null,
                                        null,
                                        null,
                                        group
                                )
                                )
                            }

                            val navBuilder = NavOptions.Builder()
                            if(group == null && arguments?.getBoolean("back") == null) {
                                arguments?.putString("choice", "all")
                                val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.all_tasks, true).build()
                                Navigation.findNavController(view).navigate(R.id.all_tasks, arguments, navOptions)
                            } else if(arguments?.getBoolean("back") != null){
                                Navigation.findNavController(view).navigate(R.id.navigation_plan)
                            } else {
                                arguments?.putString("choice", "groups")
                                val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.group_tasks, true).build()
                                Navigation.findNavController(view).navigate(R.id.group_tasks, arguments, navOptions)
                            }
                        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо выбрать хотя бы один день недели") }
                    } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести дедлайн") }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести время выполнения") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
    }

    override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
        view?.count?.text = seekBar?.progress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        view?.count?.text = seekBar?.progress.toString()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        view?.count?.text = seekBar?.progress.toString()
    }
}