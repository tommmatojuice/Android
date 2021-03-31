package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.planer.R
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.*
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.task_description
import kotlinx.android.synthetic.main.fragment_add_one_time_other_task.view.task_title

class AddOneTimeOtherTask  : Fragment()
{
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var myView: View
    private var task: Task? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_one_time_other_task, container, false)
        myView = view
        task = arguments?.getSerializable("task") as Task?

        initUI(view)
        initButtons(view, task)
        initTask(view, task)

        return view
    }

    //Сохрание
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.save_item -> {
                Log.d("click", "click")
                myView.let { saveTask(it, task) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initTask(view: View, task: Task?){
        if(task != null) {
            view.task_title.setText(task.title)
            view.task_description.setText(task.description)
            view.priority.isChecked = task.priority
            view.checkBoxMon.isChecked = task.monday!!
            view.checkBoxTue.isChecked = task.tuesday!!
            view.checkBoxWed.isChecked = task.wednesday!!
            view.checkBoxThu.isChecked = task.thursday!!
            view.checkBoxFri.isChecked = task.friday!!
            view.checkBoxSat.isChecked = task.saturday!!
            view.checkBoxSun.isChecked = task.sunday!!
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initUI(view: View)
    {
        var color: Int? = this.context?.let { ContextCompat.getColor(it, R.color.blue) }
        when(arguments?.getString("category")){
            "rest" ->{
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_green) }!!
            }
            "other" ->{
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_orange) }!!
            }
        }
//        color?.let { view.save_button.setBackgroundColor(it) }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun initButtons(view: View, task: Task?)
    {
//        view.save_button.setOnClickListener {
//            saveTask(view, task)
//        }
    }

    private fun saveTask(view: View, task: Task?){
        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.task_title.text.isNotEmpty()){
            if (view.checkBoxMon.isChecked || view.checkBoxTue.isChecked || view.checkBoxWed.isChecked || view.checkBoxThu.isChecked ||
                    view.checkBoxFri.isChecked || view.checkBoxSat.isChecked || view.checkBoxSun.isChecked){
                if(task != null){
                    task.title = view.task_title.text.toString()
                    task.description = view.task_description.text.toString()
                    task.priority = view.priority.isChecked
                    task.monday =view.checkBoxMon.isChecked
                    task.tuesday =view.checkBoxTue.isChecked
                    task.wednesday = view.checkBoxWed.isChecked
                    task.thursday = view.checkBoxThu.isChecked
                    task.friday = view.checkBoxFri.isChecked
                    task.saturday = view.checkBoxSat.isChecked
                    task.sunday = view.checkBoxSun.isChecked
                    task.let { taskViewModel.update(it) }
                } else {
                    taskViewModel.insert(Task(
                            "one_time",
                            view.task_title.text.toString(),
                            view.task_description.text.toString(),
                            arguments?.getString("category").toString(),
                            null,
                            null,
                            null,
                            view.checkBoxMon.isChecked ,
                            view.checkBoxTue.isChecked ,
                            view.checkBoxWed.isChecked ,
                            view.checkBoxThu.isChecked ,
                            view.checkBoxFri.isChecked ,
                            view.checkBoxSat.isChecked ,
                            view.checkBoxSun.isChecked ,
                            view.priority.isChecked,
                            null,
                            null,
                            null,
                            group
                    )
                    )
                }

                val navBuilder = NavOptions.Builder()
                if(group == null && arguments?.getBoolean("back") == false) {
                    arguments?.putString("choice", "all")
                    val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.all_tasks, true).build()
                    Navigation.findNavController(view).navigate(R.id.all_tasks, arguments, navOptions)
                } else if(arguments?.getBoolean("back") == true){
                    Navigation.findNavController(view).navigate(R.id.navigation_plan)
                } else {
                    arguments?.putString("choice", "groups")
                    val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.group_tasks, true).build()
                    Navigation.findNavController(view).navigate(R.id.group_tasks, arguments, navOptions)
                }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо выбрать хотя бы один день недели") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
    }
}