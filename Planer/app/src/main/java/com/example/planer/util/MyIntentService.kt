package com.example.planer.util

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyIntentService: JobIntentService()
{
    companion object{
        fun enqueueWork(context: Context?, intent: Intent?) {
            context?.let { intent?.let { it1 -> enqueueWork(it, MyIntentService::class.java, 1, it1) } }
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d("MyTestService3", "Service running");
    }
}