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
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndGroup
import kotlinx.android.synthetic.main.fragment_task.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GroupTasksRecyclerAdapter(private val context: Context,
                                private var taskList: List<Task>?,
                                private val listener: OnItemClickListener
): RecyclerView.Adapter<GroupTasksRecyclerAdapter.ViewHolder>()
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.fragment_task, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getTask(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = taskList?.size?:0

    private fun getTask(position: Int): Task? = taskList?.get(position)

    fun setTasks(taskList: List<Task>){
        this.taskList = taskList
    }

    inner class ViewHolder(taskView: View) : RecyclerView.ViewHolder(taskView), View.OnClickListener{
        private val date: TextView = taskView.task_date
        private val title: TextView = taskView.task_title
        private val description: TextView = taskView.group_title
        private val priority: ImageView =  taskView.priorityImageView
        private val card: CardView = taskView.card_view
        private val rec: ImageView? = taskView.rectangle
        private val rec2: ImageView? = taskView.rectangle2
        private val selfImprovement: ImageView? = taskView.self_improvement

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(version: Task)
        {
            when(version.category){
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

            title.text = version.title
            description.text = version.description
            selfImprovement?.visibility = View.INVISIBLE

            when {
                version.deadline != null -> {
                    val deadline = LocalDate.parse(version.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    date.text= deadline.dayOfMonth.toString() + "." + deadline.monthValue
                }
                version.date != null -> {
                    val beginDate = LocalDate.parse(version.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    date.text= beginDate.dayOfMonth.toString() + "." + beginDate.monthValue
                }
                else -> {
                    date.text = version.begin + "\n" + "-" + "\n" + version.end
                }
            }
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