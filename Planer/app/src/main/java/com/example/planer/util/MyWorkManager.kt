package com.example.planer.util

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.LocalTime

class MyWorkManger(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        makePlan()
        Log.d(TAG, "doWork: end")
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makePlan() {
        Thread(Runnable {
            while (true) {
                if (LocalTime.now().hour == 14 && LocalTime.now().minute == 39) {
                    Log.d(TAG, "run: Hello!")
                }
                Log.d(TAG, "run: ${LocalTime.now()}")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }

    companion object {
        const val TAG = "workmng"
    }
}