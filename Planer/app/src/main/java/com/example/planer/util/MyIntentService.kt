package com.example.planer.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.example.planer.MainActivity

class MyIntentService: JobIntentService()
{
    companion object{
        fun enqueueWork(context: Context?, intent: Intent?) {
            context?.let { intent?.let { it1 -> enqueueWork(it, MyIntentService::class.java, 1, it1) } }
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d("MyTestService3", "Service running")
        sendNotification(1)
    }

    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NotifyWork.NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext,
            NotifyWork.NOTIFICATION_CHANNEL
        )
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle("Новая задача")
            .setContentText("Через 5 минут начинается задача title")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача title. При необходимости перенесите задача ну 5, 10 или 30 минут."))
            .addAction(android.R.drawable.btn_plus, "5 минут", pendingIntent)
            .addAction(android.R.drawable.btn_plus,"10 минут", pendingIntent)
            .addAction(android.R.drawable.btn_plus, "30 минут", pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NotifyWork.NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel = NotificationChannel(NotifyWork.NOTIFICATION_CHANNEL, NotifyWork.NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }
}