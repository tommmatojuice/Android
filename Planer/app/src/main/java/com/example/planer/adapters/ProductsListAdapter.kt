package com.example.planer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.database.entity.ListAndAllProducts
import kotlinx.android.synthetic.main.fragment_product_list.view.*

class ProductsListAdapter(context: Context,
                          private var productsListList: List<ListAndAllProducts>?,
                          private val listener: OnItemClickListener
): RecyclerView.Adapter<ProductsListAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.fragment_product_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getList(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = productsListList?.size?:0

    private fun getList(position: Int): ListAndAllProducts? = productsListList?.get(position)

    fun setList(productsListList: List<ListAndAllProducts>){
        this.productsListList = productsListList
    }

    inner class ViewHolder(listView: View) : RecyclerView.ViewHolder(listView), View.OnClickListener{
        private val title: TextView = listView.list_title
        private val cost: TextView = listView.list_cost
        private val card: CardView = listView.product_list_card

        fun bind(version: ListAndAllProducts)
        {
            title.text = version.list.title
            cost.text = "${version.products.sumOf { it.count * it.cost!!}} руб."
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
}