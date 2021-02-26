package com.example.planer.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.database.entity.GroupAndAllTasks
import kotlinx.android.synthetic.main.fragment_group.view.*
import kotlinx.android.synthetic.main.fragment_task.view.*
import kotlinx.android.synthetic.main.fragment_task.view.group_title

class GroupRecyclerAdapter(context: Context,
                           private var taskList: List<GroupAndAllTasks>?,
                           private val listener: OnItemClickListener
): RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.fragment_group, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getTask(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = taskList?.size?:0

    private fun getTask(position: Int): GroupAndAllTasks? = taskList?.get(position)

    fun setTasks(taskList: List<GroupAndAllTasks>){
        this.taskList = taskList
    }

    inner class ViewHolder(private var groupView: View) : RecyclerView.ViewHolder(groupView), View.OnClickListener{
        private val title: TextView = groupView.group_title
        private val count: TextView = groupView.group_count
        private val card: CardView = groupView.group_card_view
        private val rec: ImageView? = groupView.group_rectangle
        private val rec2: ImageView? = groupView.group_rectangle2

        fun bind(version: GroupAndAllTasks)
        {
            when(version.tasks[0].category){
                "rest" -> {
                    card.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#26C281"))
                    rec?.setImageResource(R.drawable.rec_green)
                    rec2?.setImageResource(R.drawable.rec2_green)
                }
                "other" -> {
                    card.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F89406"))
                    rec?.setImageResource(R.drawable.rec_orange)
                    rec2?.setImageResource(R.drawable.rec2_orange)
                }
            }
            title.text = version.group.title
            count.text = (version.tasks.size - 1).toString()
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