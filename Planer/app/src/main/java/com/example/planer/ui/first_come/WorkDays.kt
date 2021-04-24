package com.example.planer.ui.first_come

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog
import kotlinx.android.synthetic.main.fragment_put_time.*
import kotlinx.android.synthetic.main.fragment_work_days.*
import kotlinx.android.synthetic.main.fragment_work_days.view.*

class WorkDays : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_work_days, container, false)
        mySharePreferences = context?.let { MySharePreferences(it)}!!

        if (savedInstanceState != null) {
            view.mon_begin_time.text = savedInstanceState.getString("monBegin")
            view.mon_work_time.text = savedInstanceState.getString("monWork")
            view.tue_begin_time.text = savedInstanceState.getString("tueBegin")
            view.tue_work_time.text = savedInstanceState.getString("tueWork")
            view.wen_begin_time.text = savedInstanceState.getString("wenBegin")
            view.wen_work_time.text = savedInstanceState.getString("wenWork")
            view.thu_begin_time.text = savedInstanceState.getString("thuBegin")
            view.thu_work_time.text = savedInstanceState.getString("thuWork")
            view.fri_begin_time.text = savedInstanceState.getString("friBegin")
            view.fri_work_time.text = savedInstanceState.getString("friWork")
            view.sat_begin_time.text = savedInstanceState.getString("satBegin")
            view.sat_work_time.text = savedInstanceState.getString("satWork")
            view.sun_begin_time.text = savedInstanceState.getString("sunBegin")
            view.sun_work_time.text = savedInstanceState.getString("sunWork")
        }

        initButtons(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("monBegin", mon_begin_time.text.toString())
        outState.putString("monWork", mon_work_time.text.toString())
        outState.putString("tueBegin", tue_begin_time.text.toString())
        outState.putString("tueWork", tue_work_time.text.toString())
        outState.putString("wenBegin", wen_begin_time.text.toString())
        outState.putString("wenWork", wen_work_time.text.toString())
        outState.putString("thuBegin", thu_begin_time.text.toString())
        outState.putString("thuWork", thu_work_time.text.toString())
        outState.putString("friBegin", fri_begin_time.text.toString())
        outState.putString("friWork", fri_work_time.text.toString())
        outState.putString("satBegin", sat_begin_time.text.toString())
        outState.putString("satWork", sat_work_time.text.toString())
        outState.putString("sunBegin", sun_begin_time.text.toString())
        outState.putString("sunWork", sun_work_time.text.toString())
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
                this.context?.let { InfoDialog.onCreateDialog(it, "Подсказка", "Введите время начала работы и длительность рабочего дня для каждого дня недели. Если в какой-то из дней работа не планируется, оставьте значения \"00:00\".", R.drawable.blue_info) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("CutPasteId")
    private fun initButtons(view: View)
    {
        view.mon_button.setOnClickListener {
                this.context?.let { it1 -> TimeDialog.getTime(view.findViewById<TextView>(R.id.mon_begin_time), it1)
            }
        }

        view.mon_work_button.setOnClickListener {
                this.context?.let { it1 -> TimeDialog.getTime(view.findViewById<TextView>(R.id.mon_work_time), it1)
            }
        }

        view.tue_begin_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.tue_begin_time),
                    it1
                )
            }
        }

        view.tue_work_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.tue_work_time),
                    it1
                )
            }
        }

        view.wen_begin_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.wen_begin_time),
                    it1
                )
            }
        }

        view.wen_work_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.wen_work_time),
                    it1
                )
            }
        }

        view.thu_begin_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.thu_begin_time),
                    it1
                )
            }
        }

        view.thu_work_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.thu_work_time),
                    it1
                )
            }
        }

        view.fri_begin_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.fri_begin_time),
                    it1
                )
            }
        }

        view.fri_work_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.fri_work_time),
                    it1
                )
            }
        }

        view.sat_begin_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sat_begin_time),
                    it1
                )
            }
        }

        view.sat_work_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sat_work_time),
                    it1
                )
            }
        }

        view.sun_begin_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sun_begin_time),
                    it1
                )
            }
        }

        view.sun_work_button.setOnClickListener {
            this.context?.let { it1 ->
                TimeDialog.getTime(
                    view.findViewById<TextView>(R.id.sun_work_time),
                    it1
                )
            }
        }

        view.next3_button.setOnClickListener {
            mySharePreferences.setMondayBegin(view.mon_begin_time.text.toString())
            mySharePreferences.setMondayWork(view.mon_work_time.text.toString())
            mySharePreferences.setTuesdayBegin(view.tue_begin_time.text.toString())
            mySharePreferences.setTuesdayWork(view.tue_work_time.text.toString())
            mySharePreferences.setWednesdayBegin(view.wen_begin_time.text.toString())
            mySharePreferences.setWednesdayWork(view.wen_work_time.text.toString())
            mySharePreferences.setThursdayBegin(view.thu_begin_time.text.toString())
            mySharePreferences.setThursdayWork(view.thu_work_time.text.toString())
            mySharePreferences.setFridayBegin(view.fri_begin_time.text.toString())
            mySharePreferences.setFridayWork(view.fri_work_time.text.toString())
            mySharePreferences.setSaturdayBegin(view.sat_begin_time.text.toString())
            mySharePreferences.setSaturdayWork(view.sat_work_time.text.toString())
            mySharePreferences.setSundayBegin(view.sun_begin_time.text.toString())
            mySharePreferences.setSundayWork(view.sun_work_time.text.toString())

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.main_frag, PutPeak())
                    ?.commit()
        }
    }
}