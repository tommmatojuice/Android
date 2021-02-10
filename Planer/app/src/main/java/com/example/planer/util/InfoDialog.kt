package com.example.planer.util

import android.app.AlertDialog
import android.content.Context

class InfoDialog
{
    companion object
    {
        fun onCreateDialog(context: Context, title: String, messages: String, icon: Int) {
            AlertDialog.Builder(context).setTitle(title)
                    .setMessage(messages)
                    .setIcon(icon)
                    .setPositiveButton(android.R.string.yes) { dialog, which -> true }
                    .show()
        }
    }
}