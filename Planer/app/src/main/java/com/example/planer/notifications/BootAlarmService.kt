package com.example.planer.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.planer.util.MySharePreferences
import java.time.LocalTime
import java.util.*

class BootAlarmService: Service()
{
    private val TAG = "BootAlarmService"
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreate() {
        Log.d(TAG, "oncreate()")
        mySharePreferences = MySharePreferences(this)
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "alarm_test: BootAlarmService.onStartCommand() Received start id $startId: $intent")
        if (intent != null){
            createNotificationOnBoot(intent.getIntExtra("index", 0))
        }
        else
            Toast.makeText(baseContext, "Intent was null in BootAlarmService.", Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationOnBoot(index: Int) {
        val inotify = Intent(this, NotificationAlarmService::class.java)
        inotify.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getService(this, 0, inotify, 0)

        if(!mySharePreferences.getPlan().isNullOrEmpty()){
            var time = mySharePreferences.getPlan()?.get(index)?.begin
            if (time != null) {
                if(time < LocalTime.now() && mySharePreferences.getPlan()?.size ?: 0 > index){
                    time = mySharePreferences.getPlan()?.get(index+1)?.begin
                }
            }
            val time2 = time?.minusHours(LocalTime.now().hour.toLong())?.minusMinutes(LocalTime.now().minute.toLong())
            val calendar = Calendar.getInstance()
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MINUTE] = calendar[Calendar.MINUTE] + ((time2?.hour ?: 0) *60 + (time2?.minute?.minus(5) ?: 0))
            if (Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
            }
            Log.d(TAG, "alarm set for " + calendar.getTime().toString())
        }

//        val notificationIntent = Intent(this, BootReceiver::class.java)
//        notificationIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
//        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val pendingIntent = PendingIntent.getBroadcast(this, 1001, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//
//        val calendar: Calendar = Calendar.getInstance()
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1)
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
//        Log.d(TAG, "alarm set for " + calendar.getTime().toString())
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}