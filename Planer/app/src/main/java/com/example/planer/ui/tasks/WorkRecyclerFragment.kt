package com.example.planer.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.planer.R
import com.example.planer.adapters.RecyclerAdapter
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_work_recycler.view.*
import java.util.*

class WorkRecyclerFragment : Fragment(), RecyclerAdapter.OnItemClickListener
{
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_work_recycler, container, false)

        val tasks = taskViewModel.taskAndGroup.value
        val adapter = this.context?.let { RecyclerAdapter(it, tasks, this) }
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