package com.example.planer.ui.plan

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.PlanRecyclerAdapter
import com.example.planer.database.MyDataBase
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.notifications.BootAlarmService
import com.example.planer.util.MySharePreferences
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class AutoPlan(private val date: String,
               private val weekDay: String
): Fragment(), PlanRecyclerAdapter.OnItemClickListener, PlanRecyclerAdapter.OnItemLongClickListener {
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
        Log.d("AutoPlan", "onCreate")
        val view = inflater.inflate(R.layout.fragment_task_recycler, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!
        view.button_add_item.visibility = View.INVISIBLE

//        mySharePreferences.setWorkEnd(null)
//        mySharePreferences.setPlan(null)

        adapter = this.context?.let { PlanRecyclerAdapter(it, pomodoros, this, this) }
        list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

//        mySharePreferences.setWorkTimePast(120)

        minusTime()

        if(date != LocalDate.now().toString()){
            this.workTimePast = 0
        } else {
            this.workTimePast = mySharePreferences.getWorkTimePast()
        }

        Log.d("workTimePast", workTimePast.toString())

        Log.d("AllInfo", mySharePreferences.getAllInfo().toString())

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

        Log.d("first_work_time", workTime.toString())
        Log.d("dateeee", date)
        Log.d("weekday", weekDay)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(): this(LocalDate.now().toString(), LocalDate.now().dayOfWeek.toString())

    @RequiresApi(Build.VERSION_CODES.O)
    private fun minusTime()
    {
        var pomodoros: MutableList<TasksForPlan>? = mutableListOf()
        pomodoros = mySharePreferences.getPlan()

        if(LocalDate.now().toString() != mySharePreferences.getToday()){
            mySharePreferences.setTaskTransfer(0)
            mySharePreferences.setWorkTimePast(0)
            mySharePreferences.getFirstTasksNext()?.let { mySharePreferences.setFirstTasksToday(it) }
            mySharePreferences.setWorkEnd(null)
        }

        pomodoros?.sortBy { it.begin }

        if(!pomodoros.isNullOrEmpty()){
            if(pomodoros.last().task?.type == "one_time" && pomodoros.last().task?.category != "work"){
                //no
            } else {
                mySharePreferences.setWorkEnd(pomodoros.last().end.toString())
                pomodoros.forEach {
                    Log.d("pomodorosFromAutoPlan", "${it.begin}-${it.end}: ${it.task?.title}")
                    if((it.end < LocalTime.now() && it.begin < LocalTime.now() && it.task?.type == "one_time") || LocalDate.now().toString() != mySharePreferences.getToday()){
                        Log.d("pomodorosFromAutoPlanMT", "${it.begin}-${it.end}: ${it.task?.title}")
                        val task = it.task
                        task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
                        mySharePreferences.setWorkTimePast(mySharePreferences.getWorkTimePast()+mySharePreferences.getPomodoroWork())
                        task?.let { it1 -> taskViewModel.update(it1) }
                    }
                }
            }
        } else mySharePreferences.getSleep()?.let { mySharePreferences.setWorkEnd(it) }

//        Log.d("WorkEnd", mySharePreferences.getWorkEnd().toString())

//        if(LocalTime.now() < beginTimeReserve){
//            Log.d("getWorkTimePast1", mySharePreferences.getWorkTimePast().toString())
//            mySharePreferences.setWorkEnd(null)
//        }

        if(LocalDate.now().toString() != mySharePreferences.getToday()){
            mySharePreferences.setWorkTimePast(0)
        }

        pomodoros?.removeIf { it.end < LocalTime.now() && it.begin != it.end && date == LocalDate.now().toString()}

        pomodoros?.forEach {
            Log.d("pomodorosFromAutoPlan2", "${it.begin}-${it.end}: ${it.task?.title}")
        }
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

                Log.d("Test1", allTasksTest?.size.toString())
                Log.d("Test2", fixedTasks?.size.toString())
                Log.d("Test3", routineTasks?.size.toString())
                Log.d("Test4", oneTimeTasks?.size.toString())
                Log.d("Test5", rest?.size.toString())
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
        Log.d("TaskTransfer", mySharePreferences.getTaskTransfer().toString())
        if(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now()){
            beginTime = beginTime.plusMinutes(mySharePreferences.getTaskTransfer().toLong())
        }
        beginTimeReserve = beginTime
        Log.d("WorkTime", mySharePreferences.getWorkTimePast().toString())
        Log.d("workTime", workTime.toString())
        workTimeFirst = workTime
        workTime -= this.workTimePast

        getAllWorkTime()
        getPomodoros()
        view?.progressBar?.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllWorkTime(){
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
        getPomodoros()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPomodoros(){
        val tasks: MutableList<TasksForPlan> = mutableListOf()
        var work = 0
        var time: LocalTime?
        val sleep = LocalTime.parse(mySharePreferences.getSleep(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
//        beginTime = beginTimeReserve

        if(LocalTime.now() > beginTimeReserve && date == LocalDate.now().toString()){
            Log.d("APGetPlan", mySharePreferences.getPlan()?.size.toString())
            if(mySharePreferences.getPlan().isNullOrEmpty() || (mySharePreferences.getPlan()?.last()?.task?.type == "one_time" && mySharePreferences.getPlan()?.last()?.task?.category != "work")){
                beginTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute).plusMinutes(10)
                val pastTime = beginTimeReserve?.hour?.toLong()?.let { beginTimeReserve?.minute?.toLong()?.let { it1 -> LocalTime.now().minusHours(it).minusMinutes(it1) } }
                Log.d("5MinuteTime", pastTime.toString())
                mySharePreferences.setWorkTimePast(((pastTime?.hour)?.times(60)?: 0).plus((pastTime?.minute ?: 0)))
//                workTime -= ((pastTime?.hour)?.times(60)?: 0).plus((pastTime?.minute ?: 0))
                Log.d("5MinuteTimeWT", workTime.toString())
//                beginTimeReserve?.hour?.toLong()?.let { beginTimeReserve?.minute?.toLong()?.let { it1 -> LocalTime.now().minusHours(it).minusMinutes(it1).toString() } }?.let { Log.d("5MinuteTime", it) }

            }
//            else if (LocalTime.now() > LocalTime.parse(mySharePreferences.getWorkEnd(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))){
//                beginTime = LocalTime.parse(mySharePreferences.getSleep(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
//            }
            else {
                for (i in 0 until (mySharePreferences.getPlan()?.size ?: 0)){
                    if(mySharePreferences.getPlan()?.get(i)?.task?.type == "one_time"){
                        beginTime = mySharePreferences.getPlan()?.get(i)!!.begin
                        break
                    }
                }
            }
            val temp = beginTime.minusHours(LocalTime.now().hour.toLong()).minusMinutes(LocalTime.now().minute.toLong())
            Log.d("MyBeginTime", (temp.hour * 60 + temp.minute).toString())
            if(beginTime > LocalTime.now() && (temp.hour * 60 + temp.minute) > mySharePreferences.getPomodoroWork()){
                beginTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute).plusMinutes(5)
                Log.d("MyBeginTime", "MyBeginTime")
            }
        }

        Log.d("MyBeginTime", beginTime.toString())

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

        Log.d("tasks_size", tasks.size.toString())

        tasks.sortBy { it.begin }

        var i = 1
        while(i < tasks.size){
            if(i > 0){
                if(tasks[i].begin == tasks[i-1].begin && i > 0){
                    if(tasks[i].end > tasks[i-1].end){
                        tasks.removeAt(i-1)
                    } else tasks.removeAt(i)
                    i--
//                break
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

        tasks.forEach {
            Log.d("tasks", "${it.begin}/${it.end}")
        }

        //intervals
        intervals?.clear()

        Log.d("beginTime", beginTime.toString())
        Log.d("workTime", workTime.toString())

        var flag: Boolean = true
        for(i in 0 until tasks.size){
            Log.d("tasks!", tasks[i].begin.toString())
//            if(tasks[i].end < beginTime){
//                tasks.removeFirst()
//            } else {
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
                        Log.d("work3", (time.hour*60 + time.minute).toString())
                    } else break
                }
                if(beginTime < tasks[i].begin && i > 0 && flag){
                    if(work < workTime){
                        time = tasks[i].begin.minus(tasks[i - 1].end.hour.toLong(), ChronoUnit.HOURS).minus(tasks[i - 1].end.minute.toLong(), ChronoUnit.MINUTES)
                        intervals?.add(TasksForPlan(tasks[i - 1].end, tasks[i].begin, (time.hour * 60 + time.minute), null))
                        work += time.hour*60 + time.minute
                        Log.d("work3", (time.hour*60 + time.minute).toString())
                    } else break
                }

//            }

        }

        if(!intervals.isNullOrEmpty() && intervals?.first()?.begin!! < beginTime){
            intervals?.first()?.begin = beginTime
        }

        work = 0
//        intervals?.forEach {
//            work += it.time!!
//            Log.d("intervals2", "${it.begin}/${it.end}")
//            Log.d("intervals2", it.time.toString())
//        }

        intervals?.forEach {
            val time = it.end.minusHours(it.begin.hour.toLong()).minusMinutes(it.begin.minute.toLong())
            it.time = time.minute + time.hour*60
            Log.d("intervals", "${it.begin}/${it.end}")
            Log.d("intervals", it.time.toString())
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

        Log.d("work1", work.toString())
        Log.d("work2", workTime.toString())

        if(work < workTime && intervals != null){
            val rest = workTime - work
            intervals!!.add(TasksForPlan(tasks.last().end, tasks.last().end.plusMinutes(rest.toLong()), rest, null))

//            intervals?.last()?.end = intervals?.last()?.end?.plusMinutes(rest.toLong())!!
//            intervals?.last()?.time = intervals?.last()?.time?.plus(rest)
        }

//        if(!intervals.isNullOrEmpty()){
//            if(sleep.hour < intervals?.last()?.end?.hour as Int){
//                while(((intervals!!.last().end.hour*60 + intervals!!.last().end.minute) - (sleep.hour*60 + sleep.minute)) < 60){
//                    intervals!!.removeLast()
//                }
//            } else {
//                while(intervals!!.isNotEmpty() && ((sleep.hour*60 + sleep.minute) - (intervals!!.last().end.hour*60 + intervals!!.last().end.minute)) < 60){
//                    if(sleep.hour >= intervals?.last()?.end?.hour as Int)
//                        intervals!!.removeLast()
//                    else break
//                }
//            }
//        }

        if(!intervals.isNullOrEmpty()){
//            if(intervals!!.last().end > sleep && ((intervals!!.last().end.hour - sleep.hour) < ((beginTimeReserve?.hour)?.minus(sleep.hour)!!))){
//                intervals!!.last().end = sleep.minusHours(1)
//            }

            intervals!!.removeIf { ((it.end > sleep.minusHours(1) && sleep > LocalTime.of(12, 0) && it.end > LocalTime.of(12, 0))
                    || (intervals!!.last().end > sleep.minusHours(1) && sleep < LocalTime.of(12, 0) && it.end < LocalTime.of(12, 0)))
                    && it.task?.type == "one_time"}

//            while((intervals!!.last().end > sleep.minusHours(1)
//                            && sleep > LocalTime.of(12, 0) && intervals!!.last().end > LocalTime.of(12, 0))
//                    || (intervals!!.last().end > sleep.minusHours(1)
//                            && sleep < LocalTime.of(12, 0) && intervals!!.last().end < LocalTime.of(12, 0))){
//                if(intervals!!.last().task?.type == "one_time"){
//                    Log.d("here", intervals!!.last().end.toString())
//                    intervals!!.removeLast()
//                } else break
//            }

//            val time = sleep.minusHours(intervals!!.last().end.hour.toLong()).minusMinutes(intervals!!.last().end.minute.toLong())
//            if((time.hour*60  + time.minute) < 60){
//                Log.d("here", time.toString())
//                intervals!!.last().end = sleep.minusHours(1)
//            }
        }

        intervals?.forEach {
//            val time = it.end.minusHours(it.begin.hour.toLong()).minusMinutes(it.begin.minute.toLong())
//            it.time = time.minute + time.hour*60
            Log.d("intervals3", "${it.begin}/${it.end}")
            Log.d("intervals3", it.time.toString())
        }

        Log.d("intervalsSize", intervals?.size.toString())

        //pomodoros
        pomodoros?.clear()

        intervals?.forEach {
            var pomodoro = mySharePreferences.getPomodoroWork()
            val breakTime = mySharePreferences.getPomodoroBreak()
            val bigBreakTime = mySharePreferences.getPomodoroBigBreak()
            pomodoro = 25
            val count = it.time?.div(pomodoro)
            Log.d("countPom", count.toString())
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
//                        if(minutes != 0){
                        if(minutes < 0){
                            pomodoros?.removeLast()
                        }
                    }
                    Log.d("minutes $i", minutes.toString())
                }
            }
        }

        Log.d("pomodoros_size", pomodoros?.size.toString())

        pomodoros?.forEach {
            Log.d("pomodoros", "${it.begin}/${it.end}")
        }

        countOfFullTasks = 0
        this.tasks?.clear()
        initTasks()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTasks() {
        Log.d("one_time", oneTimeTasks?.size.toString())

//        if(LocalTime.now() > beginTime && !mySharePreferences.getPlan().isNullOrEmpty() && date == LocalDate.now().toString()){
//            beginTime = mySharePreferences.getPlan()?.first()?.begin ?: beginTime
//            Log.d("beginTimeNew", beginTime.toString())
//        } else if(mySharePreferences.getPlan().isNullOrEmpty()){}

//        if(LocalTime.now() > beginTimeReserve && date == LocalDate.now().toString()){
//            beginTime = if(mySharePreferences.getPlan().isNullOrEmpty()){
//                LocalTime.of(LocalTime.now().hour, LocalTime.now().minute).plusMinutes(5)
//            } else mySharePreferences.getPlan()?.first()?.begin ?: beginTime
//        }

//        Log.d("beginTime",  (LocalTime.of(LocalTime.now().hour, LocalTime.now().minute).plusMinutes(5)).toString())

        oneTimeTasks?.forEach {
            val current = LocalDate.now()
            val date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            var duration: Int? = it.duration
            var daysBeforeDeadline = ChronoUnit.DAYS.between(current,
                    LocalDate.parse(it.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            //план на будущее
            Log.d("daysBeforeDeadline-${it.title}", daysBeforeDeadline.toString())
            Log.d("duration1-${it.title}", duration.toString())

            var pomodoros: Int?
            var pomodorosInDay = 0
            var workDays: Int

            if(current != date){
                val daysBetweenDates = ChronoUnit.DAYS.between(current, date)
                Log.d("daysBetweenDates-${it.title}", daysBetweenDates.toString())

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

                Log.d("pomodoros-${it.title}", pomodoros.toString())
                Log.d("pomodorosInDay-${it.title}", pomodorosInDay.toString())
                Log.d("workDays-${it.title}", workDays.toString())
                Log.d("leftWorkDays-${it.title}", leftWorkDays.toString())
                Log.d("pomodorosInDay-${it.title}", pomodorosInDay.toString())

                duration = if(workDays == 0){
                    0
                } else duration?.minus(leftWorkDays * mySharePreferences.getPomodoroWork() * pomodorosInDay)
            }
            Log.d("duration2-${it.title}", duration.toString())

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

                Log.d("days", daysBeforeDeadline.toString())

                workDays = calculateWorkDays(daysBeforeDeadline, it)
                Log.d("daysForWork", workDays.toString())

                if(workDays != 0){
                    Log.d("daysForWork2", (pomodoros/workDays).toString())
                    pomodorosInDay = when {
                        workDays > pomodoros -> 1
                        pomodoros%workDays == 0 -> pomodoros/workDays
                        else -> pomodoros/workDays + 1
                    }

                    Log.d("pomodoros-${it.title}", pomodoros.toString())
                    Log.d("pomodorosInDay-${it.title}", pomodorosInDay.toString())
                    for(i in 0 until pomodorosInDay){
                        val count = tasks?.count { task -> task == it }
                        Log.d("count", count.toString())
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

        Log.d("countOfFullTasks", countOfFullTasks.toString())
        Log.d("pomodoros_size3", pomodoros?.size.toString())
        Log.d("tasks_size3", tasks?.size.toString())
        if(pomodoros != null && tasks != null){
            if(oneTimeTasks.isNullOrEmpty()){
                pomodoros?.removeIf { task -> task.task == null }
            } else if (pomodoros?.size!! > tasks?.size!! && countOfFullTasks < oneTimeTasks!!.size) {
                Log.d("countOfFullTasksIn", countOfFullTasks.toString())
                countOfFullTasks = 0
                initTasks()
            }
        }

        tasks?.forEach {
            it?.title?.let { it1 -> Log.d("tasksInPLan", it1) }
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
        //условия сохранения нового плана
        Log.d("pomodoros_size2", pomodoros?.size.toString())
        Log.d("tasks_size2", tasks?.size.toString())
        if(pomodoros != null && tasks != null) {
            tasks!!.removeIf { tasks -> tasks == null }
            var sleepTime = LocalTime.parse(mySharePreferences.getSleep(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            if(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now())
            {
                Log.d("plan", "1")
                mySharePreferences.setToday(LocalDate.now().toString())

                pomodorosSmall?.clear()
                pomodorosSmall = byWorkPeak()
                pomodorosSmall?.forEach {
                    Log.d("pomodorosSmallFinish", "${it.begin}-${it.end}: ${it.task?.title}")
                }

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

//                mySharePreferences.getFirstTasksToday()?.let { pomodorosSmall?.addAll(it) }

                pomodorosSmall?.removeIf {it.end < LocalTime.now() && it.task?.type != "one_time"}

                mySharePreferences.setPlan(pomodorosSmall)
                mySharePreferences.setPlanForDay(true)
//                pomodorosSmall?.let { mySharePreferences.setPlan(it)
//                                    mySharePreferences.setPlanForDay(true) }

                firstTasks?.let { mySharePreferences.setFirstTasksNext(it) }
                firstTasks?.forEach {
                    Log.d("firstTasks1", "${it.begin}-${it.end}: ${it.task?.title}")
                }

                mySharePreferences.getFirstTasksToday()?.forEach {
                    Log.d("firstTasks2", "${it.begin}-${it.end}: ${it.task?.title}")
                    Log.d("firstTasks2", mySharePreferences.getFirstTasksToday()!!.size.toString())
                }

                pomodorosSmall?.forEach {
                    Log.d("finish1", "${it.begin}-${it.end}: ${it.task?.title}")
                }

                pomodorosSmall?.clear()

                pomodorosSmall = mySharePreferences.getPlan()
//
//                pomodorosSmall?.forEach {
//                    if(it.end < LocalTime.now()){
//                        val task = it.task
//                        task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
//                        Log.d("duration", task?.duration.toString())
//                        task?.let { it1 -> taskViewModel.update(it1) }
//                    }
//                }

//                pomodorosSmall?.removeIf { pom -> pom.end < LocalTime.now()}

                if(pomodorosSmall != null){
                    Log.d("workTimeFirst", date)
                    Log.d("workTimeFirst", LocalDate.now().toString())
                    Log.d("workTimeFirst", workTimeFirst.toString())
                    Log.d("workTimeFirstP", workTimePast.toString())
                    if(workTimeFirst <= this.workTimePast
                            || pomodorosSmall!!.isEmpty()
                            || (oneTimeTasks?.isEmpty() != false && routineTasks?.isEmpty() != false && fixedTasks?.isEmpty() != false)
                            || LocalTime.now() >= sleepTime.minusHours(1) && (sleepTime > LocalTime.of(12, 0) || sleepTime == LocalTime.of(0, 0))
                            || LocalTime.now() <= sleepTime.minusHours(1) && sleepTime < LocalTime.of(12, 0) && sleepTime != LocalTime.of(0, 0))
                    {
                        Log.d("rest", "rest")
                        pomodorosSmall!!.clear()
                        rest?.forEach {
                            pomodorosSmall!!.add(TasksForPlan(LocalTime.now(), LocalTime.now(), null, it))
                        }
                    }
                }

//                mySharePreferences.getPlan()?.let { pomodorosSmall?.addAll(it) }
                mySharePreferences.setPlan(pomodorosSmall)

                pomodorosSmall?.forEach {
                    Log.d("finish2", "${it.begin}-${it.end}: ${it.task?.title}")
                }
                showTasks(pomodorosSmall)
            } else {
                Log.d("plan", "3")
//                Log.d("getFirstTasks", mySharePreferences.getFirstTasks()?.size.toString())
                val days = ChronoUnit.DAYS.between(LocalDate.now(),
                        LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                val i: Long = 1
                pomodorosSmall?.clear()
                pomodorosSmall = byWorkPeak()
                pomodorosSmall?.forEach {
                    Log.d("finish3", "${it.begin}-${it.end}: ${it.task?.title}")
                }
                if(days == i){
                    mySharePreferences.getFirstTasksNext()?.let { pomodorosSmall?.addAll(it) }
                }

                //добавлено
                if(pomodorosSmall != null){
                    if(pomodorosSmall!!.isEmpty()
                            || (oneTimeTasks?.isEmpty() != false && routineTasks?.isEmpty() != false && fixedTasks?.isEmpty() != false)) {
                        Log.d("rest", "rest")
                        pomodorosSmall!!.clear()
                        rest?.forEach {
                            pomodorosSmall!!.add(TasksForPlan(LocalTime.now(), LocalTime.now(), null, it))
                        }
                    }
                }
                //
                showTasks(pomodorosSmall)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun byWorkPeak(): MutableList<TasksForPlan>? {
        var i = 0
        val pomodorsSmall: MutableList<TasksForPlan>? = mutableListOf()
        var tasksSmall: MutableList<Task?>? = mutableListOf()

        pomodorsSmall?.clear()
        tasksSmall?.clear()

        pomodoros?.let { pomodorsSmall?.addAll(it) }
        tasks?.let { tasksSmall?.addAll(it) }

        Log.d("@@@@@@@@@@@@@@", tasks?.size.toString())

//        tasks?.forEach {
//            it?.title?.let { it1 -> Log.d("tasksInPLan2", it1) }
//        }

        if(tasksSmall?.isNotEmpty()!!)
        {
            while (tasksSmall.size > pomodoros?.size!!){
                tasksSmall.removeLast()
            }
            tasksSmall.sortByDescending { it?.complexity }

            tasksSmall.forEach {
                it?.title?.let { it1 -> Log.d("tasksInPLan2", it1) }
            }

            //расставление по пику
            pomodorsSmall?.forEach {
                if(it.begin >= LocalTime.parse(mySharePreferences.getPeakBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        && it.begin < LocalTime.parse(mySharePreferences.getPeakEnd(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) && tasksSmall.isNotEmpty()){
                    it.task = tasksSmall.first()
//                    tasksSmall.first()?.title?.let { it1 -> Log.d("tasksInPLan2", it1) }
                    tasksSmall.removeFirst()
                }
            }

            //расставление оставшихся интервалов
            Log.d("tasks_size4", tasksSmall.size.toString())
            if(tasksSmall.isNotEmpty()){
                pomodorsSmall!!.forEach {
                    if(i < tasksSmall.size && it.task == null){
                        it.task = tasksSmall.get(i)
//                        tasksSmall[i]?.title?.let { it1 -> Log.d("tasksInPLan2", it1) }
                        i++
                    }
                }
            }
        }

//        pomodorsSmall?.removeIf { task -> task.begin < beginTime }

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

//        while (pomodorsSmall?.size!! > tasks?.size!!){
//            pomodorsSmall.removeLast()
//        }

        return pomodorsSmall
//        pomodorsSmall!!.sortBy { it.begin }
//
//        adapter?.setTasks(pomodorsSmall)
//        list.adapter = adapter
//
//        if(pomodorsSmall.isNullOrEmpty()){
//            view?.textView?.visibility = View.VISIBLE
//            view?.imageView?.visibility = View.VISIBLE
//        } else {
//            view?.textView?.visibility = View.INVISIBLE
//            view?.imageView?.visibility = View.INVISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTasks(pomodoros: MutableList<TasksForPlan>?)
    {
        if (pomodoros != null && pomodoros.isNotEmpty()) {
            if(pomodoros.first().task?.type == "one_time" && pomodoros.first().task?.category != "work"){
                pomodoros.sortByDescending { it.task?.priority }
            } else pomodoros.sortBy { it.begin }

            pomodoros.removeIf { it.task?.type != "one_time" && it.end < LocalTime.now() && it.end != it.begin && date == LocalDate.now().toString()}

            mySharePreferences.setPlan(pomodoros)

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
//        if(LocalTime.now() < mySharePreferences.getPlan()?.get(0)?.begin){
            if(position == 0){
                showMinutesDialog()
            }
//        }
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

        builderSingle.setNegativeButton("Отмена",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builderSingle.setAdapter(arrayAdapter,
                DialogInterface.OnClickListener { dialog, which ->
                    val pomodoros = mySharePreferences.getPlan()
                    if(pomodoros?.get(0)?.task?.type == "one_time" && pomodoros[0].begin == beginTimeReserve){
                        mySharePreferences.setTaskTransfer(mySharePreferences.getTaskTransfer()+(arrayAdapter?.getItem(which)?.toInt() ?: 0))
                    } else {
                        mySharePreferences.setTaskTransfer(0)
                        if(pomodoros?.get(0)?.task?.type == "one_time"){
                            Log.d("minutesBegin", pomodoros[0].begin.toString())
                            pomodoros[0].begin = arrayAdapter?.getItem(which)?.toLong()?.let { pomodoros[0].begin.plusMinutes(it) }!!
                            Log.d("minutesBegin", pomodoros[0].begin.toString())
                            mySharePreferences.setPlan(pomodoros)
                            Log.d("minutesBegin", mySharePreferences.getPlan()?.get(0)?.begin.toString())
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

                    Log.i("Selected Item ", arrayAdapter?.getItem(which)!!)
                    dialog.dismiss()
                })
        builderSingle.show()
    }
}