package com.example.planer.ui.first_come

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_put_time.*
import kotlinx.android.synthetic.main.fragment_put_time.view.*

class PutTime : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view  = inflater.inflate(R.layout.fragment_put_time, container, false)

        mySharePreferences = context?.let { MySharePreferences(it) }!!

        if (savedInstanceState != null) {
            view.wakeup_time.text = savedInstanceState.getString("wakeupTime")
            view.bad_time.text = savedInstanceState.getString("badTime")
            view.breakfast_time.text = savedInstanceState.getString("breakfastTime")
            view.lunch_time.text = savedInstanceState.getString("lunchTime")
            view.diner_time.text = savedInstanceState.getString("dinerTime")
            view.breakfast_end_time.text = savedInstanceState.getString("breakfastTimeEnd")
            view.lunch_end_time.text = savedInstanceState.getString("lunchTimeEnd")
            view.diner_end_time.text = savedInstanceState.getString("dinerTimeEnd")
        }

        initButtons(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("wakeupTime", wakeup_time.text.toString())
        outState.putString("badTime", bad_time.text.toString())
        outState.putString("breakfastTime", breakfast_time.text.toString())
        outState.putString("lunchTime", lunch_time.text.toString())
        outState.putString("dinerTime", diner_time.text.toString())
        outState.putString("breakfastTimeEnd", breakfast_end_time.text.toString())
        outState.putString("lunchTimeEnd", lunch_end_time.text.toString())
        outState.putString("dinerTimeEnd", diner_end_time.text.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.info_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = Html.fromHtml("<font color=\"#F2F1EF\">" + getString(R.string.app_name) + "</font>")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.info_item -> {
                this.context?.let { InfoDialog.onCreateDialog(it, "Подсказка", "Введите время своих приемов пиши, а так же время, когда вы ложитесь спать и просыпаетесь.", R.drawable.blue_info) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun initButtons(view: View)
    {
        view.wakeup_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.wakeup_time),
                    it1
                )
            }
        }

        view.bad_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.bad_time),
                    it1
                )
            }
        }

        view.breakfast_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.breakfast_time),
                    it1
                )
            }
        }

        view.lunch_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.lunch_time),
                    it1
                )
            }
        }

        view.diner_buttom.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.diner_time),
                    it1
                )
            }
        }

        view.breakfast_end_buttom.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                        view.findViewById<TextView>(R.id.breakfast_end_time),
                        it1
                )
            }
        }

        view.lunch_end_buttom.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                        view.findViewById<TextView>(R.id.lunch_end_time),
                        it1
                )
            }
        }

        view.diner_end_buttom.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                        view.findViewById<TextView>(R.id.diner_end_time),
                        it1
                )
            }
        }

        view.next2_button.setOnClickListener {
            val checkSet: Set<String> = setOf(view.wakeup_time.text, view.bad_time.text, view.breakfast_time.text, view.lunch_time.text, view.diner_time.text) as Set<String>
            if(checkSet.size == 5){
                mySharePreferences.setWakeup(view.wakeup_time.text.toString())
                mySharePreferences.setSleep(view.bad_time.text.toString())
                mySharePreferences.setBreakfast(view.breakfast_time.text.toString())
                mySharePreferences.setLunch(view.lunch_time.text.toString())
                mySharePreferences.setDiner(view.diner_time.text.toString())
                mySharePreferences.setBreakfastEnd(view.breakfast_end_time.text.toString())
                mySharePreferences.setLunchEnd(view.lunch_end_time.text.toString())
                mySharePreferences.setDinerEnd(view.diner_end_time.text.toString())

                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_frag, WorkDays())
                    ?.commit()
            } else {
                this.context?.let { it1 -> ToastMessages.showMessage(it1, "Время не должно повторяться") }
            }
        }
    }
}