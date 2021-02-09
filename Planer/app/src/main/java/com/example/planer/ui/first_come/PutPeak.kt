package com.example.planer.ui.first_come

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PutPeak : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var peakBeginText:TextView
    private lateinit var peakEndText:TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_peak, container, false)
        peakBeginText = view.findViewById<TextView>(R.id.peak_begin_time)
        peakEndText = view.findViewById<TextView>(R.id.peak_end_time)

        mySharePreferences = context?.let { MySharePreferences(it) }!!

        initButton(view)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButton(view: View)
    {
        view.findViewById<Button>(R.id.peak_begin_button).setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(peakBeginText, it1) }
        }

        view.findViewById<Button>(R.id.peak_end_button).setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(peakEndText, it1) }
        }

        view.findViewById<Button>(R.id.next4_button).setOnClickListener {
            val time1 = LocalTime.parse(peakBeginText.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val time2 = LocalTime.parse(peakEndText.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if(time1 > time2){
                this.context?.let { it1 -> ToastMessages.showMessage(it1, "Кажется вы перепутали начало и конец...") }
            } else {
                mySharePreferences.setPeakBegin(peakEndText.text.toString())
                mySharePreferences.setPeakEnd(peakEndText.text.toString())

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
