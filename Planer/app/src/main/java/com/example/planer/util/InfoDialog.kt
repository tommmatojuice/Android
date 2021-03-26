package com.example.planer.util

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View


class InfoDialog
{
    companion object
    {
        fun onCreateDialog(context: Context, title: String, messages: String, icon: Int) {
            AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(messages)
                    .setIcon(icon)
                    .setPositiveButton(android.R.string.yes) { dialog, which -> true }
                    .show()
        }

        fun onCreateConfirmDialog(context: Context, title: String, messages: String, icon: Int, myClickListener: DialogInterface.OnClickListener){
            AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(messages)
                    .setIcon(icon)
                    .setPositiveButton(R.string.yes, myClickListener)
                    .setNegativeButton(R.string.no, myClickListener)
                    .show()
        }
    }
}