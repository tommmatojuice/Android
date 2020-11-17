package com.example.lab2_levashova

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class recycler_view() : Fragment(), AndroidAdapter.OnItemClickListener
{
    private lateinit var android:List<Android>
    private var listener: OnItemClickListenerMain? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        android = DataStorage.getVersionsList()
        val adapter = context?.let { AndroidAdapter(it, android, this) }
        val list = view.findViewById<RecyclerView>(R.id.androidList)
        list.adapter = adapter

        val decoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(context?.let { ContextCompat.getDrawable(it, R.color.grey) }!!)
        list.addItemDecoration(decoration)
        return view
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if(context is OnItemClickListenerMain){
            listener = context
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        listener = null
    }

    override fun onItemClick(position: Int) {
        listener?.onItemClickMain(position)
    }

    interface OnItemClickListenerMain{
        fun onItemClickMain(position: Int)
    }
}