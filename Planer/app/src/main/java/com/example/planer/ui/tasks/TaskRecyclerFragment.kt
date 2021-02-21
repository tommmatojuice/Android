package com.example.planer.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.planer.R
import com.example.planer.adapters.TaskRecyclerAdapter
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.ToastMessages
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*

class TaskRecyclerFragment(private var type: String, private var category: String) : Fragment(), TaskRecyclerAdapter.OnItemClickListener
{
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_task_recycler, container, false)

        this.context?.let { ToastMessages.showMessage(it, type) }

        taskViewModel = activity?.application?.let { TaskViewModel(it, category, type) }!!

        val tasks = taskViewModel.taskAndGroup.value
        val adapter = this.context?.let { TaskRecyclerAdapter(it, tasks, this, category) }
        val list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            addTask(view)
        }

        taskViewModel.taskAndGroup.observe(
                viewLifecycleOwner, object: androidx.lifecycle.Observer<List<TaskAndGroup>> {
                override fun onChanged(tasks: List<TaskAndGroup>?) {
                    if (tasks != null) {
                        adapter?.setTasks(tasks)
                        list.adapter = adapter
                    }
                }
            }
        )
        return view
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }

    private fun addTask(view: View)
    {
        val bundle = Bundle().apply { putString("type", type) }.apply { putString("category", category) }
        when(type){
            "one_time" -> {
                if(category == "work")
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