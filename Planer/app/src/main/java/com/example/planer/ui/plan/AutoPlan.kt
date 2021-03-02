package com.example.planer.ui.plan

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.planer.R
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.MySharePreferences
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class AutoPlan(private val date: String, private val weekDay: String): Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var fixedTasks: List<Task>
    private lateinit var routineTasks: List<Task>

    private var workTime: Int = 0
    private var beginTime: LocalTime = 0
    private lateinit var intervals: ArrayList<LocalTime>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_pomodoro, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        taskViewModel.fixedTasksByDate(date).observe(
                viewLifecycleOwner, { fixedTasks ->
                    this.fixedTasks = fixedTasks
            }
        )

        when(weekDay){
            "monday" -> {
                val time = LocalTime.parse(mySharePreferences.getMondayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getMondayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineMon.observe(
                        viewLifecycleOwner, { routineTasks ->
                    this.routineTasks = routineTasks
                }
                )
            }
            "tuesday" -> {
                val time = LocalTime.parse(mySharePreferences.getTuesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getTuesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineTue.observe(
                        viewLifecycleOwner, { routineTasks ->
                    this.routineTasks = routineTasks
                }
                )
            }
            "wednesday" -> {
                val time = LocalTime.parse(mySharePreferences.getWednesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getWednesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineWen.observe(
                        viewLifecycleOwner, { routineTasks ->
                    this.routineTasks = routineTasks
                }
                )
            }
            "thursday" -> {
                val time = LocalTime.parse(mySharePreferences.getThursdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getThursdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineThu.observe(
                        viewLifecycleOwner, { routineTasks ->
                    this.routineTasks = routineTasks
                }
                )
            }
            "friday" -> {
                val time = LocalTime.parse(mySharePreferences.getFridayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getFridayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineFri.observe(
                        viewLifecycleOwner, { routineTasks ->
                    this.routineTasks = routineTasks
                }
                )
            }
            "saturday" -> {
                val time = LocalTime.parse(mySharePreferences.getSaturdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getSaturdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineSat.observe(
                        viewLifecycleOwner, { routineTasks ->
                    this.routineTasks = routineTasks
                }
                )
            }
            "sunday" -> {
                val time = LocalTime.parse(mySharePreferences.getSundayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getSundayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineSun.observe(
                        viewLifecycleOwner, { routineTasks ->
                    this.routineTasks = routineTasks
                }
                )
            }
        }

        getAllWorkTime()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllWorkTime(){
        val currentTime = Calendar.getInstance().time
        fixedTasks.forEach {
            val timeBegin = LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val timeEnd = LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if(it.category == "work"){
                workTime -= if(timeEnd.hour < timeBegin.hour){
                    ((24 - timeBegin.hour)*60 + (60 - timeEnd.minute)) + (timeEnd.hour*60 + timeEnd.minute)
                } else (timeEnd.hour*60 + timeEnd.minute) - (timeBegin.hour*60 + timeBegin.minute)
            }
        }

        routineTasks.forEach {
            val timeBegin = LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val timeEnd = LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if(it.category == "work"){
                workTime -= if(timeEnd.hour < timeBegin.hour){
                    ((24 - timeBegin.hour)*60 + (60 - timeEnd.minute)) + (timeEnd.hour*60 + timeEnd.minute)
                } else (timeEnd.hour*60 + timeEnd.minute) - (timeBegin.hour*60 + timeBegin.minute)
            }
        }
    }

    private fun getPomodoroCount(): Int{
        return workTime/mySharePreferences.getPomodoroWork()!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPomodoros(){
        val tasks: MutableList<TasksForPlan> = mutableListOf()
        val intervals: MutableList<TasksForPlan> = mutableListOf()
        var work: Int = 0
        var time: Int = 0

        tasks.add(TasksForPlan(
                LocalTime.parse(mySharePreferences.getBreakfast(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                LocalTime.parse(mySharePreferences.getBreakfastEnd(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                null, null
        ))

        tasks.add(TasksForPlan(
                LocalTime.parse(mySharePreferences.getLunch(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                LocalTime.parse(mySharePreferences.getLunchEnd(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                null, null
        ))

        tasks.add(TasksForPlan(
                LocalTime.parse(mySharePreferences.getDiner(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                LocalTime.parse(mySharePreferences.getDinerEnd(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                null, null
        ))

        fixedTasks.forEach{
            tasks.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null,  it
            ))
        }

        routineTasks.forEach{
            tasks.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null, it
            ))
        }

        tasks.sortBy { it.begin }

        if(beginTime < tasks[0].begin){
            time =  tasks[0].begin.hour*60 + tasks[0].begin.minute - beginTime.hour*60 + beginTime.minute
            intervals.add(TasksForPlan(beginTime, tasks[0].begin, time, null))
            work += time
        }

        for(i in 1..tasks.size){
            if(work < workTime){
                time  = tasks[i].begin.hour*60 + tasks[i].begin.minute - tasks[i-1].end.hour*60 + tasks[i-1].end.minute
                intervals.add(TasksForPlan(tasks[i-1].end, tasks[i].begin, time, null))
                work += time
            } else break
        }
    }
}