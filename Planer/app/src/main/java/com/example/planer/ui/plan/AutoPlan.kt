package com.example.planer.ui.plan

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.PlanRecyclerAdapter
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.MySharePreferences
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class AutoPlan(private val date: String,
               private val weekDay: String
): Fragment(), PlanRecyclerAdapter.OnItemClickListener, PlanRecyclerAdapter.OnItemLongClickListener
{
    private lateinit var mySharePreferences: MySharePreferences
    private val taskViewModel: TaskViewModel by viewModels()

    private var fixedTasks: MutableList<Task>? = mutableListOf()
    private var routineTasks: MutableList<Task>? = mutableListOf()
    private var allTasksTest: List<Task>? = listOf()
    private var oneTimeTasks: MutableList<Task>? = mutableListOf()
    private var rest: MutableList<Task>? = mutableListOf()

    private var workTime: Int = 0
    private var workTimeFirst: Int = 0
    private var workTimePast: Int = 0
    private var dayNumber: Int = 0
    private lateinit var beginTime: LocalTime
    private var beginTimeReserve: LocalTime? = null
    private var countOfFullTasks = 0

    private var intervals: MutableList<TasksForPlan>? = mutableListOf()
    private var pomodoros: MutableList<TasksForPlan>? = mutableListOf()
    private var pomodorosSmall: MutableList<TasksForPlan>? = mutableListOf()
    private var tasks: MutableList<Task?>? = mutableListOf()

    private var adapter: PlanRecyclerAdapter? = null
    private lateinit var list: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_task_recycler, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!
        view.button_add_item.visibility = View.INVISIBLE

        adapter = this.context?.let { PlanRecyclerAdapter(it, pomodoros, this, this) }
        list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        minusTime()

        if(date != LocalDate.now().toString()){
            this.workTimePast = 0
        } else {
            this.workTimePast = mySharePreferences.getWorkTimePast()
        }

        if(mySharePreferences.getAllInfo()){
            taskViewModel.allTasks.observe(
                    viewLifecycleOwner, { allTasks ->
                if (allTasks != null) {
                    this.allTasksTest = allTasks
                    initWeekDay()
                }
            }
            )
            view.progressBar.visibility = View.VISIBLE
        } else {
            view?.textView?.visibility = View.VISIBLE
            view?.imageView?.visibility = View.VISIBLE
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(): this(LocalDate.now().toString(), LocalDate.now().dayOfWeek.toString())

    @RequiresApi(Build.VERSION_CODES.O)
    private fun minusTime()
    {
        val pomodoros: MutableList<TasksForPlan>? = mySharePreferences.getPlan()

        if(LocalDate.now().toString() != mySharePreferences.getToday()){
            mySharePreferences.setTaskTransfer(0)
            mySharePreferences.setWorkTimePast(0)
            mySharePreferences.getFirstTasksNext()?.let { mySharePreferences.setFirstTasksToday(it) }
            mySharePreferences.setWorkEnd(null)
        }

        pomodoros?.sortBy { it.begin }
        pomodoros?.removeIf { it.end < LocalTime.now() && it.begin != it.end && date == LocalDate.now().toString()}
        mySharePreferences.setPlan(pomodoros)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initWeekDay(){
        oneTimeTasks?.clear()
        rest?.clear()
        fixedTasks?.clear()
        routineTasks?.clear()

        allTasksTest?.let { fixedTasks?.addAll(it) }
        fixedTasks?.removeIf { it.date != date }

        when(weekDay){
            "MONDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getMondayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                beginTime = LocalTime.parse(mySharePreferences.getMondayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                allTasksTest?.let { routineTasks?.addAll(it) }
                routineTasks?.removeIf { it.monday == false || it.type != "routine" || it.title == ""}

                allTasksTest?.let { oneTimeTasks?.addAll(it) }
                oneTimeTasks?.removeIf { it.monday == false || it.type != "one_time" || it.category != "work" || it.title == ""}

                allTasksTest?.let { rest?.addAll(it) }
                rest?.removeIf { it.monday == false || it.type != "one_time" || it.category == "work" || it.title == ""}
            }
            "TUESDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getTuesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 1
                beginTime = LocalTime.parse(mySharePreferences.getTuesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                allTasksTest?.let { routineTasks?.addAll(it) }
                routineTasks?.removeIf { it.tuesday == false || it.type != "routine" || it.title == ""}

                allTasksTest?.let { oneTimeTasks?.addAll(it) }
                oneTimeTasks?.removeIf { it.tuesday == false || it.type != "one_time" || it.category != "work" || it.title == ""}

                allTasksTest?.let { rest?.addAll(it) }
                rest?.removeIf { it.tuesday == false || it.type != "one_time" || it.category == "work" || it.title == ""}
            }
            "WEDNESDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getWednesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 2
                beginTime = LocalTime.parse(mySharePreferences.getWednesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                allTasksTest?.let { routineTasks?.addAll(it) }
                routineTasks?.removeIf { it.wednesday == false || it.type != "routine" || it.title == "" }

                allTasksTest?.let { oneTimeTasks?.addAll(it) }
                oneTimeTasks?.removeIf { it.wednesday == false || it.type != "one_time" || it.category != "work" || it.title == ""}

                allTasksTest?.let { rest?.addAll(it) }
                rest?.removeIf { it.wednesday == false || it.type != "one_time" || it.category == "work" || it.title == ""}
            }
            "THURSDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getThursdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 3
                beginTime = LocalTime.parse(mySharePreferences.getThursdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                allTasksTest?.let { routineTasks?.addAll(it) }
                routineTasks?.removeIf { it.thursday == false || it.type != "routine" || it.title == "" }

                allTasksTest?.let { oneTimeTasks?.addAll(it) }
                oneTimeTasks?.removeIf { it.thursday == false || it.type != "one_time" || it.category != "work" || it.title == ""}

                allTasksTest?.let { rest?.addAll(it) }
                rest?.removeIf { it.thursday == false || it.type != "one_time" || it.category == "work" || it.title == ""}
            }
            "FRIDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getFridayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 4
                beginTime = LocalTime.parse(mySharePreferences.getFridayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                allTasksTest?.let { routineTasks?.addAll(it) }
                routineTasks?.removeIf { it.friday == false || it.type != "routine"  || it.title == ""}

                allTasksTest?.let { oneTimeTasks?.addAll(it) }
                oneTimeTasks?.removeIf { it.friday == false || it.type != "one_time" || it.category != "work" || it.title == ""}

                allTasksTest?.let { rest?.addAll(it) }
                rest?.removeIf { it.friday == false || it.type != "one_time" || it.category == "work" || it.title == ""}
            }
            "SATURDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getSaturdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 5
                beginTime = LocalTime.parse(mySharePreferences.getSaturdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                allTasksTest?.let { routineTasks?.addAll(it) }
                routineTasks?.removeIf { it.saturday == false || it.type != "routine"  || it.title == ""}

                allTasksTest?.let { oneTimeTasks?.addAll(it) }
                oneTimeTasks?.removeIf { it.saturday == false || it.type != "one_time" || it.category != "work" || it.title == ""}

                allTasksTest?.let { rest?.addAll(it) }
                rest?.removeIf { it.saturday == false || it.type != "one_time" || it.category == "work" || it.title == ""}
            }
            "SUNDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getSundayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 6
                beginTime = LocalTime.parse(mySharePreferences.getSundayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

                allTasksTest?.let { routineTasks?.addAll(it) }
                routineTasks?.removeIf { it.sunday == false || it.type != "routine" || it.title == ""}

                allTasksTest?.let { oneTimeTasks?.addAll(it) }
                oneTimeTasks?.removeIf { it.sunday == false || it.type != "one_time" || it.category != "work" || it.title == ""}

                allTasksTest?.let { rest?.addAll(it) }
                rest?.removeIf { it.sunday == false || it.type != "one_time" || it.category == "work" || it.title == ""}
            }
        }
        if(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now()){
            beginTime = beginTime.plusMinutes(mySharePreferences.getTaskTransfer().toLong())
        }
        beginTimeReserve = beginTime
        workTimeFirst = workTime
        workTime -= this.workTimePast

        getAllWorkTime()
        getPomodoros()
        view?.progressBar?.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllWorkTime(){
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
        getPomodoros()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPomodoros(){
        val tasks: MutableList<TasksForPlan> = mutableListOf()
        var work = 0
        var time: LocalTime?
        val sleep = LocalTime.parse(mySharePreferences.getSleep(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        if(LocalTime.now() > beginTimeReserve && date == LocalDate.now().toString()){
            if(mySharePreferences.getPlan().isNullOrEmpty() || (mySharePreferences.getPlan()?.last()?.task?.type == "one_time" && mySharePreferences.getPlan()?.last()?.task?.category != "work")){
                beginTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute).plusMinutes(10)
                val pastTime = beginTimeReserve?.hour?.toLong()?.let { beginTimeReserve?.minute?.toLong()?.let { it1 -> LocalTime.now().minusHours(it).minusMinutes(it1) } }
                mySharePreferences.setWorkTimePast(((pastTime?.hour)?.times(60)?: 0).plus((pastTime?.minute ?: 0)))
            }
            else {
                for (i in 0 until (mySharePreferences.getPlan()?.size ?: 0)){
                    if(mySharePreferences.getPlan()?.get(i)?.task?.type == "one_time"){
                        beginTime = mySharePreferences.getPlan()?.get(i)!!.begin
                        break
                    }
                }
            }
            val temp = beginTime.minusHours(LocalTime.now().hour.toLong()).minusMinutes(LocalTime.now().minute.toLong())
            if(beginTime > LocalTime.now() && (temp.hour * 60 + temp.minute) > mySharePreferences.getPomodoroWork()){
                beginTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute).plusMinutes(5)
            }
        }

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
                    null, it
            ))
        }

        routineTasks?.forEach{
            tasks.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null, it
            ))
        }

        tasks.sortBy { it.begin }

        var i = 1
        while(i < tasks.size){
            if(i > 0){
                if(tasks[i].begin == tasks[i-1].begin && i > 0){
                    if(tasks[i].end > tasks[i-1].end){
                        tasks.removeAt(i-1)
                    } else tasks.removeAt(i)
                    i--
                }
            }

            if(i > 0){
                if(tasks[i].begin == tasks[i-1].end && i > 0){
                    tasks[i-1].end = tasks[i].end
                    tasks.removeAt(i)
                    i--
                }
            }

            if(i > 0){
                if(tasks[i].end == tasks[i-1].end){
                    tasks.removeAt(i)
                    i--
                }
            }

            if(tasks[i].begin < tasks[i-1].end){
                tasks[i].begin = tasks[i-1].end
            }

            i++
        }

        tasks.removeIf { it.end < beginTime }

        //intervals
        intervals?.clear()

        var flag = true
        for(i in 0 until tasks.size){
            if(i == 0 && tasks[i].begin > beginTime){
                time = tasks[i].begin.minus(beginTime.hour.toLong(), ChronoUnit.HOURS).minus(beginTime.minute.toLong(), ChronoUnit.MINUTES)
                intervals?.add(TasksForPlan(beginTime, tasks[i].begin, (time.hour * 60 + time.minute), null))
                work += time.hour*60 + time.minute
                flag = true
            } else if(i == 0 && tasks[i].end > beginTime && tasks.size > 1){
                time = tasks[i+1].begin.minus(tasks[i].end.hour.toLong(), ChronoUnit.HOURS).minus(tasks[i].end.minute.toLong(), ChronoUnit.MINUTES)
                intervals?.add(TasksForPlan(tasks[i].end, tasks[i+1].begin, (time.hour * 60 + time.minute), null))
                work += time.hour*60 + time.minute
                flag = false
            }
            if(beginTime < tasks[i].begin && i > 1 && !flag){
                if(work < workTime){
                    time = tasks[i].begin.minus(tasks[i - 1].end.hour.toLong(), ChronoUnit.HOURS).minus(tasks[i - 1].end.minute.toLong(), ChronoUnit.MINUTES)
                    intervals?.add(TasksForPlan(tasks[i - 1].end, tasks[i].begin, (time.hour * 60 + time.minute), null))
                    work += time.hour*60 + time.minute
                } else break
            }
            if(beginTime < tasks[i].begin && i > 0 && flag){
                if(work < workTime){
                    time = tasks[i].begin.minus(tasks[i - 1].end.hour.toLong(), ChronoUnit.HOURS).minus(tasks[i - 1].end.minute.toLong(), ChronoUnit.MINUTES)
                    intervals?.add(TasksForPlan(tasks[i - 1].end, tasks[i].begin, (time.hour * 60 + time.minute), null))
                    work += time.hour*60 + time.minute
                } else break
            }
        }

        if(!intervals.isNullOrEmpty() && intervals?.first()?.begin!! < beginTime){
            intervals?.first()?.begin = beginTime
        }

        work = 0

        intervals?.forEach {
            val time = it.end.minusHours(it.begin.hour.toLong()).minusMinutes(it.begin.minute.toLong())
            it.time = time.minute + time.hour*60
            work += it.time!!
        }

        if(intervals != null && work > workTime){
            if(intervals!!.size > 0){
                if(intervals?.last()?.time!! > work-workTime){
                    intervals!!.last().end = intervals!!.last().end.minusMinutes((work-workTime).toLong())
                    intervals!!.last().time = intervals!!.last().time!! - (work-workTime)
                } else {
                    while (work > workTime && intervals!!.size > 0){
                        work -= intervals!!.last().time!!
                        intervals!!.removeLast()
                    }
                }
            }
        }

        if(work < workTime && intervals != null && tasks.isNotEmpty()){
            val rest = workTime - work
            intervals!!.add(TasksForPlan(tasks.last().end, tasks.last().end.plusMinutes(rest.toLong()), rest, null))
        }

        if(!intervals.isNullOrEmpty()){
            intervals!!.removeIf { ((it.end > sleep.minusHours(1) && sleep > LocalTime.of(12, 0) && it.end > LocalTime.of(12, 0))
                    || (intervals!!.last().end > sleep.minusHours(1) && sleep < LocalTime.of(12, 0) && it.end < LocalTime.of(12, 0)))
                    && it.task?.type == "one_time"}
        }

        if(intervals != null){
            for(i in 0 until intervals?.size!!){
                if(intervals!![i].begin != beginTimeReserve && i != 0){
                    intervals!![i].begin = intervals!![i].begin.plusMinutes(5)
                    intervals!![i].time = intervals!![i].time?.minus(5)
                }
            }
        }

        //pomodoros
        pomodoros?.clear()

        intervals?.forEach {
            val pomodoro = mySharePreferences.getPomodoroWork()
            val breakTime = mySharePreferences.getPomodoroBreak()
            val bigBreakTime = mySharePreferences.getPomodoroBigBreak()
            val count = it.time?.div(pomodoro+breakTime)
            var minutes = it.time
            for (i in 0 until count!!){
                if (minutes != null) {
                    if(minutes >= mySharePreferences.getPomodoroWork()){
                        minutes -= if(i == 0){
                            pomodoros?.add(TasksForPlan(it.begin, it.begin.plusMinutes(pomodoro.toLong()), null, null))
                            pomodoro
                        } else {
                            if(count > 4 && i % 4 == 0 && mySharePreferences.getPomodoroBigBreakF()){
                                pomodoros?.add(TasksForPlan(pomodoros!!.last().end.plusMinutes(bigBreakTime.toLong()), pomodoros!!.last().end.plusMinutes(pomodoro.toLong() + bigBreakTime), null, null))
                                bigBreakTime + pomodoro
                            } else {
                                pomodoros?.last()?.end?.plusMinutes(breakTime.toLong())?.let { it1 -> TasksForPlan(it1, pomodoros!!.last().end.plusMinutes(pomodoro.toLong() + breakTime), null, null) }?.let { it2 -> pomodoros?.add(it2) }
                                pomodoro + breakTime
                            }
                        }
                    } else {
                        if(minutes < 0){
                            pomodoros?.removeLast()
                        }
                    }
                }
            }
        }

        countOfFullTasks = 0
        this.tasks?.clear()
        initTasks()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTasks() {
        oneTimeTasks?.forEach {
            val current = LocalDate.now()
            val date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            var duration: Int? = it.duration
            var daysBeforeDeadline = ChronoUnit.DAYS.between(current,
                    LocalDate.parse(it.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")))

            var pomodoros: Int?
            var pomodorosInDay = 0
            var workDays: Int

            if(current != date){
                val daysBetweenDates = ChronoUnit.DAYS.between(current, date)

                val leftWorkDays = calculateWorkDays(daysBetweenDates, it)
                workDays = calculateWorkDays(daysBeforeDeadline, it)

                pomodoros = if(it.duration?.rem(mySharePreferences.getPomodoroWork()) == 0)
                    it.duration?.div(mySharePreferences.getPomodoroWork())
                else it.duration?.div(mySharePreferences.getPomodoroWork())?.plus(1)

                if (pomodoros != null && workDays != 0) {
                    pomodorosInDay = when {
                        workDays > pomodoros -> 1
                        pomodorosInDay%workDays == 0 -> pomodoros/workDays
                        else -> pomodoros/workDays + 1
                    }
                }

                duration = if(workDays == 0){
                    0
                } else duration?.minus(leftWorkDays * mySharePreferences.getPomodoroWork() * pomodorosInDay)
            }

            //сегодняшний день - продолжительность не изменяется
            if(duration != null && duration > 0){
                daysBeforeDeadline = ChronoUnit.DAYS.between(date,
                        LocalDate.parse(it.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")))

                if(duration < mySharePreferences.getPomodoroWork()){
                    duration = mySharePreferences.getPomodoroWork()
                }

                pomodoros = if(duration.rem(mySharePreferences.getPomodoroWork()) == 0)
                    duration.div(mySharePreferences.getPomodoroWork())
                else duration.div(mySharePreferences.getPomodoroWork()).plus(1)

                workDays = calculateWorkDays(daysBeforeDeadline, it)

                if(workDays != 0){
                    pomodorosInDay = when {
                        workDays > pomodoros -> 1
                        pomodoros%workDays == 0 -> pomodoros/workDays
                        else -> pomodoros/workDays + 1
                    }
                    for(i in 0 until pomodorosInDay){
                        val count = tasks?.count { task -> task == it }
                        if(count != null && count < pomodoros){
                            tasks?.add(it)
                        } else {
                            countOfFullTasks++
                            break
                        }
                    }
                } else countOfFullTasks++
            } else countOfFullTasks++
        }

        if(pomodoros != null && tasks != null){
            if(oneTimeTasks.isNullOrEmpty()){
                pomodoros?.removeIf { task -> task.task == null }
            } else if (pomodoros?.size!! > tasks?.size!! && countOfFullTasks < oneTimeTasks!!.size) {
                countOfFullTasks = 0
                initTasks()
            }
        }

        unitAll()
    }

    private fun calculateWorkDays(days: Long, task: Task) :Int{
        var flag = true
        var n: Int
        var daysBeforeDeadline = days
        var workDays = 0

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
                        if (task.monday!! && daysBeforeDeadline > 0) {
                            workDays++
                        }
                        daysBeforeDeadline--
                    }
                    1 -> {
                        if (task.tuesday!! && daysBeforeDeadline > 0) {
                            workDays++
                        }
                        daysBeforeDeadline--
                    }
                    2 -> {
                        if (task.wednesday!! && daysBeforeDeadline > 0) {
                            workDays++
                        }
                        daysBeforeDeadline--
                    }
                    3 -> {
                        if (task.thursday!! && daysBeforeDeadline > 0) {
                            workDays++
                        }
                        daysBeforeDeadline--
                    }
                    4 -> {
                        if (task.friday!! && daysBeforeDeadline > 0) {
                            workDays++
                        }
                        daysBeforeDeadline--
                    }
                    5 -> {
                        if (task.saturday!! && daysBeforeDeadline > 0) {
                            workDays++
                        }
                        daysBeforeDeadline--
                    }
                    6 -> {
                        if (task.sunday!! && daysBeforeDeadline > 0) {
                            workDays++
                        }
                        daysBeforeDeadline--
                    }
                }
            }
        }
        return workDays
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun unitAll()
    {
        val firstTasks: MutableList<TasksForPlan>? = mutableListOf()

        if(pomodoros != null && tasks != null) {
            tasks!!.removeIf { tasks -> tasks == null }
            val sleepTime = LocalTime.parse(mySharePreferences.getSleep(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            if(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now())
            {
                mySharePreferences.setToday(LocalDate.now().toString())

                pomodorosSmall?.clear()
                pomodorosSmall = byWorkPeak()

                //задача после 00
                pomodorosSmall?.forEach {
                    if(it.begin < beginTimeReserve && it.task?.type == "one_time"){
                        firstTasks?.add(it)
                    }
                }
                pomodorosSmall?.removeIf { task -> task.begin < beginTimeReserve && task.task?.type == "one_time"}

                mySharePreferences.getFirstTasksToday()?.forEach{
                    if(it.end > LocalTime.now()){
                        pomodorosSmall?.add(it)
                    }
                }

                pomodorosSmall?.removeIf {it.end < LocalTime.now() && it.task?.type != "one_time"}

                mySharePreferences.setPlan(pomodorosSmall)
                mySharePreferences.setPlanForDay(true)

                firstTasks?.let { mySharePreferences.setFirstTasksNext(it) }

                pomodorosSmall?.clear()

                pomodorosSmall = mySharePreferences.getPlan()

                if(pomodorosSmall != null){
                    if(workTimeFirst <= this.workTimePast
                            || pomodorosSmall!!.isEmpty()
                            || (oneTimeTasks?.isEmpty() != false && routineTasks?.isEmpty() != false && fixedTasks?.isEmpty() != false)
                            || LocalTime.now() >= sleepTime.minusHours(1) && (sleepTime > LocalTime.of(12, 0) || sleepTime == LocalTime.of(0, 0))
                            || LocalTime.now() <= sleepTime.minusHours(1) && sleepTime < LocalTime.of(12, 0) && sleepTime != LocalTime.of(0, 0))
                    {
                        pomodorosSmall!!.clear()
                        rest?.forEach {
                            pomodorosSmall!!.add(TasksForPlan(LocalTime.now(), LocalTime.now(), null, it))
                        }
                    }
                }
                mySharePreferences.setPlan(pomodorosSmall)
                showTasks(pomodorosSmall)
            } else {
                val days = ChronoUnit.DAYS.between(LocalDate.now(),
                        LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                val i: Long = 1
                pomodorosSmall?.clear()
                pomodorosSmall = byWorkPeak()
                if(days == i){
                    mySharePreferences.getFirstTasksNext()?.let { pomodorosSmall?.addAll(it) }
                }
                if(pomodorosSmall != null){
                    if(pomodorosSmall!!.isEmpty()
                            || (oneTimeTasks?.isEmpty() != false && routineTasks?.isEmpty() != false && fixedTasks?.isEmpty() != false)) {
                        pomodorosSmall!!.clear()
                        rest?.forEach {
                            pomodorosSmall!!.add(TasksForPlan(LocalTime.now(), LocalTime.now(), null, it))
                        }
                    }
                }
                showTasks(pomodorosSmall)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun byWorkPeak(): MutableList<TasksForPlan>? {
        var i = 0
        val pomodorsSmall: MutableList<TasksForPlan>? = mutableListOf()
        val tasksSmall: MutableList<Task?>? = mutableListOf()

        pomodorsSmall?.clear()
        tasksSmall?.clear()

        pomodoros?.let { pomodorsSmall?.addAll(it) }
        tasks?.let { tasksSmall?.addAll(it) }

        if(tasksSmall?.isNotEmpty()!!)
        {
            while (tasksSmall.size > pomodoros?.size!!){
                tasksSmall.removeLast()
            }
            tasksSmall.sortByDescending { it?.complexity }

            var k = 0
            pomodorsSmall?.forEach {
                if(it.begin < LocalTime.parse(mySharePreferences.getPeakBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))){
                    k++
                }
            }

            //расставление по пику
            pomodorsSmall?.forEach {
                if(it.begin >= LocalTime.parse(mySharePreferences.getPeakBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        && it.begin < LocalTime.parse(mySharePreferences.getPeakEnd(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        && tasksSmall.isNotEmpty() && tasksSmall.size>k){
                    it.task = tasksSmall.first()
                    tasksSmall.removeFirst()
                }
            }

            //расставление оставшихся интервалов
            if(tasksSmall.isNotEmpty()){
                pomodorsSmall!!.forEach {
                    if(i < tasksSmall.size && it.task == null){
                        it.task = tasksSmall[i]
                        i++
                    }
                }
            }
        }

        while (pomodorsSmall?.size!! > tasks?.size!!){
            pomodorsSmall.removeLast()
        }

        fixedTasks?.forEach{
            pomodorsSmall.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null, it
            ))
        }

        routineTasks?.forEach{
            pomodorsSmall.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null, it
            ))
        }

        pomodorsSmall.removeIf { task -> task.task == null }
        return pomodorsSmall
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTasks(pomodoros: MutableList<TasksForPlan>?)
    {
        if (pomodoros != null && pomodoros.isNotEmpty()) {
            if(pomodoros.first().task?.type == "one_time" && pomodoros.first().task?.category != "work"){
                pomodoros.sortByDescending { it.task?.priority }
            } else pomodoros.sortBy { it.begin }

            pomodoros.removeIf { it.task?.type != "one_time" && it.end < LocalTime.now() && it.end != it.begin && date == LocalDate.now().toString()}

            if(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now()) {
                mySharePreferences.setPlan(pomodoros)
            }

            adapter?.setTasks(pomodoros)
            list.adapter = adapter
        }

        if(pomodoros.isNullOrEmpty()){
            view?.textView?.visibility = View.VISIBLE
            view?.imageView?.visibility = View.VISIBLE
        } else {
            view?.textView?.visibility = View.INVISIBLE
            view?.imageView?.visibility = View.INVISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int) {
        pomodorosSmall?.sortBy { it.begin }
        view?.let { openTask(it, pomodorosSmall?.get(position)?.task) }
    }

    private fun openTask(view: View, task: Task?)
    {
        val bundle = Bundle().apply { putString("type", task?.type) }
                .apply { putString("category", task?.category) }
                .apply { putSerializable("task", task) }
                .apply { putBoolean("back", true) }
        when(task?.type){
            "one_time" -> {
                if(task.category == "work")
                    Navigation.findNavController(view).navigate(R.id.add_one_time_work_task, bundle)
                else Navigation.findNavController(view).navigate(R.id.add_one_time_other_task, bundle)
            }
            "fixed" -> {
                Navigation.findNavController(view).navigate(R.id.add_fixed_task, bundle)
            }
            "routine" -> {
                Navigation.findNavController(view).navigate(R.id.add_routine_task, bundle)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemLongClicked(position: Int): Boolean {
        if(position == 0 && !(mySharePreferences.getPlan()?.first()?.task?.type == "one_time"
                        && mySharePreferences.getPlan()?.first()!!.task!!.category != "work")
                && date == LocalDate.now().toString()){
            showMinutesDialog()
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMinutesDialog(){
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builderSingle.setIcon(R.drawable.plan_color)
        builderSingle.setTitle("Перенос задачи (мин.)")

        val arrayAdapter = this.context?.let {
            ArrayAdapter<String>(
                    it,
                    android.R.layout.select_dialog_singlechoice)
        }

        arrayAdapter?.add("5")
        arrayAdapter?.add("10")
        arrayAdapter?.add("30")

        builderSingle.setNegativeButton("Отмена"
        ) { dialog, _ -> dialog.dismiss() }

        builderSingle.setAdapter(arrayAdapter
        ) { dialog, which ->
            val pomodoros = mySharePreferences.getPlan()
            if (pomodoros?.get(0)?.task?.type == "one_time" && pomodoros[0].begin == beginTimeReserve) {
                mySharePreferences.setTaskTransfer(mySharePreferences.getTaskTransfer() + (arrayAdapter?.getItem(which)?.toInt()
                        ?: 0))
            } else {
                mySharePreferences.setTaskTransfer(0)
                if (pomodoros?.get(0)?.task?.type == "one_time") {
                    pomodoros[0].begin = arrayAdapter?.getItem(which)?.toLong()?.let { pomodoros[0].begin.plusMinutes(it) }!!
                    mySharePreferences.setPlan(pomodoros)
                } else {
                    val task = pomodoros?.get(0)?.task
                    task?.begin = arrayAdapter?.getItem(which)?.toLong()?.let { LocalTime.parse(task?.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)).plusMinutes(it).toString() }
                    task?.let { taskViewModel.update(it) }
                }
            }
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.plan_frag, AutoPlan(this.date, this.weekDay))
                    ?.commit()

            view?.let { Navigation.findNavController(it).navigate(R.id.navigation_plan) }
            dialog.dismiss()
        }
        builderSingle.show()
    }
}