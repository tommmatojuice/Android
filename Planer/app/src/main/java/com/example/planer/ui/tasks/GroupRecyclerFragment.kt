package com.example.planer.ui.tasks

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.planer.R
import com.example.planer.adapters.GroupRecyclerAdapter
import com.example.planer.adapters.TaskRecyclerAdapter
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.ToastMessages
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_group_recycler.view.*
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*
import kotlinx.android.synthetic.main.fragment_task_recycler.view.button_add_item
import java.util.ArrayList

class GroupRecyclerFragment(private var type: String, private var category: String) : Fragment(), GroupRecyclerAdapter.OnItemClickListener
{
    private lateinit var groupViewModel: GroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_group_recycler, container, false)
        groupViewModel = activity?.application?.let { GroupViewModel(it, category, type) }!!
        val groups = groupViewModel.tasksWithGroup.value

        val adapter = this.context?.let { GroupRecyclerAdapter(it, groups, this) }
        val list = view.group_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            addGroup(view)
        }

        groupViewModel.tasksWithGroup.observe(
                viewLifecycleOwner, { groups ->
                if (groups != null) {
                    adapter?.setTasks(groups)
                    list.adapter = adapter
                }
            }
        )

        return view
    }

    override fun onItemClick(position: Int) {
        val groups = groupViewModel.tasksWithGroup.value
        val bundle = Bundle().apply {
            putSerializable("tasks", groups?.get(position))
        }
        this.view?.let { Navigation.findNavController(it).navigate(R.id.group_tasks, bundle) }
    }

    private fun addGroup(view: View){

    }
}
