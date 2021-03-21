package com.example.planer.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.planer.ui.plan.AutoPlan
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

class AlertReceiver: BroadcastReceiver()
{
    private lateinit var mySharePreferences: MySharePreferences
    private var context: Context? = null
    private val tasksDataBase: TasksDataBase = TasksDataBase()

    val CHANNEL_ID = "ForegroundServiceChannel"
    private val TAG = "ExampleJobService"
    private val NOTIFY_ID = 101

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?)
    {
        this.context = context

        Log.d("Hello", "Hello!: ${LocalTime.now()}")
        var triggerTime: Long = intent?.getLongExtra("KEY_TRIGGER_TIME", System.currentTimeMillis()) ?:0
        triggerTime += TimeUnit.DAYS.toMillis(1)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentPlan = Intent(context, AlertReceiver::class.java)
        val pendingIntentPlan = PendingIntent.getBroadcast(context, 1, intentPlan, 0)

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntentPlan
        )
        /*в 00:01 берем то, что осталась от плана в преференсах и вычитаем время из бд,
        * затем вызываем AutoPlan с наступившим днем и днем недели
        * затем устанавливаем уведомления на каждое начало
        * при выборе перенова переносим все задачи до первой фиксированной/регулярной или до приема пиши
        * если вермя последней задачи заходит на другую, удаляем ее*/

        mySharePreferences = MySharePreferences(context)

        var pomodoros = mySharePreferences.getPlan()

        if(mySharePreferences.getToday() != LocalDate.now().toString()){

            //обновление времени задач
            pomodoros?.forEach {
                val task = it.task
                task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
                task?.let { it1 -> tasksDataBase.updateTask(it1) }
            }

            //обновление плана
            AutoPlan(LocalDate.now().toString(), LocalDate.now().dayOfWeek.toString())
        }

//        val serviceIntent = Intent(context, MyService::class.java)
//        ContextCompat.startForegroundService(context, serviceIntent)

        //установка уведомлений
        pomodoros = mySharePreferences.getPlan()

        val intentNotification = Intent(context, NotificationReceiver::class.java)
        intentNotification.putExtra("TITLE", "лаба")
        val pendingIntentNotification1 = PendingIntent.getBroadcast(context, 1, intentNotification, 0)

        val calendar: Calendar = Calendar.getInstance()

        Log.d("pomodorosNotif", pomodoros?.size.toString())

        pomodoros?.forEach {
            val time = it.begin.minusMinutes(5)

            calendar.apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, time.hour)
                set(Calendar.MINUTE, time.minute)
                set(Calendar.SECOND, 0)
            }

            intentNotification.putExtra("TITLE", it.task?.title)
            val pendingIntentNotification = PendingIntent.getBroadcast(context, 1, intentNotification, 0)

            Log.d("pomodorosNotifHere", time.toString())
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntentNotification
            )
        }

        calendar.apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 52)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntentNotification1
        )
    }
}