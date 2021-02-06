package com.example.planer.ui.first_come

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView

class PutPomodoro : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_pomodoro, container, false)

        initButtons(view)

        return view
    }

    private fun initButtons(view: View)
    {
        val sharedPreferences = activity?.getSharedPreferences("SP_INFO", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        editor?.putInt("POMODORO", 25)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            view.findViewById<RadioButton>(checkedId)?.apply {
                editor?.putInt("POMODORO", text.toString().toInt())
            }
        }

        view.findViewById<Button>(R.id.next5_button).setOnClickListener {
            editor?.apply()

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
        (activity as MainActivity?)?.setActionBarTitle("")
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