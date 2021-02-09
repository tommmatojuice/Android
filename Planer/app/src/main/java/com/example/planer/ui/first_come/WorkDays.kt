package com.example.planer.ui.first_come

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog

class WorkDays : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_work_days, container, false)
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
                this.context?.let { InfoDialog.onCreateDialog(it, "Подсказка", "Введите время начало работы и длительность рабочего дня для кажого дня недели. Если в какой-то из дней работа не планируется, оставьте значения \"00:00\".", R.drawable.blue_info) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("CutPasteId")
    private fun initButtons(view: View)
    {
//        Monday
        view.findViewById<Button>(R.id.mon_button).setOnClickListener {
                this.context?.let { it1 -> TimeDialog.getTime(view.findViewById<TextView>(R.id.mon_begin_time), it1)
            }
        }

        view.findViewById<Button>(R.id.mon_work_button).setOnClickListener {
                this.context?.let { it1 -> TimeDialog.getTime(view.findViewById<TextView>(R.id.mon_work_time), it1)
            }
        }

//        Tuesday
        view.findViewById<Button>(R.id.tue_begin_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.tue_begin_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.tue_work_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.tue_work_time),
                    it1
                )
            }
        }

//        Wednesday
        view.findViewById<Button>(R.id.wen_begin_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.wen_begin_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.wen_work_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.wen_work_time),
                    it1
                )
            }
        }

//        Thursday
        view.findViewById<Button>(R.id.thu_begin_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.thu_begin_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.thu_work_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.thu_work_time),
                    it1
                )
            }
        }

//        Friday
        view.findViewById<Button>(R.id.fri_begin_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.fri_begin_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.fri_work_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.fri_work_time),
                    it1
                )
            }
        }

//        Saturday
        view.findViewById<Button>(R.id.sat_begin_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sat_begin_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.fri_work_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sat_work_time),
                    it1
                )
            }
        }

//        Sunday
        view.findViewById<Button>(R.id.sun_begin_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sun_begin_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.sun_work_button).setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sun_work_time),
                    it1
                )
            }
        }

        view.findViewById<Button>(R.id.next3_button).setOnClickListener {
            mySharePreferences.setMondayBegin(view.findViewById<TextView>(R.id.mon_begin_time).text.toString())
            mySharePreferences.setMondayWork(view.findViewById<TextView>(R.id.mon_work_time).text.toString())
            mySharePreferences.setTuesdayBegin(view.findViewById<TextView>(R.id.tue_begin_time).text.toString())
            mySharePreferences.setTuesdayWork(view.findViewById<TextView>(R.id.tue_work_time).text.toString())
            mySharePreferences.setWednesdayBegin(view.findViewById<TextView>(R.id.wen_begin_time).text.toString())
            mySharePreferences.setWednesdayWork(view.findViewById<TextView>(R.id.wen_work_time).text.toString())
            mySharePreferences.setThursdayBegin(view.findViewById<TextView>(R.id.thu_begin_time).text.toString())
            mySharePreferences.setThursdayWork(view.findViewById<TextView>(R.id.thu_work_time).text.toString())
            mySharePreferences.setFridayBegin(view.findViewById<TextView>(R.id.fri_begin_time).text.toString())
            mySharePreferences.setFridayWork(view.findViewById<TextView>(R.id.fri_work_time).text.toString())
            mySharePreferences.setSaturdayBegin(view.findViewById<TextView>(R.id.sat_begin_time).text.toString())
            mySharePreferences.setSaturdayWork(view.findViewById<TextView>(R.id.sat_work_time).text.toString())
            mySharePreferences.setSundayBegin(view.findViewById<TextView>(R.id.sun_begin_time).text.toString())
            mySharePreferences.setSundayWork(view.findViewById<TextView>(R.id.sun_work_time).text.toString())

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_frag, PutPeak())
                    ?.commit()
        }
    }
}