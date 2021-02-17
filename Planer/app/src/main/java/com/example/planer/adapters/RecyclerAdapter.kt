package com.example.planer.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.database.entity.TaskAndGroup
import kotlinx.android.synthetic.main.one_time_work_task.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class RecyclerAdapter(
    context: Context,
    private var taskList: List<TaskAndGroup>?,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.one_time_work_task, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getTask(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = taskList?.size?:0

    private fun getTask(position: Int): TaskAndGroup? = taskList?.get(position)

    fun setTasks(taskList: List<TaskAndGroup>){
        this.taskList = taskList
    }

    inner class ViewHolder(taskView: View) : RecyclerView.ViewHolder(taskView), View.OnClickListener{
        private val date:TextView = taskView.task_date
        private val title:TextView = taskView.task_title
        private val group:TextView = taskView.group_title
        private val card: CardView = taskView.card_view

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(version: TaskAndGroup){
            val deadline = LocalDate.parse(version.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            title.text = version.title
            date.text= deadline.dayOfMonth.toString() + "." + deadline.monthValue
            group.text = version.group
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
