package com.example.planer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.database.entity.Product
import com.example.planer.database.viewModel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_product.view.*

class ProductsAdapter(context: Context,
                      private var productsList: List<Product>?,
                      private val listener: OnItemClickListener,
                      private val productViewModel: ProductViewModel
): RecyclerView.Adapter<ProductsAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.fragment_product, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getProduct(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = productsList?.size?:0

    private fun getProduct(position: Int): Product? = productsList?.get(position)

    fun setList(productsList: List<Product>){
        this.productsList = productsList
    }

    inner class ViewHolder(productView: View) : RecyclerView.ViewHolder(productView), View.OnClickListener
    {
        private val title: TextView = productView.product_title
        private val count: TextView = productView.count
        private val cost: TextView = productView.cost
        private val isBought: CheckBox = productView.is_bought
        private val card: CardView = productView.product_card

        @SuppressLint("Range", "SetTextI18n")
        fun bind(version: Product)
        {
            title.text = version.name
            count.text = "Количество: ${version.count}"
            cost.text = "${version.cost} pуб."
            isBought.isChecked = version.isBought

            if(version.isBought){
                title.transitionAlpha = 0.5F
                count.transitionAlpha = 0.5F
                cost.transitionAlpha = 0.5F
                isBought.transitionAlpha = 0.5F
            }

            isBought.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    title.alpha = 0.5F
                    count.alpha = 0.5F
                    cost.alpha = 0.5F
                    isBought.alpha = 0.5F
                } else {
                    title.transitionAlpha = 1F
                    count.transitionAlpha = 1F
                    cost.transitionAlpha = 1F
                    isBought.transitionAlpha = 1F
                }
                version.isBought = isChecked
                productViewModel.update(version)
            }
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