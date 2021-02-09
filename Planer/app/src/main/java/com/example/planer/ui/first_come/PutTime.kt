package com.example.planer.ui.first_come

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog

class PutTime : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view  = inflater.inflate(R.layout.fragment_put_time, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        initButtons(view)

        return view
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
        view.findViewById<Button>(R.id.wakeup_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.wakeup_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.bad_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.bad_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.breakfast_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.breakfast_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.lunch_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.lunch_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.diner_buttom).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.diner_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.next2_button).setOnClickListener {
            mySharePreferences.setWakeup(view.findViewById<TextView>(R.id.wakeup_time).text.toString())
            mySharePreferences.setSleep(view.findViewById<TextView>(R.id.bad_time).text.toString())
            mySharePreferences.setBreakfast(view.findViewById<TextView>(R.id.breakfast_time).text.toString())
            mySharePreferences.setLunch(view.findViewById<TextView>(R.id.lunch_time).text.toString())
            mySharePreferences.setDiner(view.findViewById<TextView>(R.id.diner_time).text.toString())

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_frag, WorkDays())
                    ?.commit()
        }
    }
}