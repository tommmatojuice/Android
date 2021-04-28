package com.example.planer.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.planer.util.MySharePreferences
import java.time.LocalTime

class NotificationsUtil(private val context: Context)
{
    private lateinit var mySharePreferences: MySharePreferences

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(number: Int)
    {
        mySharePreferences = MySharePreferences(context)
        if(!mySharePreferences.getPlan().isNullOrEmpty()){
            val firstTaskBegin = if(mySharePreferences.getPlan()?.size!! > 1){
                mySharePreferences.getPlan()?.get(number)?.begin?.minusMinutes(5)
            } else {
                mySharePreferences.getPlan()?.get(0)?.begin?.minusMinutes(5)
            }
            val timeNow = LocalTime.now()
            if (firstTaskBegin != null) {
                if(firstTaskBegin > timeNow){
                    val difference = firstTaskBegin.minusHours(timeNow.hour.toLong())?.minusMinutes(timeNow.minute.toLong())
                    val delay = ((difference?.hour?.times(60) ?: 0) + (difference?.minute ?: 0)) * 60000
                    scheduleNotification(this.context, delay.toLong(), 0)
                }
            }
        }
    }

    private fun scheduleNotification(context: Context, delay: Long, notificationId: Int) {//delay is after how much time(in millis) from current time you want to schedule the notification
        Log.d("MyNotificationPublisher", delay.toString())
        Log.d("MyNotificationPublisher", "scheduleFromUtil")
        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }
}