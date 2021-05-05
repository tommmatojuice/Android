package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.FilesRecyclerAdapter
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.PathViewModel
import com.example.planer.database.viewModel.TaskViewModel
import com.example.planer.util.InfoDialog
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.view.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.properties.Delegates

class AddOneTimeWorkTask : Fragment(), DatePickerDialog.OnDateSetListener, SeekBar.OnSeekBarChangeListener, FilesRecyclerAdapter.OnItemClickListener {
    private val taskViewModel: TaskViewModel by viewModels()
    private val pathViewModel: PathViewModel by viewModels()
    private var files: MutableList<PathToFile> = mutableListOf()
    private var adapter: FilesRecyclerAdapter? = null
    private lateinit var list: RecyclerView
    private var count by Delegates.notNull<Int>()
    private val PICKFILE_RESULT_CODE = 1
    private var task: Task? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_one_time_work_task, container, false)
        task = arguments?.getSerializable("task") as Task?

        adapter = this.context?.let { FilesRecyclerAdapter(it, files, this) }
        list = view.files_recycler_view_work_one_time
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let {
            ContextCompat.getDrawable(
                    it,
                    R.color.white
            )
        }!!)
        list.addItemDecoration(decoration)

        initUI(view)
        initButtons(view)
        initTask(view, task)

        task?.task_id?.let {
            pathViewModel.pathsById(it).observe(
                    viewLifecycleOwner, {
                if (it != null) {
                    this.files = it as MutableList<PathToFile>
                    count = files.size
                    adapter?.setFiles(files)
                    list.adapter = adapter
                }
            }
            )
        }

        taskViewModel.lastTask.observe(
                viewLifecycleOwner, {
        }
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val file = files.find { file -> file.path_id == files[viewHolder.adapterPosition].path_id }

                val myClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        Dialog.BUTTON_POSITIVE -> {
                            if (task != null) {
                                file?.let { pathViewModel.delete(it) }
                            }
                        }
                        Dialog.BUTTON_NEGATIVE -> {
                            files.let { adapter?.setFiles(it) }
                            list.adapter = adapter
                        }
                    }
                }
                context?.let { InfoDialog.onCreateConfirmDialog(it, "Удаление", "Удалить прикрепленный файл?", R.drawable.delete_blue, myClickListener)}
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)

        return view
    }

    private fun addFiles(task_id: Int){
        files.forEach {
            if(task != null){
                if(count <= 0){
                    pathViewModel.insert(PathToFile(it.path, task_id))
                }
                count--
            } else {
                pathViewModel.insert(PathToFile(it.path, task_id))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            PICKFILE_RESULT_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val pathFile = data?.data
                    if(files.find{it.path == pathFile.toString()} != null){
                        this.context?.let { InfoDialog.onCreateDialog(it, "Внимание", "Вы уже прикрепили этот файл!", R.drawable.blue_info) }
                    } else {
                        files.add(PathToFile(pathFile.toString(), -1))
                        adapter?.setFiles(files)
                        list.adapter = adapter
                    }
                }
            }
        }
    }

    private fun openFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, "application/*")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context?.startActivity(intent)
    }

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
                this.view.let { it?.let { it1 -> saveTask(it1, task) } }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NewApi", "SetTextI18n")
    private fun initTask(view: View, task: Task?){
        if(task != null) {
            val hours: String = if(task.duration?.div(60)!! < 10)
                "0" + task.duration?.div(60)
            else task.duration?.div(60).toString()
            val minutes: String = if(task.duration?.rem(60)!! < 10)
                "0" + task.duration?.rem(60)
            else task.duration?.rem(60).toString()

            view.task_title.setText(task.title)
            view.task_description.setText(task.description)
            view.work_time.text = "$hours:$minutes"
            view.count.text = task.complexity.toString()
            view.deadline.text = task.deadline
            task.complexity?.let { view.difficulty.setProgress(it, false) }
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
        color?.let { view.work_button.setBackgroundColor(it) }
        color?.let { view.deadline_button.setBackgroundColor(it) }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButtons(view: View)
    {
        view.add_file_work_one_time.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICKFILE_RESULT_CODE)
        }

        view.work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.work_time, it1) }
        }

        view.deadline_button.setOnClickListener {
            val datePicker = this.context?.let { it1 ->
                DatePickerDialog(it1,
                        this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
            }
            datePicker?.show()
        }

        view.difficulty.setOnSeekBarChangeListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int)
    {
        val newMonth = month+1
        if(newMonth < 10){
            if (day < 10){
                view?.deadline?.text = "$year-0$newMonth-0$day"
            } else view?.deadline?.text = "$year-0$newMonth-$day"
        } else {
            if (day < 10){
                view?.deadline?.text = "$year-$newMonth-0$day"
            } else view?.deadline?.text = "$year-$newMonth-$day"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View, task: Task?)
    {
        val time = LocalTime.parse(view.work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.task_title.text.isNotEmpty()){
            if (view.work_time.text.isNotEmpty()){
                    if (view.deadline.text.isNotEmpty()){
                        if (view.checkBoxMon.isChecked || view.checkBoxTue.isChecked || view.checkBoxWed.isChecked || view.checkBoxThu.isChecked ||
                                view.checkBoxFri.isChecked || view.checkBoxSat.isChecked || view.checkBoxSun.isChecked) {
                            if(task != null){
                                task.title = view.task_title.text.toString()
                                task.description = view.task_description.text.toString()
                                task.duration = time.hour*60 + time.minute
                                task.complexity = view.count.text.toString().toInt()
                                task.deadline = view.deadline.text.toString()
                                task.monday =view.checkBoxMon.isChecked
                                task.tuesday =view.checkBoxTue.isChecked
                                task.wednesday = view.checkBoxWed.isChecked
                                task.thursday = view.checkBoxThu.isChecked
                                task.friday = view.checkBoxFri.isChecked
                                task.saturday = view.checkBoxSat.isChecked
                                task.sunday = view.checkBoxSun.isChecked
                                task.let { taskViewModel.update(it) }
                                addFiles(task.task_id)
                            } else {
                                taskViewModel.insert(Task(
                                        "one_time",
                                        view.task_title.text.toString(),
                                        view.task_description.text.toString(),
                                        arguments?.getString("category").toString(),
                                        view.deadline.text.toString(),
                                        time.hour*60 + time.minute,
                                        view.count.text.toString().toInt(),
                                        view.checkBoxMon.isChecked ,
                                        view.checkBoxTue.isChecked ,
                                        view.checkBoxWed.isChecked ,
                                        view.checkBoxThu.isChecked ,
                                        view.checkBoxFri.isChecked ,
                                        view.checkBoxSat.isChecked ,
                                        view.checkBoxSun.isChecked ,
                                        false,
                                        null,
                                        null,
                                        null,
                                        group
                                )
                                )
                                taskViewModel.lastTask.value?.task_id?.plus(1)?.let { addFiles(it) }
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
                    } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести дедлайн") }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести время выполнения") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
    }

    override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
        view?.count?.text = seekBar?.progress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        view?.count?.text = seekBar?.progress.toString()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        view?.count?.text = seekBar?.progress.toString()
    }

    override fun onItemClick(position: Int) {
        val file = files[position]
        openFile(Uri.parse(file.path))
    }
}