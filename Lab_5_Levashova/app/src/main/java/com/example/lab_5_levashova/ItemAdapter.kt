package com.example.lab_5_levashova

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ItemAdapter(context: Context,
                  private var itemList: List<Item>?,
                  private val listener: OnItemClickListener): RecyclerView.Adapter<ItemAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = itemList?.size?:0

    private fun getItem(position: Int): Item? = itemList?.get(position)

    fun setItems(itemList: List<Item>){
        this.itemList = itemList
    }

//    fun deleteItem(position: Int){
//        itemList?.removeAt(position)
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val title:TextView = itemView.findViewById(R.id.titleTextView)
        private val description:TextView = itemView.findViewById(R.id.descriptionTextView)
        private val card:CardView = itemView.findViewById(R.id.card_view)
        private val imageView = itemView.findViewById<ImageView>(R.id.priorityImageView)

        fun bind(version: Item){
            title.text = version.title
            description.text = version.description
            imageView.visibility = if (
                version.priority
                    ) View.VISIBLE else View.GONE
        }

        init {
            card.setOnClickListener(this)
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

    interface OnItemSwipeListener{
        fun onItemSwipe(position: Int)
    }
}
