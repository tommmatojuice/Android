package com.example.planer.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.planer.MainActivity.Companion.scheduleNotification

class MyNewReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(Intent.ACTION_BOOT_COMPLETED == intent?.action){
            Log.d("mvm", "BroadcastReceiver running")

//            scheduleNotification()
        }
    }
}