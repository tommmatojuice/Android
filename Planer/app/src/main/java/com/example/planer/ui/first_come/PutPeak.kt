package com.example.planer.ui.first_come

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_put_peak.*
import kotlinx.android.synthetic.main.fragment_put_peak.view.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PutPeak : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_peak, container, false)

        mySharePreferences = context?.let { MySharePreferences(it) }!!

        if (savedInstanceState != null) {
            view.peak_begin_time.text = savedInstanceState.getString("peakBegin").toString()
            view.peak_end_time.text = savedInstanceState.getString("peakEnd").toString()
        }

        initButton(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("peakBegin", peak_begin_time.text.toString())
        outState.putString("peakEnd", peak_end_time.text.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButton(view: View)
    {
        view.peak_begin_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.peak_begin_time, it1) }
        }

        view.peak_end_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.peak_end_time, it1) }
        }

        view.findViewById<Button>(R.id.next4_button).setOnClickListener {
            val time1 = LocalTime.parse(view.peak_begin_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val time2 = LocalTime.parse(view.peak_end_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if(time1 > time2){
                this.context?.let { it1 -> ToastMessages.showMessage(it1, "Кажется вы перепутали начало и конец...") }
            } else {
                mySharePreferences.setPeakBegin(view.peak_begin_time.text.toString())
                mySharePreferences.setPeakEnd(view.peak_end_time.text.toString())

                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.main_frag, PutPomodoro())
                        ?.commit()
            }
        }
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
                this.context?.let { InfoDialog.onCreateDialog(it, "Подсказка", "Введите промежуток вермени, когда вам легче всего концентрироваться на работе. Еси такого промежутка нет, оставьте значение \"00:00\".", R.drawable.blue_info) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
