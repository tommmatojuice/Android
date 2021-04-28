package com.example.planer.notifications

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.planer.MainActivity
import com.example.planer.util.MySharePreferences
import com.example.planer.util.NotifyWork

class MyNotificationPublisher: BroadcastReceiver()
{
    companion object
    {
        var NOTIFICATION_ID = "notification_id"
        var NOTIFICATION = "notification"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MyNotificationPublisher", "in")
//        val notificationsUtil = NotificationsUtil(context)
//        notificationsUtil.showNotification(1)

        sendNotification(0, context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(id: Int, context: Context) {
        val mySharePreferences: MySharePreferences = MySharePreferences(context)
        var title = mySharePreferences.getPlan()?.get(0)?.task?.title.toString()
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NotifyWork.NOTIFICATION_ID, id)

        val notificationManager =
                context.getSystemService(JobIntentService.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = NotificationCompat.Builder(context,
                NotifyWork.NOTIFICATION_CHANNEL
        )
                .setSmallIcon(android.R.drawable.ic_menu_today)
                .setContentTitle("Новая задача")
                .setContentText("Через 5 минут начинается задача \"$title\".")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача \"$title\". При необходимости перенесите задача ну 5, 10 или 30 минут."))
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