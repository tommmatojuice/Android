package com.example.planer.ui.food

import android.content.Context
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.planer.MainActivity
import com.example.planer.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class FoodFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_food, container, false)

        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.red) }
//        activity?.actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0F9D58")))

        val textView: TextView = root.findViewById(R.id.text_dashboard)
        foodViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D91E18")))

        return root
    }
}