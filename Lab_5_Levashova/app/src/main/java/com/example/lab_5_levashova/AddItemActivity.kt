package com.example.lab_5_levashova

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddItemActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        var title = findViewById<EditText>(R.id.titleEditText)
        var description = findViewById<EditText>(R.id.descriptionEdittext)
        var priority = findViewById<CheckBox>(R.id.priorityCheckBox)

        val item = intent.extras?.get("Item") as Item?

        if (item != null){
            title.setText(item.title)
            description.setText(item.description)
            priority.isChecked = item.priority
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.save_item -> {
                val title = findViewById<EditText>(R.id.titleEditText)
                val description = findViewById<EditText>(R.id.descriptionEdittext)
                val priority = findViewById<CheckBox>(R.id.priorityCheckBox)

                if (title.text.isNotEmpty()) {
                    val intent = Intent()
                    val item = this.intent.extras?.get("Item") as Item?
                    if (item != null) {
                        item.title = title.text.toString()
                        item.description = description.text.toString()
                        item.priority = priority.isChecked
                        intent.putExtra("item", item)
                    } else {
                        intent.putExtra("item", Item(title.text.toString(),description.text.toString(), priority.isChecked))
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    showMessage("Введите название!")
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