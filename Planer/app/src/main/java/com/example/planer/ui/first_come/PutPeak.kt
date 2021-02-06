package com.example.planer.ui.first_come

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PutPeak : Fragment()
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_peak, container, false)

        initButton(view)

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
        (activity as MainActivity?)?.setActionBarTitle("")
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButton(view: View)
    {
        val sharedPreferences = activity?.getSharedPreferences("SP_INFO", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        val peakBeginText = view.findViewById<TextView>(R.id.peak_begin_time)
        val peakEndText = view.findViewById<TextView>(R.id.peak_end_time)

        view.findViewById<Button>(R.id.peak_begin_button).setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(peakBeginText, it1) }
        }

        view.findViewById<Button>(R.id.peak_end_button).setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(peakEndText, it1) }
        }

        view.findViewById<Button>(R.id.next4_button).setOnClickListener {
            var time1 = LocalTime.parse(peakBeginText.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            var time2 = LocalTime.parse(peakEndText.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            if(time1 > time2){
                this.context?.let { it1 -> ToastMessages.showMessage(it1, "Кажется вы перепутали начало и конец...") }
            } else {
                editor?.putString("PEAK_BEGIN", peakBeginText.text.toString())
                editor?.putString("PEAK_END", peakEndText.text.toString())
                editor?.apply()

                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.main_frag, PutPomodoro())
                        ?.commit()
            }
        }
    }
}
