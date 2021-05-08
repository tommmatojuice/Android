package com.example.planer.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.database.entity.PathToFile
import kotlinx.android.synthetic.main.fragment_task.view.*
import java.io.File

class FilesRecyclerAdapter(context: Context,
                           private var taskList: List<PathToFile>?,
                           private val listener: OnItemClickListener
): RecyclerView.Adapter<FilesRecyclerAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.file_fragment, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getTask(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = taskList?.size?:0

    private fun getTask(position: Int): PathToFile? = taskList?.get(position)

    fun setFiles(taskList: List<PathToFile>){
        this.taskList = taskList
    }

    inner class ViewHolder(fileView: View) : RecyclerView.ViewHolder(fileView), View.OnClickListener{
        private val title: TextView = fileView.task_title
        private val card: CardView = fileView.card_view

        fun bind(version: PathToFile)
        {
            val path = Uri.parse(version.path).path
            title.text = File(path).name
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