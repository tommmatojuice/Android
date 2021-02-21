package com.example.planer.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.planer.R
import com.example.planer.adapters.GroupTasksRecyclerAdapter
import com.example.planer.database.entity.GroupAndAllTasks
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*

class GroupTasksRecyclerFragment : Fragment(), GroupTasksRecyclerAdapter.OnItemClickListener
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_task_recycler, container, false)

        val group = arguments?.getSerializable("tasks") as GroupAndAllTasks

        val adapter = this.context?.let { GroupTasksRecyclerAdapter(it, group.tasks, this) }
        val list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        (activity as AppCompatActivity).supportActionBar?.title = group.group.title
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val buttonAddItem: FloatingActionButton = view.button_add_item
//        buttonAddItem.setOnClickListener{
//            val intent = Intent(this, AddItemActivity::class.java)
//            startActivityForResult(intent, ADD_ITEM_REQUEST)
//        }

        return view
    }

    override fun onItemClick(position: Int) {

    }
}