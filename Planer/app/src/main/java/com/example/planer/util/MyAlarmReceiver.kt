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
import com.example.planer.MainActivity.Companion.scheduleNotification
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