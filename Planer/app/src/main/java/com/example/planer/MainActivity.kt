package com.example.planer

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.planer.ui.first_come.PutName
import com.example.planer.util.InfoDialog
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_food, R.id.navigation_notifications, R.id.navigation_plan, R.id.navigation_tasks, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        navView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_food -> {
//                    navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.red)
//                    navView.itemTextColor = ContextCompat.getColorStateList(this, R.color.red)
//                }
//                R.id.navigation_notifications -> {
//                    navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.yellow)
//                    navView.itemTextColor = ContextCompat.getColorStateList(this, R.color.yellow)
//                }
//                R.id.navigation_plan -> {
//                    navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.dark_blue)
//                    navView.itemTextColor = ContextCompat.getColorStateList(this, R.color.dark_blue)
//                }
//                R.id.navigation_tasks -> {
//                    navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.dark_green)
//                    navView.itemTextColor = ContextCompat.getColorStateList(this, R.color.dark_green)
//                }
//                R.id.navigation_profile -> {
//                    navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.dark_orange)
//                    navView.itemTextColor = ContextCompat.getColorStateList(this, R.color.dark_orange)
//                }
//            }
//            true
//        }

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

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
////        val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
//        return when (item.itemId) {
//            R.id.navigation_food -> {
////                navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.red)
//                ToastMessages.showMessage(this, "111")
//                true
//            }
//            R.id.navigation_notifications -> {
////                navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.yellow)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
////      navView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.red)
//
//        return when (item.itemId) {
//            R.id.navigation_food -> {
//                ToastMessages.showMessage(applicationContext, "111")
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}