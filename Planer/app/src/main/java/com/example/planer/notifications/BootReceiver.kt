package com.example.planer.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.planer.MainActivity
import com.example.planer.util.MySharePreferences

class BootReceiver: BroadcastReceiver()
{
    private val TAG = "BootReceiver"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, bootintent: Intent?) {
        context?.startService(Intent(context, BootAlarmService::class.java))
//        Log.d(TAG, "alarm_test: NotificationAlarmService.onStartCommand()")
//        if (bootintent != null){
//            //            createNotification()
//            context?.let { sendNotification(10001, it) }
//        }
//        else
//            Toast.makeText(context, "Intent was null in NotificationAlarmService.", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(id: Int, context: Context) {
        val mySharePreferences: MySharePreferences = MySharePreferences(context)
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val notificationManager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notification = NotificationCompat.Builder(
            context,
            "appName_channel_01"
        )
                .setSmallIcon(android.R.drawable.ic_menu_today)
                .setContentTitle("Новая задача")
                .setContentText("Через 5 минут начинается задача title")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Через 5 минут начинается задача ... . При необходимости перенесите задача ну 5, 10 или 30 минут.")
                )
//            .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача ${mySharePreferences.getPlan()?.get(0)?.task?.title}. При необходимости перенесите задача ну 5, 10 или 30 минут."))
                .addAction(android.R.drawable.btn_plus, "5 минут", pendingIntent)
                .addAction(android.R.drawable.btn_plus, "10 минут", pendingIntent)
                .addAction(android.R.drawable.btn_plus, "30 минут", pendingIntent)
                .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel = NotificationChannel(
                "appName_channel_01",
                "appName",
                NotificationManager.IMPORTANCE_HIGH
            )

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