package com.example.planer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.planer.ui.first_come.PutName
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_food, R.id.navigation_notifications, R.id.navigation_plan, R.id.navigation_tasks, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val sharedPreferences = this?.getSharedPreferences("SP_INFO", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        if(sharedPreferences.getString("NAME", "")?.isEmpty()!!){
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frag, PutName())
                    .commit()

            findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
            findViewById<View>(R.id.nav_host_fragment).visibility = View.GONE
        }
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }
}