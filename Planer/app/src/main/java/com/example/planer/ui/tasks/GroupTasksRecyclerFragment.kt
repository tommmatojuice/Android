package com.example.planer.ui.tasks

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.planer.R
import com.example.planer.adapters.TaskRecyclerAdapter
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.InfoDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*

class GroupTasksRecyclerFragment : Fragment(), TaskRecyclerAdapter.OnItemClickListener
{
    private val taskViewModel: TaskViewModel by viewModels()
    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var group: GroupTask
    private var tasks: List<TaskAndGroup>? = null
    private var allTasks: List<Task>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_task_recycler, container, false)

        group = arguments?.getSerializable("main_group") as GroupTask
        val id = group.group_task_id

        arguments?.putInt("group", id)

        val tasks = taskViewModel.taskByGroup(id).value

        (activity as AppCompatActivity).supportActionBar?.title = group.title
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val adapter = this.context?.let { arguments?.getString("category")?.let { it1 -> TaskRecyclerAdapter(it, tasks, this, it1) } }
        val list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            addGroupTask(view, null)
        }

        taskViewModel.taskByGroup(id).observe(
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
                Log.d("allTasks", this.allTasks!!.size.toString())
            }
        }
        )

        return view
    }

    override fun onItemClick(position: Int) {
        val task = allTasks?.find { task -> task.task_id == tasks?.get(position)?.task_id!! }
        view?.let { addGroupTask(it, task) }
    }

    private fun addGroupTask(view: View, task: Task?){
        arguments?.putSerializable("task", task)
        when(arguments?.getString("type")){
            "one_time" -> {
                if(arguments?.getString("category") == "work")
                    Navigation.findNavController(view).navigate(R.id.add_one_time_work_task, arguments)
                else Navigation.findNavController(view).navigate(R.id.add_one_time_other_task, arguments)
            }
            "fixed" -> {
                Navigation.findNavController(view).navigate(R.id.add_fixed_task, arguments)
            }
            "routine" -> {
                Navigation.findNavController(view).navigate(R.id.add_routine_task, arguments)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.edit_item -> {
                showDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Новая составная задача")

        val input = EditText(this.context)

        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(this.group.title)
        builder.setView(input)

        builder.setPositiveButton("Сохранить") { _, _ ->
            this.group.title = input.text.toString()
            groupViewModel.update(this.group)
            (activity as AppCompatActivity).supportActionBar?.title = group.title
        }
        builder.setNegativeButton("Назад") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}