 package com.example.planer.ui.plan

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.MySharePreferences
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*
import kotlinx.android.synthetic.main.test.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class AutoPlan(private val date: String,
               private val weekDay: String
): Fragment(), PlanRecyclerAdapter.OnItemClickListener
{
    private lateinit var mySharePreferences: MySharePreferences
    private val taskViewModel: TaskViewModel by viewModels()
    private var fixedTasks: List<Task>? = null
    private var routineTasks: List<Task>? = null
    private var oneTimeTasks: MutableList<Task>? = mutableListOf()
    private var rest: MutableList<Task>? = mutableListOf()

    private var workTime: Int = 0
    private var dayNumber: Int = 0
    private lateinit var beginTime: LocalTime
    private lateinit var beginTimeReserve: LocalTime
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

        adapter = this.context?.let { PlanRecyclerAdapter(it, pomodoros, this) }
        list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        //вычитаем время пройденных задач
        pomodorosSmall = mySharePreferences.getPlan()

//        if(LocalDate.now().toString() != mySharePreferences.getToday()){
//            mySharePreferences.setWorkTimePast(0)
//        }
//
//        if(!pomodorosSmall.isNullOrEmpty()){
//            pomodorosSmall?.forEach {
//                if(it.end < LocalTime.now() || LocalDate.now().toString() != mySharePreferences.getToday()){
//                    val task = it.task
//                    task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
//                    mySharePreferences.setWorkTimePast(mySharePreferences.getWorkTimePast()+mySharePreferences.getPomodoroWork())
//                    this.context?.let { it1 -> ToastMessages.showMessage(it1, "Minus time!") }
//                    task?.let { it1 -> taskViewModel.update(it1) }
//                }
//            }
//        }
//
//        if(LocalDate.now().toString() != mySharePreferences.getToday()){
//            mySharePreferences.setWorkTimePast(0)
//        }

        Log.d("workTimePast", mySharePreferences.getWorkTimePast().toString())

        taskViewModel.fixedTasksByDate(date).observe(
                viewLifecycleOwner, { fixedTasks ->
            if (fixedTasks != null) {
                this.fixedTasks = fixedTasks
            }
        }
        )

        initWeekDay()

        view.progressBar.visibility = View.VISIBLE
        Handler().postDelayed({ getAllWorkTime()
            getPomodoros()
            view.progressBar.visibility = View.INVISIBLE}, 200)

        Log.d("first_work_time", workTime.toString())
        Log.d("dateeee", date)
        Log.d("weekday", weekDay)

        if(date == LocalDate.now().toString())
        {
            val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                    Log.d("onSwiped", viewHolder.adapterPosition.toString())
                    pomodorosSmall?.removeAt(viewHolder.adapterPosition)
                    mySharePreferences.setPlan(pomodorosSmall)
                    pomodorosSmall?.let { adapter?.setTasks(it) }
                    list.adapter = adapter
                }
            }

            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(list)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initWeekDay(){
        oneTimeTasks?.clear()
        rest?.clear()
        when(weekDay){
            "MONDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getMondayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                beginTime = LocalTime.parse(mySharePreferences.getMondayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.tasksMon("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                    }
                })

                taskViewModel.tasksMon("one_time").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if (oneTimeTasks != null) {
                        this.oneTimeTasks?.addAll(oneTimeTasks)
                        this.rest?.addAll(oneTimeTasks)
                        this.oneTimeTasks?.removeIf { it.category != "work" }
                        this.rest?.removeIf { it.category == "work" }
                    }
                })
            }
            "TUESDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getTuesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 1
                beginTime = LocalTime.parse(mySharePreferences.getTuesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.tasksTue("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                    }
                }
                )

                taskViewModel.tasksTue("one_time").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if (oneTimeTasks != null) {
                        this.oneTimeTasks?.addAll(oneTimeTasks)
                        this.rest?.addAll(oneTimeTasks)
                        this.oneTimeTasks?.removeIf { it.category != "work" }
                        this.rest?.removeIf { it.category == "work" }
                    }
                })
            }
            "WEDNESDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getWednesdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 2
                beginTime = LocalTime.parse(mySharePreferences.getWednesdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.tasksWen("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                    }
                }
                )

                taskViewModel.tasksWen("one_time").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if (oneTimeTasks != null) {
                        this.oneTimeTasks?.addAll(oneTimeTasks)
                        this.rest?.addAll(oneTimeTasks)
                        this.oneTimeTasks?.removeIf { it.category != "work" }
                        this.rest?.removeIf { it.category == "work" }
                    }
                })
            }
            "THURSDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getThursdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 3
                beginTime = LocalTime.parse(mySharePreferences.getThursdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.tasksThu("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                    }
                })

                taskViewModel.tasksThu("one_time").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if (oneTimeTasks != null) {
                        this.oneTimeTasks?.addAll(oneTimeTasks)
                        this.rest?.addAll(oneTimeTasks)
                        this.oneTimeTasks?.removeIf { it.category != "work" }
                        this.rest?.removeIf { it.category == "work" }
                    }
                })
            }
            "FRIDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getFridayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 4
                beginTime = LocalTime.parse(mySharePreferences.getFridayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.tasksFri("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                    }
                })

                taskViewModel.tasksFri("one_time").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if (oneTimeTasks != null) {
                        this.oneTimeTasks?.addAll(oneTimeTasks)
                        this.rest?.addAll(oneTimeTasks)
                        this.oneTimeTasks?.removeIf { it.category != "work" }
                        this.rest?.removeIf { it.category == "work" }
                    }
                })
            }
            "SATURDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getSaturdayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 5
                beginTime = LocalTime.parse(mySharePreferences.getSaturdayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.tasksSat("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                    }
                })

                taskViewModel.tasksSat("one_time").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if (oneTimeTasks != null) {
                        this.oneTimeTasks?.addAll(oneTimeTasks)
                        this.rest?.addAll(oneTimeTasks)
                        this.oneTimeTasks?.removeIf { it.category != "work" }
                        this.rest?.removeIf { it.category == "work" }
                    }
                })
            }
            "SUNDAY" -> {
                val time = LocalTime.parse(mySharePreferences.getSundayWork(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                workTime = time.hour * 60 + time.minute
                dayNumber = 6
                beginTime = LocalTime.parse(mySharePreferences.getSundayBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                taskViewModel.tasksSun("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                    }
                })

                taskViewModel.tasksSun("one_time").observe(
                        viewLifecycleOwner, { oneTimeTasks ->
                    if (oneTimeTasks != null) {
                        this.oneTimeTasks?.addAll(oneTimeTasks)
                        this.rest?.addAll(oneTimeTasks)
                        this.oneTimeTasks?.removeIf { it.category != "work" }
                        this.rest?.removeIf { it.category == "work" }
                    }
                })
            }
        }
        beginTimeReserve = beginTime
        workTime -= mySharePreferences.getWorkTimePast()
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
            if(mySharePreferences.getPlan().isNullOrEmpty()){
                beginTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute).plusMinutes(5)
            } else {
                for (i in 0 until (mySharePreferences.getPlan()?.size ?: 0)){
                    if(mySharePreferences.getPlan()?.get(i)?.task?.type == "one_time"){
                        beginTime = mySharePreferences.getPlan()?.get(i)!!.begin
                        break
                    }
                }
//                beginTime = mySharePreferences.getPlan()?.first()?.begin ?: beginTime
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

        Log.d("tasks_size", tasks.size.toString())

        tasks.sortBy { it.begin }

        var i = 1
        while(i < tasks.size){
            if(tasks[i].begin == tasks[i-1].begin){
                if(tasks[i].end > tasks[i-1].end){
                    tasks.removeAt(i-1)
                } else tasks.removeAt(i)
                i--
                break
            }
            if(tasks[i].begin == tasks[i-1].end){
                tasks[i-1].end = tasks[i].end
                tasks.removeAt(i)
                i--
            }
            if(tasks[i].end == tasks[i-1].end){
                tasks.removeAt(i)
                i--
            }
            i++
        }

        tasks.forEach {
            Log.d("tasks", "${it.begin}/${it.end}")
        }

        //intervals
        intervals?.clear()

        Log.d("beginTime", beginTime.toString())
        Log.d("workTime", workTime.toString())

        for(i in 0 until tasks.size){
            Log.d("tasks!", tasks[i].begin.toString())
            if(beginTime < tasks[i].begin){
                Log.d("beginTime ${tasks[i]}", tasks[i].begin.toString())
                Log.d("workTime1!", work.toString())
                Log.d("workTime2!", workTime.toString())
                if(work < workTime){
                    time = tasks[i].begin.minus(tasks[i - 1].end.hour.toLong(), ChronoUnit.HOURS).minus(tasks[i - 1].end.minute.toLong(), ChronoUnit.MINUTES)
                    intervals?.add(TasksForPlan(tasks[i - 1].end, tasks[i].begin, (time.hour * 60 + time.minute), null))
                    work += time.hour*60 + time.minute
                    Log.d("work3", (time.hour*60 + time.minute).toString())
                } else break
            }
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
            if(intervals!!.last().end > sleep && ((intervals!!.last().end.hour - sleep.hour) < (beginTimeReserve.hour - sleep.hour))){
                intervals!!.last().end = sleep.minusHours(1)
            }
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
            val pomodoro = mySharePreferences.getPomodoroWork()
            val breakTime = mySharePreferences.getPomodoroBreak()
            val bigBreakTime = mySharePreferences.getPomodoroBigBreak()
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
    private fun unitAll(){
//        mySharePreferences.setToday("2020-09-09")
//        var pomodorosSmall: MutableList<TasksForPlan>? = mutableListOf()
        val firstTasks: MutableList<TasksForPlan>? = mutableListOf()
        //условия сохранения нового плана
        Log.d("pomodoros_size2", pomodoros?.size.toString())
        Log.d("tasks_size2", tasks?.size.toString())
        if(pomodoros != null && tasks != null){
            tasks!!.removeIf { tasks -> tasks == null }
//            if(LocalDate.parse(mySharePreferences.getToday(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) != LocalDate.now()
//                    && LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now())
//            {
//                Log.d("plan", "1")
//                Log.d("plan", mySharePreferences.getToday().toString())
//
//                pomodorosSmall?.clear()
//
//                pomodorosSmall = mySharePreferences.getPlan()
//
//                pomodorosSmall?.forEach {
//                    Log.d("finish1", "${it.begin}-${it.end}: ${it.task?.title}")
//                }
//
//                pomodorosSmall?.forEach {
//                    if(it.end <  LocalTime.now()){
//                        val task = it.task
//                        task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
//                        task?.let { it1 -> taskViewModel.update(it1) }
//                    }
//                }
//
//                pomodorosSmall?.removeIf { pom -> pom.end < LocalTime.now()}
//
//                if(pomodorosSmall != null && pomodorosSmall?.isEmpty()!!){
//                    rest?.forEach {
//                        pomodorosSmall!!.add(TasksForPlan(LocalTime.now(), LocalTime.now(), null, it))
//                    }
//                }
//
//                mySharePreferences.setPlan(pomodorosSmall)
//
//                showTasks(pomodorosSmall)
//            } else if(LocalDate.parse(mySharePreferences.getToday(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now()
//                    && LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now())
            if(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) == LocalDate.now())
            {
                Log.d("plan", "2")
                mySharePreferences.setToday(LocalDate.now().toString())

                pomodorosSmall?.clear()
                pomodorosSmall = byWorkPeak()
                pomodorosSmall?.forEach {
                    Log.d("pomodorosSmallFinish", "${it.begin}-${it.end}: ${it.task?.title}")
                }

                pomodorosSmall?.forEach {
                    if(it.begin < beginTimeReserve && it.task?.type == "one_time"){
                        firstTasks?.add(it)
                    }
                }
                pomodorosSmall?.removeIf { task -> task.begin < beginTimeReserve && task.task?.type == "one_time"}

//                mySharePreferences.getFirstTasks()?.let { pomodorosSmall?.addAll(it) }

                mySharePreferences.setPlan(pomodorosSmall)
                mySharePreferences.setPlanForDay(true)
//                pomodorosSmall?.let { mySharePreferences.setPlan(it)
//                                    mySharePreferences.setPlanForDay(true) }

                firstTasks?.let { mySharePreferences.setFirstTasks(it) }
                firstTasks?.forEach {
                    Log.d("firstTasks", "${it.begin}-${it.end}: ${it.task?.title}")
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

                pomodorosSmall?.removeIf { pom -> pom.end < LocalTime.now()}

                if(pomodorosSmall != null && pomodorosSmall?.isEmpty()!!){
                    rest?.forEach {
                        pomodorosSmall!!.add(TasksForPlan(LocalTime.now(), LocalTime.now(), null, it))
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
                Log.d("getFirstTasks", mySharePreferences.getFirstTasks()?.size.toString())
                val days = ChronoUnit.DAYS.between(LocalDate.now(),
                        LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                val i: Long = 1
                pomodorosSmall?.clear()
                pomodorosSmall = byWorkPeak()
                pomodorosSmall?.forEach {
                    Log.d("finish3", "${it.begin}-${it.end}: ${it.task?.title}")
                }
//                if(days == i){
//                    mySharePreferences.getFirstTasks()?.let { pomodorosSmall?.addAll(it) }
//                }
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

            tasksSmall?.forEach {
                it?.title?.let { it1 -> Log.d("tasksInPLan2", it1) }
            }

            //расставление по пику
            pomodorsSmall?.forEach {
                if(it.begin >= LocalTime.parse(mySharePreferences.getPeakBegin(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                        && it.begin < LocalTime.parse(mySharePreferences.getPeakEnd(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)) && tasksSmall?.isNotEmpty()!!){
                    it.task = tasksSmall?.first()
//                    tasksSmall.first()?.title?.let { it1 -> Log.d("tasksInPLan2", it1) }
                    tasksSmall?.removeFirst()
                }
            }

            //расставление оставшихся интервалов
            Log.d("tasks_size4", tasksSmall?.size.toString())
            if(tasksSmall?.isNotEmpty()!!){
                pomodorsSmall!!.forEach {
                    if(i < tasksSmall?.size!! && it.task == null){
                        it.task = tasksSmall?.get(i)
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
            pomodorsSmall!!.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null, it
            ))
        }

        routineTasks?.forEach{
            pomodorsSmall!!.add(TasksForPlan(
                    LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    null, it
            ))
        }

        pomodorsSmall?.removeIf { task -> task.task == null }

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

    private fun showTasks(pomodoros: MutableList<TasksForPlan>?){
        pomodoros!!.sortBy { it.begin }

        adapter?.setTasks(pomodoros)
        list.adapter = adapter

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
}