package com.example.planer.ui.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.pom_25
import kotlinx.android.synthetic.main.fragment_profile.view.pom_30
import kotlinx.android.synthetic.main.fragment_profile.view.pom_40
import kotlinx.android.synthetic.main.fragment_profile.view.pom_50
import kotlinx.android.synthetic.main.fragment_profile.view.pom_60
import kotlinx.android.synthetic.main.fragment_profile.view.radioGroup
import kotlinx.android.synthetic.main.fragment_put_pomodoro.view.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var mySharePreferences: MySharePreferences
    private var pomodoroWork = 0

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        mySharePreferences = this.context?.let { MySharePreferences(it) }!!

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

//        val textView: TextView = root.findViewById(R.id.profile_text)
//        profileViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        initUI()
        initButtons(view)
        initElements(view)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.save_item -> {
                saveSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveSettings()
    {
        mySharePreferences.setName(view?.editText?.text.toString())
        view?.autoPlan?.isChecked?.let { mySharePreferences.setAutoPlan(it) }
        mySharePreferences.setWakeup(view?.wake_up_time?.text.toString())
        mySharePreferences.setSleep(view?.sleep_time?.text.toString())
        mySharePreferences.setBreakfast(view?.breakfast_begin_time?.text.toString())
        mySharePreferences.setBreakfastEnd(view?.breakfast_end_time?.text.toString())
        mySharePreferences.setLunch(view?.lunch_begin_time?.text.toString())
        mySharePreferences.setLunchEnd(view?.lunch_end_time?.text.toString())
        mySharePreferences.setDiner(view?.diner_begin_time?.text.toString())
        mySharePreferences.setDinerEnd(view?.diner_end_time?.text.toString())
        mySharePreferences.setPeakBegin(view?.peak_begin_time?.text.toString())
        mySharePreferences.setPeakEnd(view?.peak_end_time?.text.toString())
        mySharePreferences.setMondayWork(view?.work_mon_time?.text.toString())
        mySharePreferences.setMondayBegin(view?.begin_mon_time?.text.toString())
        mySharePreferences.setTuesdayWork(view?.work_tue_time?.text.toString())
        mySharePreferences.setTuesdayBegin(view?.begin_tue_time?.text.toString())
        mySharePreferences.setWednesdayWork(view?.work_wed_time?.text.toString())
        mySharePreferences.setWednesdayBegin(view?.begin_wed_time?.text.toString())
        mySharePreferences.setThursdayWork(view?.work_thur_time?.text.toString())
        mySharePreferences.setThursdayBegin(view?.begin_thur_time?.text.toString())
        mySharePreferences.setFridayWork(view?.work_fri_time?.text.toString())
        mySharePreferences.setFridayBegin(view?.begin_fri_time?.text.toString())
        mySharePreferences.setSaturdayWork(view?.work_sat_time?.text.toString())
        mySharePreferences.setSaturdayBegin(view?.begin_sat_time?.text.toString())
        mySharePreferences.setSundayWork(view?.work_sun_time?.text.toString())
        mySharePreferences.setSundayBegin(view?.begin_sun_time?.text.toString())
        view?.bigBreak?.isChecked?.let { mySharePreferences.setPomodoroBigBreakF(it) }

        mySharePreferences.setPomodoroWork(pomodoroWork)
        when(pomodoroWork){
            25 -> {
                mySharePreferences.setPomodoroBreak(5)
                mySharePreferences.setPomodoroBigBreak(10)
            }
            30 -> {
                mySharePreferences.setPomodoroBreak(5)
                mySharePreferences.setPomodoroBigBreak(10)
            }
            40 -> {
                mySharePreferences.setPomodoroBreak(10)
                mySharePreferences.setPomodoroBigBreak(20)
            }
            50 -> {
                mySharePreferences.setPomodoroBreak(10)
                mySharePreferences.setPomodoroBigBreak(30)
            }
            60 -> {
                mySharePreferences.setPomodoroBreak(10)
                mySharePreferences.setPomodoroBigBreak(30)
            }
        }
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_orange) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F27935")))
        (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Профиль" + "</font>"))
    }

    private fun initButtons(view: View)
    {
        view.radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            view.findViewById<RadioButton>(checkedId)?.apply {
                pomodoroWork = text.toString().toInt()
            }
        }

        view.wake_up_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.wake_up_time,
                    time
                )
            }
        }
        view.sleep_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.sleep_time,
                    time
                )
            }
        }
        view.breakfast_begin_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.breakfast_begin_time,
                    time
                )
            }
        }
        view.breakfast_end_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.breakfast_end_time,
                    time
                )
            }
        }
        view.lunch_begin_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.lunch_begin_time,
                    time
                )
            }
        }
        view.lunch_end_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.lunch_end_time,
                    time
                )
            }
        }
        view.diner_begin_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.diner_begin_time,
                    time
                )
            }
        }
        view.diner_end_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.diner_end_time,
                    time
                )
            }
        }
        view.peak_begin_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.peak_begin_time,
                    time
                )
            }
        }
        view.peak_end_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.peak_end_time,
                    time
                )
            }
        }
        view.work_mon_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.work_mon_time,
                    time
                )
            }
        }
        view.begin_mon_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.begin_mon_time,
                    time
                )
            }
        }
        view.work_tue_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.work_tue_time,
                    time
                )
            }
        }
        view.begin_tue_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.begin_tue_time,
                    time
                )
            }
        }
        view.work_wed_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.work_wed_time,
                    time
                )
            }
        }
        view.begin_wed_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.begin_wed_time,
                    time
                )
            }
        }
        view.work_thur_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.work_thur_time,
                    time
                )
            }
        }
        view.begin_thur_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.begin_thur_time,
                    time
                )
            }
        }
        view.work_fri_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.work_fri_time,
                    time
                )
            }
        }
        view.begin_fri_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.begin_fri_time,
                    time
                )
            }
        }
        view.work_sat_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.work_sat_time,
                    time
                )
            }
        }
        view.begin_sat_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.begin_sat_time,
                    time
                )
            }
        }
        view.work_sun_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.work_sun_time,
                    time
                )
            }
        }
        view.begin_sun_time.setOnClickListener {
            this.context?.let { time ->
                TimeDialog.getTime(
                    view.begin_sun_time,
                    time
                )
            }
        }
    }

    private fun initElements(view: View)
    {
        view.editText.setText(mySharePreferences.getName())
        view.autoPlan.isChecked = mySharePreferences.getAutoPlan()
        view.wake_up_time.text = mySharePreferences.getWakeup()
        view.sleep_time.text = mySharePreferences.getSleep()
        view.breakfast_begin_time.text = mySharePreferences.getBreakfast()
        view.breakfast_end_time.text = mySharePreferences.getBreakfastEnd()
        view.lunch_begin_time.text = mySharePreferences.getLunch()
        view.lunch_end_time.text = mySharePreferences.getLunchEnd()
        view.diner_begin_time.text = mySharePreferences.getDiner()
        view.diner_end_time.text = mySharePreferences.getDinerEnd()
        view.peak_begin_time.text = mySharePreferences.getPeakBegin()
        view.peak_end_time.text = mySharePreferences.getPeakEnd()
        view.work_mon_time.text = mySharePreferences.getMondayWork()
        view.begin_mon_time.text = mySharePreferences.getMondayBegin()
        view.work_tue_time.text = mySharePreferences.getTuesdayWork()
        view.begin_tue_time.text = mySharePreferences.getTuesdayBegin()
        view.work_wed_time.text = mySharePreferences.getWednesdayWork()
        view.begin_wed_time.text = mySharePreferences.getWednesdayBegin()
        view.work_thur_time.text = mySharePreferences.getThursdayWork()
        view.begin_thur_time.text = mySharePreferences.getThursdayBegin()
        view.work_fri_time.text = mySharePreferences.getFridayWork()
        view.begin_fri_time.text = mySharePreferences.getFridayBegin()
        view.work_sat_time.text = mySharePreferences.getSaturdayWork()
        view.begin_sat_time.text = mySharePreferences.getSaturdayBegin()
        view.work_sun_time.text = mySharePreferences.getSundayWork()
        view.begin_sun_time.text = mySharePreferences.getSundayBegin()
        view.bigBreak.isChecked = mySharePreferences.getPomodoroBigBreakF()

        when(mySharePreferences.getPomodoroWork()){
            25 -> view.pom_25.isChecked = true
            30 -> view.pom_30.isChecked = true
            40 -> view.pom_40.isChecked = true
            50 -> view.pom_50.isChecked = true
            60 -> view.pom_60.isChecked = true
        }
    }
}