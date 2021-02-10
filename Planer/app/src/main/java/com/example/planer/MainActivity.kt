package com.example.planer

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.planer.ui.first_come.PutName
import com.example.planer.ui.food.FoodFragment
import com.example.planer.ui.food.FoodViewModel
import com.example.planer.ui.notifications.NotificationsFragment
import com.example.planer.ui.plan.PlanFragment
import com.example.planer.ui.profile.ProfileFragment
import com.example.planer.ui.tasks.TasksFragment
import com.example.planer.ui.tasks.TasksTypesFragment
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences:MySharePreferences
    private val SIMPLE_FRAGMENT_TAG = "myFragmentTag"
    private var myFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mySharePreferences = MySharePreferences(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null

//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.main_frag, PlanFragment())
//            .commit()


//
//        navView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_food -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.main_frag, FoodFragment())
//                        .commit()
//                }
//                R.id.navigation_notifications -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.main_frag, NotificationsFragment())
//                        .commit()
//                }
//                R.id.navigation_plan -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.main_frag, PlanFragment())
//                        .commit()
//                }
//                R.id.navigation_tasks -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.main_frag, TasksFragment())
//                        .commit()
//                }
//                R.id.navigation_profile -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.main_frag, ProfileFragment())
//                        .commit()
//                }
//            }
//            true
//        }

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
//            findViewById<View>(R.id.nav_host_fragment).visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        myFragment?.let { supportFragmentManager.putFragment(outState, SIMPLE_FRAGMENT_TAG, it) }
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }
}