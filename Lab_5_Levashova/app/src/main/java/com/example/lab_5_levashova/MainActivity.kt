package com.example.lab_5_levashova

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener
{
    private val itemViewModel: ItemViewModel by viewModels()
    private val ADD_ITEM_REQUEST: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = itemViewModel.allItems.value
        val adapter = ItemAdapter(this, items, this)
        items?.let { adapter.setItems(it) }
        val list = findViewById<RecyclerView>(R.id.recycler_view)
        list.adapter = adapter

        val decoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.color.white)!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = findViewById(R.id.button_add_item)
        buttonAddItem.setOnClickListener{
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, ADD_ITEM_REQUEST)
        }

        itemViewModel.allItems.observe(
            this, object: Observer<List<Item>>{
                override fun onChanged(items: List<Item>?) {
                    if (items != null) {
                        adapter.setItems(items)
                        list.adapter = adapter
                    }
                }
            }
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val items = itemViewModel.allItems.value
                Log.d("@@@@@@@", "DELETE!")
                Log.d("@@@@@@@", items?.get(viewHolder.adapterPosition)?.id.toString())
                Log.d("@@@@@@@", viewHolder.adapterPosition.toString())
                items?.forEach {
                    if (it.id == items[viewHolder.adapterPosition].id){
                        itemViewModel.delete(it)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val items = itemViewModel.allItems.value
        if (data != null) {
            if(data.getIntExtra("id", -1) == -1){
                itemViewModel.insert(
                        Item(
                                data.getStringExtra("title").toString(),
                                data.getStringExtra("description").toString(),
                                data.getBooleanExtra("priority", false)
                        )
                )
            } else {
//                Log.d("@@@@@@@@@@@@@@@@@", "update")
                if (items != null) {
                    Log.d("@@@@@@@@@@@@@@@@@", data.getIntExtra("id", -1).toString())
//                    items?.forEach {
//                        if (it.id == data.getIntExtra("id", -1)){
//                            itemViewModel.update(it)
//                            Log.d("@@@@@@@@@@@@@@@@@", "update")
//                        }
//                    }

                    var i = 0
                    for (item in items){
                        if (item.id == data.getIntExtra("id", -1)){
                            items[i].title = data.getStringExtra("title").toString()
                            items[i].description = data.getStringExtra("description").toString()
                            items[i].priority = data.getBooleanExtra("priority", false)
                            itemViewModel.update(items[i])
                        }
                        i++
                    }
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
        val items = itemViewModel.allItems.value
        Log.d("@@@@@@@", "Click! $position")
        val item: Item? = items?.get(position)
        if (item != null) {
            Log.d("@@@@@@@@@@@@@@@@@@@", item.title + " " +  item.description + " " + item.priority)
        }
        val intent = Intent(this, AddItemActivity::class.java).apply { putExtra("Item", item) }
        startActivityForResult(intent, ADD_ITEM_REQUEST)
    }
}