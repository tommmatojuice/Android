package com.example.planer.util

import android.R
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.planer.MainActivity
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationReceiver: BroadcastReceiver()
{
    private var context: Context? = null

    val CHANNEL_ID = "ForegroundServiceChannel"
    private val TAG = "ExampleJobService"
    private val NOTIFY_ID = 101

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?)
    {
        this.context = context
        val title: String? = intent?.getStringExtra("TITLE")
        Log.d("onReceive", title.toString())
        createNotificationChannel()
        setNotifications(title)

//        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intentNotification = Intent(context, NotificationReceiver::class.java)
//        intentNotification.putExtra("TITLE", "лаба")
//        val pendingIntentNotification1 = PendingIntent.getBroadcast(context, 1, intentNotification, 0)
//
//        val calendar: Calendar = Calendar.getInstance()
//
//        calendar.apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 18)
//            set(Calendar.MINUTE, 46)
//            set(Calendar.SECOND, 0)
//        }
//
//        alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis.plus(TimeUnit.SECONDS.toMillis(5)),
//                pendingIntentNotification1
//        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context?.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotifications(title: String?){
        title?.let { Log.d("setNotifications", it) }
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_menu_today)
                    .setContentTitle("Новая задача")
                    .setContentText("Через 5 минут начинается задача \"$title\"")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача \"$title\". При необходимости перенесите задача ну 5, 10 или 30 минут."))
                    .addAction(R.drawable.btn_plus, "5 минут", pendingIntent)
                    .addAction(R.drawable.btn_plus,"10 минут", pendingIntent)
                    .addAction(R.drawable.btn_plus, "30 минут", pendingIntent)
                    .setAutoCancel(true)
        }

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        builder?.build()?.let { notificationManager?.notify(NOTIFY_ID, it) }
    }
}