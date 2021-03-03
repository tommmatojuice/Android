package com.example.planer

import android.app.DownloadManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.PathViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.ui.first_come.PutName
import com.example.planer.util.MySharePreferences
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity.DOWNLOAD_SERVICE as DOWNLOAD_SERVICE1
import androidx.appcompat.app.AppCompatActivity.DOWNLOAD_SERVICE as DOWNLOAD_SERVICE1
import androidx.appcompat.app.AppCompatActivity.DOWNLOAD_SERVICE as DOWNLOAD_SERVICE1

class MainActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences:MySharePreferences
    private val SIMPLE_FRAGMENT_TAG = "myFragmentTag"
    private var myFragment: Fragment? = null

    private val groupViewModel: GroupViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val pathViewModel: PathViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        taskViewModel.insert(
//            Task("one_time", "taskTitle1", "Description1", "work", "2020-03-21",
//            30, 1,true, true, false, true, false, false,
//            true, false, null, null, null, null)
//        )
//
//        pathViewModel.insert(PathToFile("jhjfgfg", 1) )


//        groupViewModel.insert(GroupTask("groupTitle1"))
//        groupViewModel.insert(GroupTask("groupTitle2"))
//        groupViewModel.insert(GroupTask("groupTitle3"))
//        groupViewModel.insert(GroupTask("groupTitle4"))
//        groupViewModel.insert(GroupTask("groupTitle5"))
//        groupViewModel.insert(GroupTask("groupTitle6"))
//        groupViewModel.insert(GroupTask("groupTitle7"))
//        groupViewModel.insert(GroupTask("groupTitle8"))
//        groupViewModel.insert(GroupTask("groupTitle9"))
//
//        taskViewModel.insert(
//            Task("one_time", "taskTitle1", "Description1", "work", "2020-03-21",
//            30, 1,true, true, false, true, false, false,
//            true, false, null, null, null, null)
//        )
//        taskViewModel.insert(
//            Task("one_time", "taskTitle2", "Description1", "rest", "2020-03-21",
//            30, 1,true, true, false, true, false, false,
//            true, true, null, null, null, 2)
//        )
//        taskViewModel.insert(
//            Task("one_time", "taskTitle3", "Description1", "other", "2020-03-21",
//            30, 1,true, true, false, true, false, false,
//            true, false, null, null, null, 3)
//        )
//
//        taskViewModel.insert(
//            Task("fixed", "taskTitle4", "Description1", "work", null,
//            30, 1,true, true, false, true, false, false,
//            true, false, "2020-03-21", null, null, 1)
//        )
//        taskViewModel.insert(
//            Task("fixed", "taskTitle5", "Description1", "rest", null,
//            30, 1,true, true, false, true, false, false,
//            true, true, "2020-03-21", null, null, null)
//        )
//        taskViewModel.insert(
//            Task("fixed", "taskTitle6", "Description1", "other", null,
//            30, 1,true, true, false, true, false, false,
//            true, false, "2020-03-21", null, null, null)
//        )
//
//        taskViewModel.insert(
//            Task("routine", "taskTitle7", "Description1", "work", null,
//            30, 1,true, true, false, true, false, false,
//            true, false, null, "10:00", "12:00", 7)
//        )
//        taskViewModel.insert(
//            Task("routine", "taskTitle8", "Description1", "rest", null,
//            30, 1,true, true, false, true, false, false,
//            true, true, null, "10:00", "12:00", 8)
//        )
//        taskViewModel.insert(
//            Task("routine", "taskTitle9", "Description1", "other", null,
//            30, 1,true, true, false, true, false, false,
//            true, false, null, "10:00", "12:00", 9)
//        )

        groupViewModel.allGroups.observe(
                this, object: Observer<List<GroupTask>> {
                override fun onChanged(items: List<GroupTask>?) {
                    if (items != null) {
                        val groups = groupViewModel.allGroups.value
//                        ToastMessages.showMessage(applicationContext, groups?.size.toString())
                    }
                }
            }
        )

//        groupViewModel.tasksWithGroup.observe(
//                this, object: Observer<List<GroupAndAllTasks>> {
//                override fun onChanged(items: List<GroupAndAllTasks>?) {
//                    if (items != null) {
//                        val groupsAndTasks = groupViewModel.tasksWithGroup.value
//                        ToastMessages.showMessage(applicationContext, groupsAndTasks?.get(0)?.group?.title.toString())
//                    }
//                }
//            }
//        )

        mySharePreferences = MySharePreferences(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_food, R.id.navigation_notifications, R.id.navigation_plan, R.id.navigation_profile,
            R.id.navigation_tasks, R.id.tasks_types))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        initFragments(savedInstanceState)
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

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    override fun onBackPressed() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onBackPressed()
    }
}