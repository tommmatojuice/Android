 package com.example.planer.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.ui.plan.TasksForPlan
import com.example.planer.util.MyIntentService
import com.example.planer.util.MySharePreferences
import java.time.LocalDate
import java.time.LocalTime


class NotifyWork(private val context: Context, private val workerParams: WorkerParameters) : Worker(context, workerParams) {
    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }

    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var taskViewModel: TaskViewModel

    @SuppressLint("WrongThread")
    @RequiresApi(O)
    override fun doWork(): Result {
        Log.d("mvm", "NotifyWork running")
        mySharePreferences = MySharePreferences(applicationContext)

//        scheduleNotification()

//        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        minusTime()

        Log.d("timeNow1", LocalTime.now().toString())
        Log.d("timeNow2", mySharePreferences.getPlan()?.get(0)?.begin.toString())
//        if(LocalTime.now().hour == mySharePreferences.getPlan()?.get(0)?.begin){
        if(LocalTime.now().hour == mySharePreferences.getPlan()?.get(0)?.begin?.hour
                && LocalTime.now().minute == mySharePreferences.getPlan()?.get(0)?.begin?.minute){
            Log.d("timeNow1", LocalTime.now().toString())
            Log.d("timeNow2", mySharePreferences.getPlan()?.get(0)?.begin?.minusMinutes(5).toString())
            val i = Intent(context, MyIntentService::class.java)
            MyIntentService.enqueueWork(applicationContext, i)
        }

        return success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun minusTime()
    {
        Log.d("minusTime2", "minusTime")

        var pomodoros: MutableList<TasksForPlan>? = mutableListOf()
        pomodoros = mySharePreferences.getPlan()

        if(LocalDate.now().toString() != mySharePreferences.getToday()){
            mySharePreferences.setWorkTimePast(0)
            mySharePreferences.getFirstTasksNext()?.let { mySharePreferences.setFirstTasksToday(it) }
            mySharePreferences.setWorkEnd(null)
        }

        if(!pomodoros.isNullOrEmpty()){
            if(pomodoros.last().task?.type == "one_time" && pomodoros.last().task?.category != "work"){
                //no
            } else {
                mySharePreferences.setWorkEnd(pomodoros.last().end.toString())
                pomodoros.forEach {
                    Log.d("pomodorosFromMain", "${it.begin}-${it.end}: ${it.task?.title}")
                    if((it.end <= LocalTime.now() && it.begin < LocalTime.now() && it.task?.type == "one_time") || LocalDate.now().toString() != mySharePreferences.getToday()){
                        Log.d("pomodorosFromMainMT", "${it.begin}-${it.end}: ${it.task?.title}")
                        val task = it.task
                        task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
                        mySharePreferences.setWorkTimePast(mySharePreferences.getWorkTimePast() + mySharePreferences.getPomodoroWork())
//                        task?.let { it1 -> taskViewModel.update(it1) }
                    }
                }
            }
        } else mySharePreferences.getSleep()?.let { mySharePreferences.setWorkEnd(it) }

        if(LocalDate.now().toString() != mySharePreferences.getToday()){
            mySharePreferences.setWorkTimePast(0)
        }

        pomodoros?.removeIf { it.end < LocalTime.now() && it.begin != it.end }
        mySharePreferences.setPlan(pomodoros)
    }

//    private fun sendNotification(id: Int) {
//        val intent = Intent(applicationContext, MainActivity::class.java)
//        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
//        intent.putExtra(NOTIFICATION_ID, id)
//
//        val notificationManager =
//                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        val pendingIntent = getActivity(applicationContext, 0, intent, 0)
//        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
//                .setSmallIcon(android.R.drawable.ic_menu_today)
//                .setContentTitle("Новая задача")
//                .setContentText("Через 5 минут начинается задача title")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача title. При необходимости перенесите задача ну 5, 10 или 30 минут."))
//                .addAction(android.R.drawable.btn_plus, "5 минут", pendingIntent)
//                .addAction(android.R.drawable.btn_plus, "10 минут", pendingIntent)
//                .addAction(android.R.drawable.btn_plus, "30 минут", pendingIntent)
//                .setAutoCancel(true)
//
//        notification.priority = PRIORITY_MAX
//
//        if (SDK_INT >= O) {
//            notification.setChannelId(NOTIFICATION_CHANNEL)
//
//            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
//            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
//                    .setContentType(CONTENT_TYPE_SONIFICATION).build()
//
//            val channel = NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)
//
//            channel.enableLights(true)
//            channel.lightColor = RED
//            channel.enableVibration(true)
//            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
//            channel.setSound(ringtoneManager, audioAttributes)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(id, notification.build())
//    }
}