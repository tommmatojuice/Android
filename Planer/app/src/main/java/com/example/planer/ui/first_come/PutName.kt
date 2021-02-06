package com.example.planer.ui.first_come

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import com.example.planer.MainActivity
import com.example.planer.R
import com.example.planer.util.InfoDialog
import com.example.planer.util.ToastMessages

class PutName : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_put_name, container, false)

        this.context?.let { ToastMessages.showMessage(it, "Все настройки можно будет изменить позже в разделе \"Профиль\"") }

        initButtons(view)

        return view
    }

    private fun initButtons(view: View)
    {
        val sharedPreferences = activity?.getSharedPreferences("SP_INFO", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        view.findViewById<Button>(R.id.next1_button).setOnClickListener {
            var name= view.findViewById<EditText>(R.id.user_name).text.toString()
            if (name.isEmpty())
                activity?.applicationContext?.let { ToastMessages.showMessage(it, "Введите имя") }
            else {
                editor?.putString("NAME", name)
                editor?.apply()

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
                this.context?.let { InfoDialog.onCreateDialog(it, "Подсказка", "Приветствую! Введите свое имя или любой способ обращения к вам. Все настройки можно будет изменить позже в разделе \"Профиль\".", R.drawable.blue_info) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
