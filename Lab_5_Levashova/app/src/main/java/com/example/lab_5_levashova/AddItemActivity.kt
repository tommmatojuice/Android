package com.example.lab_5_levashova

import android.R.attr.key
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddItemActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        var title = findViewById<EditText>(R.id.titleEditText)
        var description = findViewById<EditText>(R.id.descriptionEdittext)
        var priority = findViewById<CheckBox>(R.id.priorityCheckBox)

        val item = intent.extras?.get("Item") as Item?

        if (item != null){
            Log.d("@@@@@@@@@@@@@@@@@@@", item.title + " " + item.description + " " + item.priority)

            title.setText(item.title)
            description.setText(item.description)
            priority.isChecked = item.priority
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        return super.onCreateOptionsMenu(menu)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.save_item -> {
                val title = findViewById<EditText>(R.id.titleEditText)
                val description = findViewById<EditText>(R.id.descriptionEdittext)
                val priority = findViewById<CheckBox>(R.id.priorityCheckBox)
                if (title.text != null && description.text != null) {
                    val intent = Intent()
//                    val item = this.intent.extras?.get("Item") as Item?
////                    var flag: Boolean = true
//                    var id: Int = -1
//                    Log.d("2@@@@@@@@@@@@@@@@@@@", item?.id.toString())
//                    if (item != null) {
////                        flag = false
//                        id = item.id
//                    }
////                    intent.putExtra("flag", flag)
                    intent.putExtra("title", title.text.toString())
                            .putExtra("description", description.text.toString())
                            .putExtra("priority", priority.isChecked)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    showMessage("Введите все данные!")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}