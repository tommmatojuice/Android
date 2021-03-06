package com.example.planer

import android.util.Log
import org.quartz.Job
import org.quartz.JobExecutionContext

class SimpleJob: Job {
    override fun execute(context: JobExecutionContext?) {
        val dataMap = context?.jobDetail?.jobDataMap
        Log.d("message", "Hello world!")
    }
}