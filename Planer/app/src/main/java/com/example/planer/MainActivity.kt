package com.example.planer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.planer.ui.first_come.PutName
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
{
    private lateinit var mySharePreferences:MySharePreferences
    private val SIMPLE_FRAGMENT_TAG = "myFragmentTag"
    private var myFragment: Fragment? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mySharePreferences = MySharePreferences(this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_food, R.id.navigation_notifications, R.id.navigation_plan, R.id.navigation_tasks, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
}