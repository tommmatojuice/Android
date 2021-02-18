package com.example.planer.adapters

import android.content.Context
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
import com.example.planer.database.entity.TaskAndGroup
import kotlinx.android.synthetic.main.rest_task.view.*
import kotlinx.android.synthetic.main.work_task.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RestRecyclerAdapter(
    context: Context,
    private var taskList: List<TaskAndGroup>?,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<RestRecyclerAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.rest_task, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getTask(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = taskList?.size?:0

    private fun getTask(position: Int): TaskAndGroup? = taskList?.get(position)

    fun setTasks(taskList: List<TaskAndGroup>){
        this.taskList = taskList
    }

    inner class ViewHolder(taskView: View) : RecyclerView.ViewHolder(taskView), View.OnClickListener{
        private val date: TextView = taskView.rest_task_date
        private val title: TextView = taskView.rest_task_title
        private val group: TextView = taskView.rest_group_title
        private val priority: ImageView =  taskView.rest_priorityImageView
        private val card: CardView = taskView.rest_card_view

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(version: TaskAndGroup)
        {
            title.text = version.title
            if(version.deadline != null){
                val deadline = LocalDate.parse(version.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                date.text= deadline.dayOfMonth.toString() + "." + deadline.monthValue
            } else if (version.date != null){
                val beginDate = LocalDate.parse(version.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                date.text= beginDate.dayOfMonth.toString() + "." + beginDate.monthValue
            } else {
                date.text = version.begin + "\n" + "-" + "\n" + version.end
            }
            group.text = version.groupTitle
            priority.visibility = if (
                version.priority
            ) View.VISIBLE else View.INVISIBLE
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
