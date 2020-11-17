package com.example.lab2_levashova

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), AndroidAdapter.OnItemClickListener
{
    private lateinit var android:List<Android>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        android = DataStorage.getVersionsList()
        val adapter = AndroidAdapter(this, android, this)

        val list = findViewById<RecyclerView>(R.id.androidList)
        list.adapter = adapter

        val decoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.color.grey)!!)
        list.addItemDecoration(decoration)
    }

    override fun onItemClick(position: Int) {
        val itemAndroid: Android = android[position]
        val intent = Intent(this, firstActivity::class.java).apply { putExtra("Android", itemAndroid) }
        startActivity(intent)
    }
}


