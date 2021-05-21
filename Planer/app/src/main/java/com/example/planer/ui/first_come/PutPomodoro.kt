package com.example.planer.ui.first_come

import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_put_pomodoro.*
import kotlinx.android.synthetic.main.fragment_put_pomodoro.view.*

class PutPomodoro : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_pomodoro, container, false)

        mySharePreferences = context?.let { MySharePreferences(it) }!!

        if (savedInstanceState != null) {
            view.findViewById<RadioButton>(savedInstanceState.getInt("checkedButton", 0)).isChecked = true
        }

        initButtons(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("checkedButton", radioGroup.checkedRadioButtonId)
    }

    private fun initButtons(view: View)
    {
        view.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            view.findViewById<RadioButton>(checkedId)?.apply {
                mySharePreferences.setPomodoroWork( text.toString().toInt())
                when(text.toString().toInt()){
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
        }

        view.next5_button.setOnClickListener {
            mySharePreferences.setAllInfo(true)

            setHasOptionsMenu(false)

            activity?.findViewById<FrameLayout>(R.id.main_frag)?.visibility = View.GONE
            activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
            activity?.findViewById<View>(R.id.nav_host_fragment)?.visibility = View.VISIBLE
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
                this.context?.let { InfoDialog.onCreateDialog(it, "Подсказка", "Выберите время вашего рабочего помидора (в минутах).", R.drawable.blue_info) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}