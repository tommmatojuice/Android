package com.example.planer.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_tasks_types.view.*

class TasksTypesFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_tasks_types, container, false)

        return view
    }
}