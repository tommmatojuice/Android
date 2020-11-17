package com.example.lab2_levashova

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AndroidAdapter(context: Context,
                     private val android: List<Android>,
                     private val listener: OnItemClickListener): RecyclerView.Adapter<AndroidAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_android, parent, false))
    }

    override fun getItemCount(): Int = android.size

    private fun getItem(position: Int): Android = android[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val image:ImageView = itemView.findViewById(R.id.image)
        private val title:TextView = itemView.findViewById(R.id.title)

        fun bind(version: Android){
            image.setImageResource(version.imageAndroid)
            title.text = version.title
        }

        init {
            image.setOnClickListener(this)
            title.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position:Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}

