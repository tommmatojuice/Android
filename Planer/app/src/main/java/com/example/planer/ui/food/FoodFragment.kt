package com.example.planer.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.planer.R

class FoodFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        foodViewModel =
                ViewModelProvider(this).get(FoodViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_food, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        foodViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}