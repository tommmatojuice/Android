package com.example.planer.background_job

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast
import com.example.planer.util.ToastMessages

class MyJobService : JobService() {
    override fun onStartJob(parameters: JobParameters?): Boolean
    {
//        ToastMessages.showMessage(this, "test2")
        Log.d("test2", "test2")
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }
}