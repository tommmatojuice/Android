package com.example.planer.ui.plan

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.notifications.BootAlarmService
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_plan.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlanFragment : Fragment()
{
    private var days: Array<String?> = arrayOfNulls<String>(7)
    @SuppressLint("SimpleDateFormat")
    private val format: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    private lateinit var mySharePreferences: MySharePreferences

    @SuppressLint("UseRequireInsteadOfGet")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)
        mySharePreferences = this.context?.let { MySharePreferences(it) }!!

        initUI()
        initWeek(view)
        initDays(view)
        initTitle(days[initToday(view)].toString(), view)

        if(mySharePreferences.getAutoPlan()){
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.plan_frag, AutoPlan(LocalDate.now().toString(), LocalDate.now().dayOfWeek.toString()))
                    ?.commit()
        } else {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.plan_frag, UserPlan(LocalDate.now().toString(), LocalDate.now().dayOfWeek.toString()))
                    ?.commit()
        }

        if(!mySharePreferences.getPlan().isNullOrEmpty() && mySharePreferences.getAutoPlan()){
            if(LocalTime.now() > mySharePreferences.getPlan()?.get(0)?.begin
                || (LocalTime.now() == mySharePreferences.getPlan()?.get(0)?.begin && LocalTime.now().second > 0)){
                requireContext().startService(Intent(context, BootAlarmService::class.java).putExtra("index", 0))
            } else requireContext().startService(Intent(context, BootAlarmService::class.java).putExtra("index", 1))
        }

        return view
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_blue) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2574A9")))
    }

    private fun initWeek(view: View){
        val calendar: Calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        for (i in 0..6) {
            days[i] = format.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        view.day_mon.text = days[0]?.substring(8, 10)
        view.day_tue.text = days[1]?.substring(8, 10)
        view.day_wen.text = days[2]?.substring(8, 10)
        view.day_thu.text = days[3]?.substring(8, 10)
        view.day_fri.text = days[4]?.substring(8, 10)
        view.day_sat.text = days[5]?.substring(8, 10)
        view.day_sun.text = days[6]?.substring(8, 10)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDays(view: View){
        view.day_mon.setOnClickListener {
            initEachButton(0, view.text_mon, view.day_mon, view)
        }
        view.day_tue.setOnClickListener {
            initEachButton(1, view.text_tue, view.day_tue, view)
        }
        view.day_wen.setOnClickListener {
            initEachButton(2, view.text_wen, view.day_wen, view)
        }
        view.day_thu.setOnClickListener {
            initEachButton(3, view.text_thu, view.day_thu, view)
        }
        view.day_fri.setOnClickListener {
            initEachButton(4, view.text_fri, view.day_fri, view)
        }
        view.day_sat.setOnClickListener {
            initEachButton(5, view.text_sat, view.day_sat, view)
        }
        view.day_sun.setOnClickListener {
            initEachButton(6, view.text_sun, view.day_sun, view)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private fun initToday(view: View): Int{
        val calendar = Calendar.getInstance()
        var day = 0

        when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> {
                view.text_mon.setTextColor(resources.getColor(R.color.dark_blue))
                view.day_mon.setTextColor(resources.getColor(R.color.dark_blue))
            }
            Calendar.TUESDAY -> {
                view.text_tue.setTextColor(resources.getColor(R.color.dark_blue))
                view.day_tue.setTextColor(resources.getColor(R.color.dark_blue))
                day = 1
            }
            Calendar.WEDNESDAY -> {
                view.text_wen.setTextColor(resources.getColor(R.color.dark_blue))
                view.day_wen.setTextColor(resources.getColor(R.color.dark_blue))
                day = 2
            }
            Calendar.THURSDAY -> {
                view.text_thu.setTextColor(resources.getColor(R.color.dark_blue))
                view.day_thu.setTextColor(resources.getColor(R.color.dark_blue))
                day = 3
            }
            Calendar.FRIDAY -> {
                view.text_fri.setTextColor(resources.getColor(R.color.dark_blue))
                view.day_fri.setTextColor(resources.getColor(R.color.dark_blue))
                day = 4
            }
            Calendar.SATURDAY -> {
                view.text_sat.setTextColor(resources.getColor(R.color.dark_blue))
                view.day_sat.setTextColor(resources.getColor(R.color.dark_blue))
                day = 5
            }
            Calendar.SUNDAY -> {
                view.text_sun.setTextColor(resources.getColor(R.color.dark_blue))
                view.day_sun.setTextColor(resources.getColor(R.color.dark_blue))
                day = 6
            }
        }
        return day
    }

    private fun deactivateAll(view: View){
        view.text_mon.setTextColor(resources.getColor(R.color.text))
        view.day_mon.setTextColor(resources.getColor(R.color.text))
        view.text_tue.setTextColor(resources.getColor(R.color.text))
        view.day_tue.setTextColor(resources.getColor(R.color.text))
        view.text_wen.setTextColor(resources.getColor(R.color.text))
        view.day_wen.setTextColor(resources.getColor(R.color.text))
        view.text_thu.setTextColor(resources.getColor(R.color.text))
        view.day_thu.setTextColor(resources.getColor(R.color.text))
        view.text_fri.setTextColor(resources.getColor(R.color.text))
        view.day_fri.setTextColor(resources.getColor(R.color.text))
        view.text_sat.setTextColor(resources.getColor(R.color.text))
        view.day_sat.setTextColor(resources.getColor(R.color.text))
        view.text_sun.setTextColor(resources.getColor(R.color.text))
        view.day_sun.setTextColor(resources.getColor(R.color.text))
    }

    private fun initTitle(day: String, view: View){
        val date = format.parse(day)
        val title = DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale("ru")).format(date)
        view.text_home.text = title
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initEachButton(dayNumber: Int, text: TextView, day: TextView, view: View){
        deactivateAll(view)
        text.setTextColor(resources.getColor(R.color.red))
        day.setTextColor(resources.getColor(R.color.red))
        initTitle(days[dayNumber].toString(), view)
        initToday(view)
        if(mySharePreferences.getAutoPlan()){
            if(initToday(view) > dayNumber){
                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.plan_frag, PastDaysFragment())
                        ?.commit()
            } else {
                this.context?.let {
                    activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.plan_frag, AutoPlan(days[dayNumber].toString(), LocalDate.parse(days[dayNumber], DateTimeFormatter.ofPattern("yyyy-MM-dd")).dayOfWeek.toString()))
                            ?.commit()
                }
            }
        } else {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.plan_frag, UserPlan(days[dayNumber].toString(), LocalDate.parse(days[dayNumber], DateTimeFormatter.ofPattern("yyyy-MM-dd")).dayOfWeek.toString()))
                    ?.commit()
        }
    }
}