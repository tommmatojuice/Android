package com.example.planer

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
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.PathViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.ui.first_come.PutName
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate


class MainActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences:MySharePreferences
    private val SIMPLE_FRAGMENT_TAG = "myFragmentTag"
    private var myFragment: Fragment? = null

    private val groupViewModel: GroupViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private val pathViewModel: PathViewModel by viewModels()


    private var fixedTasks: List<Task>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        taskViewModel.fixedTasksByDate(LocalDate.now().toString()).observe(
//                this, { fixedTasks ->
//            if (fixedTasks != null) {
//                this.fixedTasks = fixedTasks
//                Log.d("fixedTaskSize", fixedTasks.size.toString())
//            }
//        }
//        )


        taskViewModel.allTasks.observe(
                this, { tasks ->
            if (tasks != null) {
//                this.fixedTasks = tasks

                Log.d("all_in", tasks!!.size.toString())
                set(tasks)
            }
        }
        )
        Log.d("fixedTaskSize2", fixedTasks?.size.toString())

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

        initFragments(savedInstanceState)
    }

    private fun set(f: List<Task>){
        this.fixedTasks = f
    }

    private fun initFragments(savedInstanceState: Bundle?)
    {
        Log.d("fixedTaskSize2", fixedTasks?.size.toString())
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