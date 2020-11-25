package com.example.lab_5_levashova

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
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
//        Log.d("@@@@@@@@@@@@@@@@@@@", items.toString())
        val adapter = ItemAdapter(this, items, this)
//        items?.let { adapter.setItems(it) }
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
                    }
                }
            }
        )
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
                Log.d("@@@@@@@@@@@@@@@@@", "update")
                if (items != null) {
                    var i =0
                    for (item in items){
                        if (item.id == data.getIntExtra("id", -1)){
                            items[i].title = data.getStringExtra("title").toString()
//                            item.title = data.getStringExtra("title").toString()
                            items[i].description = data.getStringExtra("description").toString()
                            items[i].priority = data.getBooleanExtra("priority", false)
                            itemViewModel.update(items[i])
                        }
                        i++
                    }
                }
//                items?.forEach{
//                    if (it.id == data.getIntExtra("id", -1)){
//                        it.title = data.getStringExtra("title").toString()
//                        it.description = data.getStringExtra("description").toString()
//                        it.priority = data.getBooleanExtra("priority", false)
//                        itemViewModel.update(it)
//                    }
//                }
            }

//            items?.get(0)?.title = "измененное названия"
//            items?.get(0)?.let { itemViewModel.update(it) }
        }


//        Log.d("ddddddddddd", data.toString())
//        if (data != null){
////            val extras: Bundle? = data.extras
////            val item = extras?.get("item") as Item
//            val flag = data.getBooleanExtra("flag", false)
//            Log.d("FFFFFFFFFFFFFFFFF", flag.toString())
//            if(flag){
//                itemViewModel.insert(Item(data.getStringExtra("title").toString(), data.getStringExtra("description").toString(),
//                    data.getBooleanExtra("priority", false)
//                ))
//            } else {
////                itemViewModel.insert(Item(data.getStringExtra("title").toString(), data.getStringExtra("description").toString(), data?.getBooleanExtra("priority", false)))
//                val items = itemViewModel.allItems.value
//                items?.forEach{
//                    if (it.id.toString() == data.getStringExtra("id")){
//                        it.title = data.getStringExtra("title").toString()
//                        it.description = data.getStringExtra("description").toString()
//                        it.priority = data.getBooleanExtra("priority", false)
//                        itemViewModel.update(it)
//                    }
//                }
//            }
//        }
    }

    override fun onItemClick(position: Int) {
        val items = itemViewModel.allItems.value
        Log.d("@@@@@@@", "Click! $position")
        val item: Item? = items?.get(position)
        if (item != null) {
            Log.d("@@@@@@@@@@@@@@@@@@@", item.title + " " +  item.description + " " + item.priority)
        }
        val intent = Intent(this, AddItemActivity::class.java).apply { putExtra("Item", item) }
        startActivity(intent)
    }
}