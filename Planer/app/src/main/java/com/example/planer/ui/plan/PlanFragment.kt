package com.example.planer.ui.plan

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.planer.R
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_plan.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PlanFragment : Fragment()
{
    private var days: Array<String?> = arrayOfNulls<String>(7)
    private val format: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        initUI()
        initWeek(view)
        initDays(view)
        initTitle(days[initToday(view)].toString(), view)

        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.plan_frag, AutoPlan())
                ?.commit()


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

    private fun initEachButton(dayNumber: Int, text: TextView, day: TextView, view: View){
        deactivateAll(view)
        text.setTextColor(resources.getColor(R.color.red))
        day.setTextColor(resources.getColor(R.color.red))
        initTitle(days[dayNumber].toString(), view)
        initToday(view)
        if(initToday(view) > dayNumber){
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.plan_frag, PastDaysFragment())
                    ?.commit()
        } else {
//            activity?.supportFragmentManager
//                    ?.beginTransaction()
//                    ?.replace(R.id.main_frag, AutoPlan())
//                    ?.commit()
            //план на день

            //        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//        try {
//            val date: Date = format.parse(dtStart)
//            System.out.println(date)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
        }
    }


}