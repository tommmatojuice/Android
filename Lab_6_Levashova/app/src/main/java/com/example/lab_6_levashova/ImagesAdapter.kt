package com.example.lab_6_levashova

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation

class ImagesAdapter(context: Context,
                    private val images: MutableList<ImageResponse>): RecyclerView.Adapter<ImagesAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
//    private val images: MutableList<ImageResponse> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_image, parent, false))
    }

    override fun getItemCount(): Int = images.size

    private fun getItem(position: Int): ImageResponse = images[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun addImages(newImages:List<ImageResponse>){
        images+=newImages
        notifyDataSetChanged()
    }

    fun deleteImages(){
        images.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val positionText: TextView = itemView.findViewById(R.id.image_position)
        private val author: TextView = itemView.findViewById(R.id.image_author)

        fun bind(version: ImageResponse, position: Int) {
            image.load(version.download_url){
                crossfade(true)
                placeholder(R.mipmap.ic_launcher)
                transformations(CircleCropTransformation())
            }
            positionText.text = "Image $position"
            author.text = version.author
        }
    }
}