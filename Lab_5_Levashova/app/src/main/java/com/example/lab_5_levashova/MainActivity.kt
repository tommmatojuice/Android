package com.example.lab_5_levashova

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener
{
    private val itemViewModel: ItemViewModel by viewModels()
    private val ADD_ITEM_REQUEST: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = itemViewModel.allItems.value
        val adapter = ItemAdapter(this, items, this)

        val buttonAddItem: FloatingActionButton = findViewById(R.id.button_add_item)
        buttonAddItem.setOnClickListener{
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, ADD_ITEM_REQUEST)
        }

        itemViewModel.allItems.observe(
            this, object: Observer<List<Item>>{
                override fun onChanged(t: List<Item>?) {
                    if (items != null) {
                        adapter.setItems(items)
                    }
                }

            }
        )
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}