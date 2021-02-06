package com.example.planer.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planer.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class FoodViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is food Fragment"
    }

    val text: LiveData<String> = _text
}