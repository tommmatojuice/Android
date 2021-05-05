package com.example.planer.ui.plan

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.PlanRecyclerAdapter
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.InfoDialog
import com.example.planer.util.MySharePreferences
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_food.view.*
import kotlinx.android.synthetic.main.fragment_food.view.button_add_item
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class UserPlan(private val date: String, private val weekDay: String): Fragment(), PlanRecyclerAdapter.OnItemClickListener, PlanRecyclerAdapter.OnItemLongClickListener {
    private lateinit var mySharePreferences: MySharePreferences
    private val taskViewModel: TaskViewModel by viewModels()
    private var fixedTasks: List<Task>? = listOf()
    private var routineTasks: List<Task>? = listOf()
    private var tasks: MutableList<TasksForPlan> = mutableListOf()
    private var adapter: PlanRecyclerAdapter? = null
    private lateinit var list: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_task_recycler, container, false)
        mySharePreferences = context?.let { MySharePreferences(it) }!!

        adapter = this.context?.let { PlanRecyclerAdapter(it, tasks, this, this) }
        list = view.task_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            showCategoryDialog()
        }

        taskViewModel.fixedTasksByDate(date).observe(
                viewLifecycleOwner, { fixedTasks ->
            if (fixedTasks != null) {
                this.fixedTasks = fixedTasks
                Log.d("fixedTasks", this.fixedTasks!!.size.toString())
                getRoutineTasks()
            }
        }
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val task = tasks[viewHolder.adapterPosition].task

                val myClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        Dialog.BUTTON_POSITIVE -> {
                            if (task != null) {
                                taskViewModel.delete(task)
                            }
                        }
                        Dialog.BUTTON_NEGATIVE -> {
                            tasks.let { adapter?.setTasks(it) }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRoutineTasks(){
        when(weekDay){
            "MONDAY" -> {
                taskViewModel.tasksMon("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                        Log.d("routineTasks", this.routineTasks?.size.toString())
                        initTasks()
                    }
                })
            }
            "TUESDAY" -> {
                taskViewModel.tasksTue("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                        Log.d("routineTasks", this.routineTasks?.size.toString())
                        initTasks()
                    }
                }
                )
            }
            "WEDNESDAY" -> {
                taskViewModel.tasksWen("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                        Log.d("routineTasks", this.routineTasks?.size.toString())
                        initTasks()
                    }
                }
                )
            }
            "THURSDAY" -> {
                taskViewModel.tasksThu("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                        Log.d("routineTasks", this.routineTasks?.size.toString())
                        initTasks()
                    }
                })
            }
            "FRIDAY" -> {
                taskViewModel.tasksFri("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                        Log.d("routineTasks", this.routineTasks?.size.toString())
                        initTasks()
                    }
                })
            }
            "SATURDAY" -> {
                taskViewModel.tasksSat("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                        Log.d("routineTasks", this.routineTasks?.size.toString())
                        initTasks()
                    }
                })
            }
            "SUNDAY" -> {
                taskViewModel.tasksSun("routine").observe(
                        viewLifecycleOwner, { routineTasks ->
                    if (routineTasks != null) {
                        this.routineTasks = routineTasks
                        Log.d("routineTasks", this.routineTasks?.size.toString())
                        initTasks()
                    }
                })
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTasks(){
        this.tasks.clear()
        Log.d("fixedTasks1", this.fixedTasks?.size.toString())
        Log.d("routineTasks1", this.routineTasks?.size.toString())
        fixedTasks?.forEach {
            this.tasks.add(TasksForPlan(LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), null, it))
        }
        routineTasks?.forEach {
            this.tasks.add(TasksForPlan(LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                    LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), null, it))
        }
        Log.d("tasksTasks", this.tasks.size.toString())
        this.tasks.sortBy { it.begin }
        if(tasks.isNullOrEmpty()){
            view?.textView?.visibility = View.VISIBLE
            view?.imageView?.visibility = View.VISIBLE
        } else {
            view?.textView?.visibility = View.INVISIBLE
            view?.imageView?.visibility = View.INVISIBLE
        }
        adapter?.setTasks(tasks)
        list.adapter = adapter
    }

    private fun addTask(category: String?)
    {
        val bundle = Bundle().apply { putString("type", "fixed") }
                .apply { putString("category", category) }
                .apply { putString("date", date) }
                .apply { putBoolean("back", true) }
        this.view?.let { Navigation.findNavController(it).navigate(R.id.add_fixed_task, bundle) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int) {
        view?.let { openTask(it, tasks[position].task) }
    }

    private fun openTask(view: View, task: Task?)
    {
        val bundle = Bundle().apply { putString("type", task?.type) }
                .apply { putString("category", task?.category) }
                .apply { putSerializable("task", task) }
                .apply { putBoolean("back", true) }
        when(task?.type){
            "one_time" -> {
                if (task.category == "work")
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

    private fun showCategoryDialog(){
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builderSingle.setIcon(R.drawable.plan_color)
        builderSingle.setTitle("Категория")

        val arrayAdapter = this.context?.let {
            ArrayAdapter<String>(
                    it,
                android.R.layout.select_dialog_singlechoice)
        }

        arrayAdapter?.add("Работа")
        arrayAdapter?.add("Отдых")
        arrayAdapter?.add("Другое")

        builderSingle.setNegativeButton("Отмена",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builderSingle.setAdapter(arrayAdapter,
                DialogInterface.OnClickListener { dialog, which ->
                    val category = when(arrayAdapter?.getItem(which)!!){
                        "Работа" ->  "work"
                        "Отдых" ->  "rest"
                        "Другое" -> "other"
                        else -> null
                    }
                    addTask(category)
                    Log.i("Selected Item ", arrayAdapter?.getItem(which)!!)
                    dialog.dismiss()
                })
        builderSingle.show()
    }

    override fun onItemLongClicked(position: Int): Boolean {
        return true
    }
}