package com.example.planer.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planer.R
import kotlinx.android.synthetic.main.fragment_into_tasks_types.view.*
import kotlinx.android.synthetic.main.fragment_tasks_types.view.*

class IntoTasksTypesFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_into_tasks_types, container, false)

        initButtons(view)

        return view
    }

    private fun initButtons(view: View){

    }
}