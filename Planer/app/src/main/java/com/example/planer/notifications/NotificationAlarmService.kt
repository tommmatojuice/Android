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
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planer.MainActivity
import com.example.planer.database.MyDataBase
import com.example.planer.database.dao.TaskDao
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.MySharePreferences
import java.time.LocalTime

class NotificationAlarmService: Service(){
    private val TAG = "NotificationAlarmService"
    private lateinit var mySharePreferences: MySharePreferences
    private var myDataBase: MyDataBase? = null

    override fun onCreate() {
        super.onCreate()
        mySharePreferences = MySharePreferences(this)
        myDataBase = MyDataBase.getDatabase(this, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "alarm_test: NotificationAlarmService.onStartCommand()")
        if (intent != null){
            //проверка времени
            if(!mySharePreferences.getPlan().isNullOrEmpty()){
                if(LocalTime.now() > mySharePreferences.getPlan()?.get(0)?.begin){
                    sendFinishNotification(10002)
                } else {
                    sendBeginNotification(10001)
                }
            }
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
    private fun sendBeginNotification(id: Int) {
        val mySharePreferences: MySharePreferences = MySharePreferences(applicationContext)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext,
                "appName_channel_01"
        )
            .setSmallIcon(R.drawable.ic_menu_today)
            .setContentTitle("Новая задача")
            .setContentText("Через 5 минут начинается задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\"")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\". При необходимости перенесите задача ну 5, 10 или 30 минут в приложении, удерживая верхнюю задачу списка."))
//            .addAction(android.R.drawable.btn_plus, "5 минут", pendingIntent)
//            .addAction(android.R.drawable.btn_plus,"10 минут", pendingIntent)
//            .addAction(android.R.drawable.btn_plus, "30 минут", pendingIntent)
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

        /*
        * Сделать уведомления о завершении задачи, если задача завершена, то пользователь попадает в MainActivity и отнимается время,
        * если нет - время не отнимается и задача удаляется, если задача не отмечается, то задача удаляется.
        * */

        if(!mySharePreferences.getPlan().isNullOrEmpty()){
            startService(Intent(this, BootAlarmService::class.java).putExtra("index", 0))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendFinishNotification(id: Int) {
        val mySharePreferences: MySharePreferences = MySharePreferences(applicationContext)
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val notificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext,
                "appName_channel_01"
        )
                .setSmallIcon(R.drawable.ic_menu_today)
                .setContentTitle("Завершение задач")
                .setContentText("Завершена задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\"")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Завершена задача \"${mySharePreferences.getPlan()?.get(0)?.task?.title}\". Была ли задача выполнена?"))
                .addAction(R.drawable.btn_plus, "Да", pendingIntent)
                .addAction(R.drawable.btn_plus,"Нет", pendingIntent)
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

        /*
        * Сделать уведомления о завершении задачи, если задача завершена, то пользователь попадает MainActivity и отнимается время,
        * если нет - время не отнимается и задача удаляется, если задача не отмечается, то задача удаляется.
        * */

        val task = mySharePreferences.getPlan()?.get(0)?.task
        task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
        mySharePreferences.setWorkTimePast(mySharePreferences.getWorkTimePast() + mySharePreferences.getPomodoroWork())
        task?.let { myDataBase?.taskDao()?.update(it) }

        val plan = mySharePreferences.getPlan()
        plan?.removeFirst()
        mySharePreferences.setPlan(plan)
        
        if(!mySharePreferences.getPlan().isNullOrEmpty()){
            startService(Intent(this, BootAlarmService::class.java).putExtra("index", 1))
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}