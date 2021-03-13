package com.example.planer.util

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.planer.MainActivity
import java.util.*


class MyService : Service()
{
    val CHANNEL_ID = "ForegroundServiceChannel"
    private val TAG = "ExampleJobService"
    private val NOTIFY_ID = 101

    private lateinit var mySharePreferences: MySharePreferences

    private var context: Context? = null

    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        this.context = applicationContext
        mySharePreferences = MySharePreferences(applicationContext)

        val pomodoros = mySharePreferences.getPlan()

        val calendar: Calendar = Calendar.getInstance()

        pomodoros?.forEach {
            val time = it.begin.minusMinutes(5)

            calendar.apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, time.hour)
                set(Calendar.MINUTE, time.minute)
                set(Calendar.SECOND, 0)
            }

            Log.d("pomodorosNotifHere", time.toString())

            setNotifications(it.task?.title, calendar)
        }

        calendar.apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 57)
            set(Calendar.SECOND, 0)
        }

        setNotifications("лаба", calendar)

        createNotificationChannel()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                    NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotifications(title: String?, calendar: Calendar){
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
                    .setWhen(calendar.timeInMillis)
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача \"$title\". При необходимости перенесите задача ну 5, 10 или 30 минут."))
                    .addAction(R.drawable.btn_plus, "5 минут", pendingIntent)
                    .addAction(R.drawable.btn_plus, "10 минут", pendingIntent)
                    .addAction(R.drawable.btn_plus, "30 минут", pendingIntent)
                    .setAutoCancel(true)
                    .build()
        }

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        builder?.let { notificationManager?.notify(NOTIFY_ID, it) }

        startForeground(1, builder)
    }
}
//class MyService : JobIntentService() {
//    private var jobCancelled = false
//
//    companion object {
//        private const val TAG = "ExampleJobService"
//
//        fun enqueueWork(context: Context?, intent: Intent?) {
//            if (intent != null && context != null) {
//                enqueueWork(context, MyService::class.java, 1, intent)
//            }
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun makePlan() {
//        Thread(Runnable {
//            while (true) {
//                if (LocalTime.now().hour == 14 && LocalTime.now().minute == 39) {
//                    Log.d(TAG, "run: Hello!")
//                }
//                Log.d(TAG, "run: ${LocalTime.now()}")
//                if (jobCancelled) {
//                    return@Runnable
//                }
//                try {
//                    Thread.sleep(1000)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }
//        }).start()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onHandleWork(intent: Intent) {
//        Log.d(TAG, "Start Service!")
//        makePlan()
//    }
//}

/*private var jobCancelled = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job started")
        makePlan(params)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makePlan(params: JobParameters) {
        Thread(Runnable {
            while(true) {
                if(LocalTime.now().hour == 14 && LocalTime.now().minute == 39){
                    Log.d(TAG, "run: Hello!")
                }
                Log.d(TAG, "run: ${LocalTime.now()}")
                if (jobCancelled) {
                    return@Runnable
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
//            Log.d(TAG, "Job finished")
//            jobFinished(params, false)
        }).start()
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true
    }

    companion object {
        private const val TAG = "ExampleJobService"
    }

    override fun onHandleWork(intent: Intent) {
    }*/