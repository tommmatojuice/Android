package com.example.planer

import android.R.attr.key
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.PathViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.ui.first_come.PutName
import com.example.planer.util.MyAlarmReceiver
import com.example.planer.util.MyService
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences: MySharePreferences
    private val SIMPLE_FRAGMENT_TAG = "myFragmentTag"
    private val TAG = "MainActivity"
    private var myFragment: Fragment? = null

    private val groupViewModel: GroupViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val pathViewModel: PathViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mySharePreferences = MySharePreferences(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_food,
                        R.id.navigation_notifications,
                        R.id.navigation_plan,
                        R.id.navigation_profile,
                        R.id.navigation_tasks,
                        R.id.tasks_types
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, AlertReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this,1,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//        }
//        alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//        )

        Log.d("MyTestService1", "Service running");
//
//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, MyAlarmReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 18)
//            set(Calendar.MINUTE, 51)
//            set(Calendar.SECOND, 0)
//        }
//        intent.putExtra("TITLE", calendar.timeInMillis.toString())
//        alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 28)
            set(Calendar.SECOND, 40)
        }

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                sender
        )

//        val intent = Intent(this, MyAlarmReceiver::class.java)
//        val pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val firstMillis = System.currentTimeMillis() // alarm is set right away
//        val alarm = getSystemService(ALARM_SERVICE) as AlarmManager
//        alarm.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                firstMillis,
//                pIntent
//        )
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
}