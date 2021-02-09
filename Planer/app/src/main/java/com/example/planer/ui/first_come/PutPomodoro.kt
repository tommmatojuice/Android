package com.example.planer.ui.first_come

import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class PutPomodoro : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_pomodoro, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        initButtons(view)

        return view
    }

    private fun initButtons(view: View)
    {
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            view.findViewById<RadioButton>(checkedId)?.apply {
                mySharePreferences.setPomodoroWork( text.toString().toInt())
            }
        }

        view.findViewById<Button>(R.id.next5_button).setOnClickListener {
            mySharePreferences.setAllInfo(true)

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