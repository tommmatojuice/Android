package com.example.planer.notifications

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.planer.ui.plan.AutoPlan
import java.time.LocalDate

class AutoPlanService: Service()
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null){
            AutoPlan(LocalDate.now().toString(), LocalDate.now().dayOfWeek.toString())
            this.startService(Intent(this, BootAlarmService::class.java).putExtra("index", 1))
        }
        else Log.d("AutoPlanService", "Intent was null in AutoPlanService.")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}