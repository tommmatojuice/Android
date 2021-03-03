package com.example.planer.ui.plan

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.MySharePreferences
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.*

class AutoPlan(private val date: String = "2021-03-03", private val weekDay: String = "wednesday"): Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences
    private val taskViewModel: TaskViewModel by viewModels()
    private var fixedTasks: List<Task>? = null
    private var routineTasks: List<Task>? = null
    private var oneTimeTasks: List<Task>? = null

    private var workTime: Int = 0
    private var dayNumber: Int = 0
    private lateinit var beginTime: LocalTime
    private var intervals: MutableList<TasksForPlan> = mutableListOf()
    private var pomodoros: MutableList<TasksForPlan> = mutableListOf()
    private var tasks: MutableList<Task> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.auto_plan, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        taskViewModel.fixedTasksByDate(date).observe(
                viewLifecycleOwner, { fixedTasks ->
            if(fixedTasks != null){
                this.fixedTasks = fixedTasks
                initWeekDay()
            }
        }
        )

        Log.d("first_work_time", workTime.toString())
        Log.d("date", date)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initWeekDay(){
        when(weekDay){
            "monday" -> {
                val time = LocalTime.parse(mySharePreferences.getMondayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                beginTime = LocalTime.parse(mySharePreferences.getMondayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineMon.observe(
                        viewLifecycleOwner, { routineTasks ->
                    if(routineTasks != null){
                        this.routineTasks = routineTasks
                        getAllWorkTime()
                        getPomodoros()
                    }
                })

                taskViewModel.oneTimeMon("work").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if(oneTimeTasks != null){
                        this.oneTimeTasks = oneTimeTasks
                        initTasks()
                    }
                })
            }
            "tuesday" -> {
                val time = LocalTime.parse(mySharePreferences.getTuesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                dayNumber = 1
                beginTime = LocalTime.parse(mySharePreferences.getTuesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineTue.observe(
                        viewLifecycleOwner, { routineTasks ->
                    if(routineTasks != null){
                        this.routineTasks = routineTasks
                        getAllWorkTime()
                        getPomodoros()
                    }
                }
                )

                taskViewModel.oneTimeTue("work").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if(oneTimeTasks != null){
                        this.oneTimeTasks = oneTimeTasks
                        initTasks()
                    }
                })
            }
            "wednesday" -> {
                val time = LocalTime.parse(mySharePreferences.getWednesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                dayNumber = 2
                beginTime = LocalTime.parse(mySharePreferences.getWednesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineWen.observe(
                        viewLifecycleOwner, { routineTasks ->
                    if(routineTasks != null){
                        this.routineTasks = routineTasks
                        getAllWorkTime()
                        getPomodoros()
                    }
                }
                )

                taskViewModel.oneTimeWen("work").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if(oneTimeTasks != null){
                        this.oneTimeTasks = oneTimeTasks
                        initTasks()
                    }
                })
            }
            "thursday" -> {
                val time = LocalTime.parse(mySharePreferences.getThursdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                dayNumber = 3
                beginTime = LocalTime.parse(mySharePreferences.getThursdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineThu.observe(
                        viewLifecycleOwner, { routineTasks ->
                    if(routineTasks != null){
                        this.routineTasks = routineTasks
                        getAllWorkTime()
                        getPomodoros()
                    }
                })

                taskViewModel.oneTimeThu("work").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if(oneTimeTasks != null){
                        this.oneTimeTasks = oneTimeTasks
                        initTasks()
                    }
                })
            }
            "friday" -> {
                val time = LocalTime.parse(mySharePreferences.getFridayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                dayNumber = 4
                beginTime = LocalTime.parse(mySharePreferences.getFridayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineFri.observe(
                        viewLifecycleOwner, { routineTasks ->
                    if(routineTasks != null){
                        this.routineTasks = routineTasks
                        getAllWorkTime()
                        getPomodoros()
                    }
                })

                taskViewModel.oneTimeFri("work").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if(oneTimeTasks != null){
                        this.oneTimeTasks = oneTimeTasks
                        initTasks()
                    }
                })
            }
            "saturday" -> {
                val time = LocalTime.parse(mySharePreferences.getSaturdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                dayNumber = 5
                beginTime = LocalTime.parse(mySharePreferences.getSaturdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineSat.observe(
                        viewLifecycleOwner, { routineTasks ->
                    if(routineTasks != null){
                        this.routineTasks = routineTasks
                        getAllWorkTime()
                        getPomodoros()
                    }
                })

                taskViewModel.oneTimeSat("work").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if(oneTimeTasks != null){
                        this.oneTimeTasks = oneTimeTasks
                        initTasks()
                    }
                })
            }
            "sunday" -> {
                val time = LocalTime.parse(mySharePreferences.getSundayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour*60+time.minute
                dayNumber = 6
                beginTime = LocalTime.parse(mySharePreferences.getSundayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.routineSun.observe(
                        viewLifecycleOwner, { routineTasks ->
                    if(routineTasks != null){
                        this.routineTasks = routineTasks
                        getAllWorkTime()
                        getPomodoros()
                    }
                })

                taskViewModel.oneTimeSun("work").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if(oneTimeTasks != null){
                        this.oneTimeTasks = oneTimeTasks
                        initTasks()
                    }
                })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllWorkTime(){
//        val currentTime = Calendar.getInstance().time
        Log.d("fixed", fixedTasks?.size.toString())
        Log.d("routine", routineTasks?.size.toString())
        fixedTasks?.forEach {
            val timeBegin = LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val timeEnd = LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if(it.category == "work"){
                workTime -= if(timeEnd.hour < timeBegin.hour){
                    ((24 - timeBegin.hour)*60 + (60 - timeEnd.minute)) + (timeEnd.hour*60 + timeEnd.minute)
                } else (timeEnd.hour*60 + timeEnd.minute) - (timeBegin.hour*60 + timeBegin.minute)
            }
        }

        routineTasks?.forEach {
            val timeBegin = LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val timeEnd = LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if(it.category == "work"){
                workTime -= if(timeEnd.hour < timeBegin.hour){
                    ((24 - timeBegin.hour)*60 + (60 - timeEnd.minute)) + (timeEnd.hour*60 + timeEnd.minute)
                } else (timeEnd.hour*60 + timeEnd.minute) - (timeBegin.hour*60 + timeBegin.minute)
            }
        }
        Log.d("second_work_time", workTime.toString())
    }

    private fun getPomodoroCount(): Int{
        return workTime/mySharePreferences.getPomodoroWork()!!
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPomodoros(){
        val tasks: MutableList<TasksForPlan> = mutableListOf()
        var work = 0
        var time: LocalTime?
        val sleep = LocalTime.parse(mySharePreferences.getSleep(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        //tasks

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

        fixedTasks?.forEach{
            tasks.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null,  it
            ))
        }

        routineTasks?.forEach{
            tasks.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null, it
            ))
        }

        Log.d("tasks_size", tasks.size.toString())

        tasks.sortBy { it.begin }

        tasks.forEach {
            Log.d("tasks", "${it.begin}/${it.end}")
        }

        //intervals

        for(i in 0 until tasks.size){
            if(beginTime < tasks[i].begin){
                if(work < workTime){
                    time = tasks[i].begin.minus(tasks[i-1].end.hour.toLong(), ChronoUnit.HOURS).minus(tasks[i-1].end.minute.toLong(), ChronoUnit.MINUTES)
                    intervals.add(TasksForPlan(tasks[i-1].end, tasks[i].begin, (time.hour*60 + time.minute), null))
                    work += time.hour*60 + time.minute
                } else break
            }
        }

        if(work < workTime){
            val rest = workTime - work
            time = tasks.last().end.plus((rest/60).toLong(), ChronoUnit.HOURS).plus((rest%60).toLong(), ChronoUnit.MINUTES)
            intervals.add(TasksForPlan(tasks.last().end, time, rest, null))
        }

        if(sleep.hour < intervals.last().end.hour){
            while(((intervals.last().end.hour*60 + intervals.last().end.minute) - (sleep.hour*60 + sleep.minute)) < 60){
                intervals.removeLast()
            }
        } else {
            while(((sleep.hour*60 + sleep.minute) - (intervals.last().end.hour*60 + intervals.last().end.minute)) < 60){
                intervals.removeLast()
            }
        }

//        sleep = sleep.minus(3, ChronoUnit.HOURS)
//        Log.d("test", LocalTime.of(26, 0).toString())

        intervals.forEach {
            Log.d("intervals", "${it.begin}/${it.end}")
            Log.d("intervals", it.time.toString())
        }

        //pomodoros

        intervals.forEach {
            val pomodoro = mySharePreferences.getPomodoroWork()
            val breakTime = mySharePreferences.getPomodoroBreak()
            val bigBreakTime = mySharePreferences.getPomodoroBigBreak()
            val count = it.time?.div(pomodoro)
            var minutes = it.time
            for (i in 0 until count!!){
                if (minutes != null) {
                    if(minutes > 25){
                        if(i == 0){
                            pomodoros.add(TasksForPlan(it.begin, it.begin.plusMinutes(pomodoro.toLong()), null, null))
                            minutes -= pomodoro
                        } else {
                            if(count > 4 && i % 4 == 0 && mySharePreferences.getPomodoroBigBreakF()){
                                pomodoros.add(TasksForPlan(pomodoros.last().end.plusMinutes(bigBreakTime.toLong()), pomodoros.last().end.plusMinutes(pomodoro.toLong() + bigBreakTime), null, null))
                                minutes -= bigBreakTime + pomodoro
                            } else {
                                pomodoros.add(TasksForPlan(pomodoros.last().end.plusMinutes(breakTime.toLong()), pomodoros.last().end.plusMinutes(pomodoro.toLong() + breakTime), null, null))
                                minutes -= pomodoro + breakTime
                            }
                        }
                    } else {
                        pomodoros.last().end = it.end
                    }
                }
            }
        }

        pomodoros.forEach {
            Log.d("pomodoros", "${it.begin}/${it.end}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTasks()
    {
        Log.d("one_time", oneTimeTasks?.size.toString())

        oneTimeTasks?.forEach {
            var daysBeforeDeadline = ChronoUnit.DAYS.between(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    LocalDate.parse(it.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            var workDays = 0
            val pomodoros = it.duration?.div(mySharePreferences.getPomodoroWork())
            var pomodorosInDay = 0
            var flag = true
            var n: Int
            Log.d("days", daysBeforeDeadline.toString())

            while (daysBeforeDeadline > 0){
                if(flag){
                    n = dayNumber
                } else {
                    n = 0
                    flag = false
                }
                for(i in n..6){
                    when(i){
                        0 -> {
                            if(it.monday!! && daysBeforeDeadline>0){
                                workDays++
                            }
                            daysBeforeDeadline--
                        }
                        1 -> {
                            if(it.tuesday!! && daysBeforeDeadline>0){
                                workDays++
                            }
                            daysBeforeDeadline--
                        }
                        2 -> {
                            if(it.wednesday!! && daysBeforeDeadline>0){
                                workDays++
                            }
                            daysBeforeDeadline--
                        }
                        3 -> {
                            if(it.thursday!! && daysBeforeDeadline>0){
                                workDays++
                            }
                            daysBeforeDeadline--
                        }
                        4 -> {
                            if(it.friday!! && daysBeforeDeadline>0){
                                workDays++
                            }
                            daysBeforeDeadline--
                        }
                        5 -> {
                            if(it.saturday!! && daysBeforeDeadline>0){
                                workDays++
                            }
                            daysBeforeDeadline--
                        }
                        6 -> {
                            if(it.sunday!! && daysBeforeDeadline>0){
                                workDays++
                            }
                            daysBeforeDeadline--
                        }
                    }
                }
            }
            Log.d("daysForWork", workDays.toString())
            if (pomodoros != null) {
                pomodorosInDay = pomodoros/workDays + 1
            }
            Log.d("pomodoros", pomodoros.toString())
            Log.d("pomodorosInDay", pomodorosInDay.toString())
            for(i in 0 until pomodorosInDay){
                tasks.add(it)
            }
        }
        if(pomodoros.size > tasks.size){
            initTasks()
        }
//        mySharePreferences.setPlanForDay(false)
        unitAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun unitAll(){
        var i = 0
        if(pomodoros.size <= tasks.size){
            if(!mySharePreferences.getPlanForDay()){
                pomodoros.forEach {
                    it.task = tasks[i]
                    i++
                }
                fixedTasks?.forEach{
                    pomodoros.add(TasksForPlan(
                            LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                            LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                            null,  it
                    ))
                }
                routineTasks?.forEach{
                    pomodoros.add(TasksForPlan(
                            LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                            LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                            null, it
                    ))
                }
                pomodoros.sortBy { it.begin }
                pomodoros.forEach {
                    Log.d("finish", "${it.begin}-${it.end}: ${it.task?.title}")
                }
                mySharePreferences.setPlanForDay(true)
            }
        }
    }
}