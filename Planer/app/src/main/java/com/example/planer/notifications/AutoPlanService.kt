package com.example.planer.notifications

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.planer.database.MyDataBase
import com.example.planer.ui.plan.AutoPlan
import com.example.planer.util.MySharePreferences
import java.time.LocalDate
import java.time.LocalTime

class AutoPlanService: Service()
{
    private val TAG = "AutoPlanService"

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "alarm_test: AutoPlanService.onStartCommand()")
        if (intent != null){
            AutoPlan(LocalDate.now().toString(), LocalDate.now().dayOfWeek.toString())
            this.startService(Intent(this, BootAlarmService::class.java).putExtra("index", 1))
        }
        else Toast.makeText(baseContext, "Intent was null in AutoPlanService.", Toast.LENGTH_LONG).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}