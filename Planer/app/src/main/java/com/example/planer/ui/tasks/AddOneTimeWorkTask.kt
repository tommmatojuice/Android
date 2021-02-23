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
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
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

class AddOneTimeWorkTask : Fragment(), DatePickerDialog.OnDateSetListener, SeekBar.OnSeekBarChangeListener  {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_one_time_work_task, container, false)

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
        color?.let { view.work_button.setBackgroundColor(it) }
        color?.let { view.deadline_button.setBackgroundColor(it) }
        color?.let { view.save_button.setBackgroundColor(it) }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButtons(view: View)
    {
        view.save_button.setOnClickListener {
            saveTask(view)
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
            view?.deadline?.text = "$year-0$newMonth-$day"
        } else view?.deadline?.text = "$year-$newMonth-$day"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View)
    {
        val taskViewModel = activity?.application?.let { TaskViewModel(it, "", "") }
        val time = LocalTime.parse(view.work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.task_title.text.isNotEmpty()){
            if (view.work_time.text.isNotEmpty()){
                    if (view.deadline.text.isNotEmpty()){
                        if (!view.checkBoxMon.isChecked || !view.checkBoxTue.isChecked || !view.checkBoxWed.isChecked || !view.checkBoxThu.isChecked ||
                                !view.checkBoxFri.isChecked || !view.checkBoxSat.isChecked || !view.checkBoxSun.isChecked) {
                            taskViewModel?.insert(Task(
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
                            arguments?.putString("choice", "all")
                            Navigation.findNavController(view).navigate(R.id.all_tasks, arguments)
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