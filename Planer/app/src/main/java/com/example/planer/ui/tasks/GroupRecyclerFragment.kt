package com.example.planer.ui.tasks

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.GroupRecyclerAdapter
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.InfoDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_group_recycler.view.*
import kotlinx.android.synthetic.main.fragment_task_recycler.view.button_add_item

class GroupRecyclerFragment(private var type: String, private var category: String) : Fragment(), GroupRecyclerAdapter.OnItemClickListener
{
    private val groupViewModel: GroupViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private var groups: List<GroupAndAllTasks>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_group_recycler, container, false)

        val adapter = this.context?.let { GroupRecyclerAdapter(it, groups, this) }
        val list = view.group_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            addGroup()
        }

        groupViewModel.tasksWithGroup(category, type).observe(
                viewLifecycleOwner, {
            if (it != null) {
                this.groups = it
                adapter?.setTasks(it)
                list.adapter = adapter
            }
        }
        )

        groupViewModel.lastGroup.observe(
                viewLifecycleOwner, {
        }
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val id = groups!![viewHolder.adapterPosition].group.group_task_id
                val group =  groups?.find { group -> group.group.group_task_id == id}?.group

                val myClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        Dialog.BUTTON_POSITIVE -> {
                            group?.let { groupViewModel.delete(it) }
                        }
                        Dialog.BUTTON_NEGATIVE -> {
                            groups?.let { adapter?.setTasks(it) }
                            list.adapter = adapter
                        }
                    }
                }

                context?.let { InfoDialog.onCreateConfirmDialog(it, "Удаление", "Удалить составную задачу \"${group?.title}\"?", R.drawable.delete, myClickListener)}
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)

        return view
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle().apply { putSerializable("main_group", groups?.get(position)?.group) }
                .apply { putString("category", category) }.apply { putString("type", type) }
        this.view?.let { Navigation.findNavController(it).navigate(R.id.group_tasks, bundle) }
    }

    private fun addGroup()
    {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Новая составная задача")

        val input = EditText(this.context)

        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Сохранить") { _, _ ->
            groupViewModel.insert(GroupTask(input.text.toString()))
            Handler().postDelayed(Runnable { addTask() }, 100)
        }
        builder.setNegativeButton("Назад") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun addTask(){
        taskViewModel.insert(Task(type, "", null, category, null,
                null, null, null, null, null, null, null, null,
                null, false, null, null, null, groupViewModel.lastGroup.value?.group_task_id))
    }
}
