package com.example.planer

import android.R.attr.key
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.PathViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.notifications.MyNotificationPublisher
import com.example.planer.ui.first_come.PutName
import com.example.planer.ui.plan.TasksForPlan
import com.example.planer.util.*
import com.example.planer.util.NotifyWork.Companion.NOTIFICATION_WORK
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.nio.charset.CodingErrorAction.REPLACE
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class MainActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences: MySharePreferences
    private val SIMPLE_FRAGMENT_TAG = "myFragmentTag"
    private val TAG = "MainActivity"
    private var myFragment: Fragment? = null
    val CHANNEL_ID = "ForegroundServiceChannel"

    private val groupViewModel: GroupViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val pathViewModel: PathViewModel by viewModels()

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun scheduleNotification1() {
            Log.d("mvm", "scheduleNotification running")

            val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
                .setInitialDelay(30, TimeUnit.SECONDS)
                .build()

            val instanceWorkManager = WorkManager.getInstance()
            instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
        }
    }

    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mySharePreferences = MySharePreferences(this)
//        mySharePreferences.getSleep()?.let { mySharePreferences.setWorkEnd(it) }

//        mySharePreferences.setAllInfo(false)
//        mySharePreferences.setPlan(null)
//        mySharePreferences.setWorkTimePast(0)

        minusTime()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_food,
                        R.id.navigation_profile,
                        R.id.navigation_plan,
                        R.id.navigation_tasks,
                        R.id.tasks_types
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        Log.d("MyTestService1", "Service running")

        initFragments(savedInstanceState)

//        scheduleNotification()

//        if(!mySharePreferences.getPlan().isNullOrEmpty()){
//            val firstTaskBegin = mySharePreferences.getPlan()?.get(0)?.begin?.minusMinutes(5)
//            val timeNow = LocalTime.now()
//            Log.d("MyNotificationPublisher", firstTaskBegin.toString())
//            Log.d("MyNotificationPublisher", timeNow.toString())
//            if (firstTaskBegin != null) {
//                if(firstTaskBegin > timeNow){
//                    val difference = firstTaskBegin.minusHours(timeNow.hour.toLong())?.minusMinutes(timeNow.minute.toLong())
//                    val delay = ((difference?.hour?.times(60) ?: 0) + (difference?.minute ?: 0)) * 60000
//                    Log.d("MyNotificationPublisher", delay.toString())
//                    scheduleNotification2(this, delay.toLong(), 0)
//                }
//            }
//        }

        scheduleNotification2(this, 5000, 0)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE, 35)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                1000*15,
                sender
        )
    }

    private fun scheduleNotification2(context: Context, delay: Long, notificationId: Int) {//delay is after how much time(in millis) from current time you want to schedule the notification
        Log.d("MyNotificationPublisher", "schedule")
        val builder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_today)
                    .setContentTitle("Новая задача")
                    .setContentText("Через 5 минут начинается задача \"$title\"")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Через 5 минут начинается задача \"$title\". При необходимости перенесите задача ну 5, 10 или 30 минут."))
                    .setAutoCancel(true)
        }
        val intent = Intent(context, MainActivity::class.java)
        val activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        builder?.setContentIntent(activity)
        val notification: Notification? = builder?.build()
        val notificationIntent = Intent(context, MyNotificationPublisher::class.java)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION_ID, notificationId)
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }

    private fun initFragments(savedInstanceState: Bundle?)
    {
        if(!mySharePreferences.getAllInfo()) {
            if (savedInstanceState != null) {
                myFragment = supportFragmentManager.findFragmentByTag(SIMPLE_FRAGMENT_TAG)
            } else {
                myFragment = PutName()
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_frag, myFragment as PutName, SIMPLE_FRAGMENT_TAG)
                        .commit()
            }
            findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
            findViewById<View>(R.id.nav_host_fragment).visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        myFragment?.let { supportFragmentManager.putFragment(outState, SIMPLE_FRAGMENT_TAG, it) }
    }

    private fun scheduleJob()
    {
        val componentName = ComponentName(this, MyService::class.java)
        val info = JobInfo.Builder(123, componentName)
            .setRequiresCharging(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setPersisted(true)
            .setPeriodic(15 * 60 * 1000.toLong())
            .build()
        val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled")
        } else {
            Log.d(TAG, "Job scheduling failed")
        }
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onBackPressed() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun minusTime()
    {
        Log.d("minusTime", "minusTime")
        var pomodoros: MutableList<TasksForPlan>? = mutableListOf()
        pomodoros = mySharePreferences.getPlan()

        if(LocalDate.now().toString() != mySharePreferences.getToday()){
            mySharePreferences.setWorkTimePast(0)
            mySharePreferences.getFirstTasksNext()?.let { mySharePreferences.setFirstTasksToday(it) }
            mySharePreferences.setWorkEnd(null)
        }

        if(!pomodoros.isNullOrEmpty()){
            if(pomodoros.last().task?.type == "one_time" && pomodoros.last().task?.category != "work"){
                //no
            } else {
                mySharePreferences.setWorkEnd(pomodoros.last().end.toString())
                pomodoros.forEach {
                    Log.d("pomodorosFromMain", "${it.begin}-${it.end}: ${it.task?.title}")
                    if((it.end < LocalTime.now() && it.begin < LocalTime.now() && it.task?.type == "one_time") || LocalDate.now().toString() != mySharePreferences.getToday()){
                        Log.d("pomodorosFromMainMT", "${it.begin}-${it.end}: ${it.task?.title}")
                        val task = it.task
                        task?.duration = task?.duration?.minus(mySharePreferences.getPomodoroWork())
                        mySharePreferences.setWorkTimePast(mySharePreferences.getWorkTimePast()+mySharePreferences.getPomodoroWork())
                        task?.let { it1 -> taskViewModel.update(it1) }
                    }
                }
            }
        } else mySharePreferences.getSleep()?.let { mySharePreferences.setWorkEnd(it) }

        if(LocalDate.now().toString() != mySharePreferences.getToday()){
            mySharePreferences.setWorkTimePast(0)
        }

        pomodoros?.removeIf { it.end < LocalTime.now() && it.begin != it.end }
        mySharePreferences.setPlan(pomodoros)
    }
}