package com.example.planer.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.util.*

class BootAlarmService: Service()
{
    private val TAG = "BootAlarmService"

    override fun onCreate() {
        Log.d(TAG, "oncreate()")
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "alarm_test: BootAlarmService.onStartCommand() Received start id $startId: $intent")
        if (intent != null)
            createNotificationOnBoot()
        else
            Toast.makeText(baseContext, "Intent was null in BootAlarmService.", Toast.LENGTH_LONG).show()
        return START_STICKY
    }


    private fun createNotificationOnBoot() {
        val inotify = Intent(this, NotificationAlarmService::class.java)
        inotify.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getService(this, 0, inotify, 0)

        // go off two mins from now
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1)
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        }
        Log.d(TAG, "alarm set for " + calendar.getTime().toString())

//        val notificationIntent = Intent(this, BootReceiver::class.java)
//        notificationIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
//        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val pendingIntent = PendingIntent.getBroadcast(this, 1001, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//
//        val calendar: Calendar = Calendar.getInstance()
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1)
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
//        Log.d(TAG, "alarm set for " + calendar.getTime().toString())
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}