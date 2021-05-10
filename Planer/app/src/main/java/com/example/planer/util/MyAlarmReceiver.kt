package com.example.planer.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.JobIntentService.enqueueWork
import androidx.core.content.ContextCompat.getSystemService
import java.util.*
import java.util.concurrent.TimeUnit

class MyAlarmReceiver: BroadcastReceiver()
{
    companion object{
        val REQUEST_CODE = 12345
        val ACTION = "com.codepath.example.servicesdemo.alarm"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MyTestService2", "Service running")

//        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intentPlan = Intent(context, MyAlarmReceiver::class.java)
//        val pendingIntentPlan = PendingIntent.getBroadcast(context, 1, intentPlan, 0)
//
//        alarmManager.cancel(pendingIntentPlan)

        val i = Intent(context, MyIntentService::class.java)
        i.putExtra("maxCountValue", 10)
        MyIntentService.enqueueWork(context, i)

//        scheduleNotification()
    }
}

/*
* private fun scheduleNotification(context: Context, delay: Long, notificationId: Int) { //delay is after how much time(in millis) from current time you want to schedule the notification
        val builder: Notification.Builder? = Notification.Builder(context)
                .setContentTitle(context.getString(R.string.emptyPhoneNumber))
                .setContentText(context.getString(R.string.cancel))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.btn_plus)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        val intent = Intent(context, MainActivity::class.java)
        val activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder?.setContentIntent(activity)
        val notification: Notification? = builder?.build()
        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }*/