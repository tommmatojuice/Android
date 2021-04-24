package com.example.planer.ui.first_come

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
import com.example.planer.util.ToastMessages

class PutName : Fragment()
{
    private lateinit var mySharePreferences: MySharePreferences
    private lateinit var nameView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_name, container, false)
        nameView = view.findViewById(R.id.user_name)

        mySharePreferences = context?.let { MySharePreferences(it) }!!

        this.context?.let { ToastMessages.showMessage(it, "Все настройки можно будет изменить позже в разделе \"Профиль\".") }

        if (savedInstanceState != null) {
            nameView.text = savedInstanceState.getString("name").toString()
        }

        initButtons(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", nameView.text.toString())
    }


    private fun initButtons(view: View)
    {
        view.findViewById<Button>(R.id.next1_button).setOnClickListener {
            val name= nameView.text.toString()
            if (name.isEmpty())
                activity?.applicationContext?.let { ToastMessages.showMessage(it, "Введите имя") }
            else {
                mySharePreferences.setName(name)

                activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.main_frag, PutTime())
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
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
                this.context?.let { InfoDialog.onCreateDialog(it, "Подсказка", "Приветствую! Введите свое имя или любой способ обращения к вам. Все настройки можно будет изменить позже в разделе \"Профиль\".", R.drawable.blue_info) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
