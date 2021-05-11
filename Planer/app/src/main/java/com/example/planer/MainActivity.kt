package com.example.planer

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
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.notifications.BootAlarmService
import com.example.planer.notifications.NotificationAlarmService
import com.example.planer.ui.first_come.PutName
import com.example.planer.ui.plan.TasksForPlan
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences: MySharePreferences
    private val SIMPLE_FRAGMENT_TAG = "myFragmentTag"
    private val TAG = "MainActivity"
    private var myFragment: Fragment? = null
    val CHANNEL_ID = "ForegroundServiceChannel"

    private val taskViewModel: TaskViewModel by viewModels()

    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mySharePreferences = MySharePreferences(this)
//        mySharePreferences.getSleep()?.let { mySharePreferences.setWorkEnd(it) }

//        mySharePreferences.setAllInfo(true)
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

        initFragments(savedInstanceState)

//        startService(Intent(this, BootAlarmService::class.java).putExtra("index", 0))
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
            mySharePreferences.setTaskTransfer(0)
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
                        mySharePreferences.setWorkTimePast(mySharePreferences.getWorkTimePast() + mySharePreferences.getPomodoroWork())
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