package com.example.planer.ui.notifications

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.planer.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        initUI()

        return root
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.yellow) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#F9BF3B")))
        (activity as AppCompatActivity).supportActionBar?.setTitle(Html.fromHtml("<font color=\"#F2F1EF\">" + "Уведомления" + "</font>"))
    }
}