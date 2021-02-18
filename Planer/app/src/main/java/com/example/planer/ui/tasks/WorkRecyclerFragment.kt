package com.example.planer.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.planer.R
import com.example.planer.adapters.WorkRecyclerAdapter
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.ToastMessages
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_work_recycler.view.*

class WorkRecyclerFragment(private var type: Int, private var category: String) : Fragment(), WorkRecyclerAdapter.OnItemClickListener
{
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_work_recycler, container, false)

        this.context?.let { ToastMessages.showMessage(it, type.toString()) }

        when(this.type){
            1 ->  taskViewModel = activity?.application?.let { TaskViewModel(it, "work", "one_time") }!!
            2 ->  taskViewModel = activity?.application?.let { TaskViewModel(it, "work", "fixed") }!!
            3 ->  taskViewModel = activity?.application?.let { TaskViewModel(it, "work", "routine") }!!
        }

        val tasks = taskViewModel.taskAndGroup.value
        val adapter = this.context?.let { WorkRecyclerAdapter(it, tasks, this) }
        val list = view.work_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
//        buttonAddItem.setOnClickListener{
//            val intent = Intent(this, AddItemActivity::class.java)
//            startActivityForResult(intent, ADD_ITEM_REQUEST)
//        }

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
}