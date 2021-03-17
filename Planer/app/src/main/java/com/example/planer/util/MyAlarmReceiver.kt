package com.example.planer.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService.enqueueWork
import java.util.concurrent.TimeUnit

class MyAlarmReceiver: BroadcastReceiver()
{
    companion object{
        val REQUEST_CODE = 12345
        val ACTION = "com.codepath.example.servicesdemo.alarm"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MyTestService2", "Service running");
        val i = Intent(context, MyIntentService::class.java)
        i.putExtra("maxCountValue", 10)
        MyIntentService.enqueueWork(context, intent)
    }
}