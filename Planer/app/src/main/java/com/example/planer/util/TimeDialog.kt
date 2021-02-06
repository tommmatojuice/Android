package com.example.planer.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class TimeDialog {
    companion object{
        @SuppressLint("SimpleDateFormat")
        fun getTime(textView: TextView, context: Context){
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                textView.text = SimpleDateFormat("HH:mm").format(cal.time)
            }

            TimePickerDialog(context, timeSetListener, 0, 0, true).show()
        }
    }
}