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
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.ui.plan.TasksForPlan
import kotlinx.android.synthetic.main.fragment_task.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlanRecyclerAdapter (context: Context,
                           private var taskList: MutableList<TasksForPlan>?,
                           private val listener: OnItemClickListener
): RecyclerView.Adapter<PlanRecyclerAdapter.ViewHolder>()
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

    private fun getTask(position: Int): TasksForPlan? = taskList?.get(position)

    fun setTasks(taskList: MutableList<TasksForPlan>){
        this.taskList = taskList
    }

    inner class ViewHolder(taskView: View) : RecyclerView.ViewHolder(taskView), View.OnClickListener{
        private val date: TextView = taskView.task_date
        private val title: TextView = taskView.task_title
        private val group: TextView = taskView.group_title
        private val priority: ImageView =  taskView.priorityImageView
        private val card: CardView = taskView.card_view
        private val rec: ImageView? = taskView.rectangle
        private val rec2: ImageView? = taskView.rectangle2
        private val selfImprovement: ImageView? = taskView.self_improvement

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(version: TasksForPlan)
        {
            card.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F1EF"))
            rec?.setImageResource(R.drawable.rec_gray)
            rec2?.setImageResource(R.drawable.rec2_gray)

            title.text = version.task?.title
            group.visibility = View.GONE
            selfImprovement?.visibility = View.INVISIBLE

            if(version.task?.category != "work" && version.task?.type == "one_time"){
                date.text = null
                selfImprovement?.visibility = View.VISIBLE
            } else {
                date.text = "${version.begin}\n-\n${version.end}"
            }

            if(version.task?.priority != null){
                priority.visibility = if (
                        version.task?.priority!!
                ) View.VISIBLE else View.INVISIBLE
            } else priority.visibility = View.INVISIBLE
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