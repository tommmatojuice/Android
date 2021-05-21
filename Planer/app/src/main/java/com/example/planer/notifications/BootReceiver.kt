package com.example.planer.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class BootReceiver: BroadcastReceiver()
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, bootintent: Intent?) {
        context?.startService(Intent(context, BootAlarmService::class.java))
    }
}