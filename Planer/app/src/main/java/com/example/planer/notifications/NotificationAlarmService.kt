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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.planer.MainActivity
import com.example.planer.database.MyDataBase
import com.example.planer.util.MySharePreferences
import java.time.LocalTime

class NotificationAlarmService: Service()
{
    private val TAG = "NotificationAlarmService"
    private lateinit var mySharePreferences: MySharePreferences
    private var myDataBase: MyDataBase? = null

    override fun onCreate() {
        super.onCreate()
        mySharePreferences = MySharePreferences(this)
        myDataBase = MyDataBase.getSimpleDatabase(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "alarm_test: NotificationAlarmService.onStartCommand()")
        if (intent != null){
            if(!mySharePreferences.getPlan().isNullOrEmpty()){
                if(LocalTime.now() > mySharePreferences.getPlan()?.get(0)?.begin){
                    sendNotification("Завершение задачи",
                            "Завершена задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\"",
                            "Завершена задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\".",
                            1
                    )
                } else {
                    sendNotification("Новая задача",
                            "Через 2 минут начинается задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\"",
                            "Через 2 минуты начинается задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\". При необходимости перенесите задача ну 5, 10 или 30 минут в приложении, удерживая верхнюю задачу списка.",
                            0
                    )
                }
            }
        }
        else Log.d(TAG, "Intent was null in NotificationAlarmService.")
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(title: String, smallText: String, bigText: String, index: Int) {
        val mySharePreferences = MySharePreferences(applicationContext)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext,
                "appName_channel_01"
        )
            .setSmallIcon(R.drawable.ic_menu_today)
            .setContentTitle(title)
            .setContentText(smallText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
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

        notificationManager.notify(10001, notification.build())

        if(LocalTime.now() >= mySharePreferences.getPlan()?.get(0)?.end
                && mySharePreferences.getPlan()?.first()?.task?.type == "one_time"
                && mySharePreferences.getPlan()?.first()!!.task?.category == "work"){
            val task = mySharePreferences.getPlan()?.get(0)?.task
            task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
            mySharePreferences.setWorkTimePast(mySharePreferences.getWorkTimePast() + mySharePreferences.getPomodoroWork() + mySharePreferences.getPomodoroBreak())

            Thread {
                kotlin.run {
                    task?.let { myDataBase?.taskDao()?.update(it) }
                }
            }.start()

            val plan = mySharePreferences.getPlan()
            plan?.removeFirst()
            mySharePreferences.setPlan(plan)
        }

        if(!mySharePreferences.getPlan().isNullOrEmpty() && mySharePreferences.getAutoPlan()){
            startService(Intent(this, BootAlarmService::class.java).putExtra("index", index))
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}