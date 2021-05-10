package com.example.planer.notifications

import android.R
import android.app.*
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.planer.MainActivity
import com.example.planer.util.MySharePreferences

class NotificationAlarmService: Service()
{
    private val TAG = "NotificationAlarmService"

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "alarm_test: NotificationAlarmService.onStartCommand()")
        if (intent != null){
            //            createNotification()
            sendNotification(10001)
        }
        else
            Toast.makeText(baseContext, "Intent was null in NotificationAlarmService.", Toast.LENGTH_LONG).show()
        return super.onStartCommand(intent, flags, startId)
    }


    private fun createNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification(R.drawable.stat_sys_warning, "Note from AlarmService", System.currentTimeMillis())
        val i = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, i, 0)
        notificationManager.notify(10001, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(id: Int) {
        val mySharePreferences: MySharePreferences = MySharePreferences(applicationContext)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext,
                "appName_channel_01"
        )
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle("Новая задача")
            .setContentText("Через 5 минут начинается задача title")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача ... . При необходимости перенесите задача ну 5, 10 или 30 минут."))
//            .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача ${mySharePreferences.getPlan()?.get(0)?.task?.title}. При необходимости перенесите задача ну 5, 10 или 30 минут."))
            .addAction(android.R.drawable.btn_plus, "5 минут", pendingIntent)
            .addAction(android.R.drawable.btn_plus,"10 минут", pendingIntent)
            .addAction(android.R.drawable.btn_plus, "30 минут", pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel = NotificationChannel("appName_channel_01", "appName", NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}