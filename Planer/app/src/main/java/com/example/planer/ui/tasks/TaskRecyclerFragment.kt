package com.example.planer.ui.tasks

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.TaskRecyclerAdapter
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.ui.plan.AutoPlan
import com.example.planer.util.InfoDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_put_peak.*
import kotlinx.android.synthetic.main.fragment_put_peak.view.*
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*
import java.time.LocalDate


class TaskRecyclerFragment(private var type: String, private var category: String) : Fragment(), TaskRecyclerAdapter.OnItemClickListener
{
    private val taskViewModel: TaskViewModel by viewModels()
    private var tasks: List<TaskAndGroup>? = null
    private var allTasks: List<Task>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_task_recycler, container, false)

        if (savedInstanceState != null) {
            this.type = savedInstanceState.getString("type").toString()
            this.category = savedInstanceState.getString("category").toString()
        }

        val adapter = this.context?.let { TaskRecyclerAdapter(it, tasks, this, category) }
        val list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            addTask(view, null)
        }

        initUI()

        taskViewModel.taskAndGroup(category, type).observe(
                viewLifecycleOwner, { tasks ->
            if (tasks != null) {
                this.tasks = tasks
                adapter?.setTasks(tasks)
                list.adapter = adapter
            }
        }
        )

        taskViewModel.allTasks.observe(
                viewLifecycleOwner, { tasks ->
            if (tasks != null) {
                this.allTasks = tasks
                Log.d("all_in", this.allTasks!!.size.toString())
            }
        }
        )

        Log.d("all", allTasks?.size.toString())


        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val id = tasks?.find { task -> task.task_id == tasks?.get(viewHolder.adapterPosition)?.task_id }?.task_id
                val task = allTasks?.find { task -> task.task_id == id }

                val myClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        Dialog.BUTTON_POSITIVE -> {
                            if (task != null) {
                                taskViewModel.delete(task)
                            }
                        }
                        Dialog.BUTTON_NEGATIVE -> {
                            tasks?.let { adapter?.setTasks(it) }
                            list.adapter = adapter
                        }
                    }
                }

                context?.let { InfoDialog.onCreateConfirmDialog(it, "Удаление", "Удалить задачу \"${task?.title}\"?", R.drawable.delete, myClickListener)}
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)

        return view
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.dark_green) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#13A678")))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(): this("", "")

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("type", this.type)
        outState.putString("category", this.category)
    }

    override fun onItemClick(position: Int) {
        val task = allTasks?.find { task -> task.task_id == tasks?.get(position)?.task_id!! }
        view?.let { addTask(it, task) }
    }

    private fun addTask(view: View, task: Task?)
    {
        val bundle = Bundle().apply { putString("type", type) }
                .apply { putString("category", category) }
                .apply { putSerializable("task", task) }
                .apply { putBoolean("back", false) }
        when(type){
            "one_time" -> {
                if (category == "work")
                    Navigation.findNavController(view).navigate(R.id.add_one_time_work_task, bundle)
                else Navigation.findNavController(view).navigate(R.id.add_one_time_other_task, bundle)
            }
            "fixed" -> {
                Navigation.findNavController(view).navigate(R.id.add_fixed_task, bundle)
            }
            "routine" -> {
                Navigation.findNavController(view).navigate(R.id.add_routine_task, bundle)
            }
        }
    }
}