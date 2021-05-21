package com.example.planer.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.planer.util.MySharePreferences
import java.time.LocalTime
import java.util.*

class BootAlarmService: Service()
{
    private val TAG = "BootAlarmService"
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreate() {
        super.onCreate()
        mySharePreferences = MySharePreferences(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "alarm_test: BootAlarmService.onStartCommand() Received start id $startId: $intent")
        if (intent != null){
            createNotificationOnBoot(intent.getIntExtra("index", 0))
        }
        else Log.d(TAG, "Intent was null in BootAlarmService.")
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationOnBoot(index: Int) {
        if(!mySharePreferences.getPlan().isNullOrEmpty()){
            if (mySharePreferences.getPlan()?.first()?.task?.type == "one_time" && mySharePreferences.getPlan()!!.first().task?.category != "work"){
                autoPlan()
            } else {
                val inotify = Intent(this, NotificationAlarmService::class.java)
                inotify.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                val pendingIntent = PendingIntent.getService(this, 0, inotify, 0)

                val time = if(index == 0){
                    mySharePreferences.getPlan()?.get(0)?.end
                } else {
                    mySharePreferences.getPlan()?.get(0)?.begin
                }

                val time2 = time?.minusHours(LocalTime.now().hour.toLong())?.minusMinutes(LocalTime.now().minute.toLong())
                val calendar = Calendar.getInstance()
                calendar[Calendar.SECOND] = 0
                if(index == 0){
                    calendar[Calendar.MINUTE] = calendar[Calendar.MINUTE] + ((time2?.hour ?: 0) *60 + (time2?.minute ?: 0))
                } else {
                    calendar[Calendar.MINUTE] = calendar[Calendar.MINUTE] + ((time2?.hour ?: 0) *60 + (time2?.minute?.minus(2) ?: 0))
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
                }
                Log.d(TAG, "alarm set for " + calendar.getTime().toString())
            }
        } else {
            autoPlan()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun autoPlan(){
        val inotify = Intent(this, AutoPlanService::class.java)
        inotify.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getService(this, 0, inotify, 0)

        val time = LocalTime.of(0, 10)
        val time2 = time?.minusHours(LocalTime.now().hour.toLong())?.minusMinutes(LocalTime.now().minute.toLong())
        val calendar = Calendar.getInstance()
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MINUTE] = calendar[Calendar.MINUTE] + ((time2?.hour ?: 0) *60 + (time2?.minute ?: 0))
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        }
        Log.d(TAG, "alarm set for " + calendar.getTime().toString())
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}