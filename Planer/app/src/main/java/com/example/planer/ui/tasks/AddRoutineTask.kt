package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
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
import com.example.planer.util.MySharePreferences
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_add_routine_task.view.*
import java.lang.Exception
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.properties.Delegates

class AddRoutineTask : Fragment(), FilesRecyclerAdapter.OnItemClickListener
{
    private val taskViewModel: TaskViewModel by viewModels()
    private var checkTasks: MutableList<Task>? = null
    private val pathViewModel: PathViewModel by viewModels()
    private var files: MutableList<PathToFile> = mutableListOf()
    private var adapter: FilesRecyclerAdapter? = null
    private lateinit var list: RecyclerView
    private var count by Delegates.notNull<Int>()
    private val PICKFILE_RESULT_CODE = 1
    private var task: Task? = null
    private lateinit var mySharePreferences: MySharePreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_routine_task, container, false)
        task = arguments?.getSerializable("task") as Task?

        if (savedInstanceState != null) {
            this.files = savedInstanceState.getSerializable("files") as MutableList<PathToFile>
        }

        adapter = this.context?.let { FilesRecyclerAdapter(it, files, this) }
        list = view.files_recycler_view_routine
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let {
            ContextCompat.getDrawable(
                    it,
                    R.color.white
            )
        }!!)
        list.addItemDecoration(decoration)

        mySharePreferences = activity?.applicationContext?.let { MySharePreferences(it) }!!

        initUI(view)
        initButtons(view)
        initTask(view, task, savedInstanceState)

        taskViewModel.allTasks.observe(
                viewLifecycleOwner, {
            if (it != null) {
                this.checkTasks = it as MutableList<Task>?
            }
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

        taskViewModel.lastTask.observe(
                viewLifecycleOwner, {
        }
        )

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

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("files", ArrayList(this.files))
        outState.putString("begin_work_time", view?.begin_work_time?.text.toString())
        outState.putString("end_work_time", view?.end_work_time?.text.toString())
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
                    pathFile?.let { context?.contentResolver?.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION) }
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
        try {
            context?.contentResolver?.openInputStream(uri)
            val intent = Intent(Intent.ACTION_VIEW)
                    .setDataAndType(uri, "application/*")
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context?.startActivity(intent)
        } catch (e: Exception){
            this.context?.let { InfoDialog.onCreateDialog(it, "Ошибка", "Невозможно открыть файл! Возможно, файл был удален с устройства.", R.drawable.blue_info) }
        }
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
                this.view.let { it?.let { it1 -> saveTask(it1, task) } }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NewApi")
    private fun initTask(view: View, task: Task?, savedInstanceState: Bundle?){
        if(task != null) {
            view.task_title.setText(task.title)
            view.task_description.setText(task.description)
            view.begin_work_time.text = task.begin
            view.end_work_time.text = task.end
            view.checkBoxMon.isChecked = task.monday!!
            view.checkBoxTue.isChecked = task.tuesday!!
            view.checkBoxWed.isChecked = task.wednesday!!
            view.checkBoxThu.isChecked = task.thursday!!
            view.checkBoxFri.isChecked = task.friday!!
            view.checkBoxSat.isChecked = task.saturday!!
            view.checkBoxSun.isChecked = task.sunday!!
        }
        if (savedInstanceState != null) {
            view.begin_work_time?.text = savedInstanceState.getString("begin_work_time")
            view.end_work_time?.text = savedInstanceState.getString("end_work_time")
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initUI(view: View)
    {
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#13A678")))

        var color: Int? = this.context?.let { ContextCompat.getColor(it, R.color.blue) }
        when(arguments?.getString("category")){
            "rest" ->{
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_green) }!!
            }
            "other" ->{
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_orange) }!!
            }
        }
        color?.let { view.begin_work_button.setBackgroundColor(it) }
        color?.let { view.end_work_button.setBackgroundColor(it) }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButtons(view: View)
    {
        view.add_file_routine.setOnClickListener{
            val openDocumentIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            openDocumentIntent.addCategory(Intent.CATEGORY_OPENABLE)
            openDocumentIntent.type = "*/*"
            openDocumentIntent.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            startActivityForResult(openDocumentIntent, 1)
        }

        view.begin_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.begin_work_time, it1) }
        }

        view.end_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.end_work_time, it1) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View, task: Task?)
    {
        checkTasks?.removeIf { it.type == "one_time" || it.title == ""}
        checkTasks?.removeIf { !(it.monday == view.checkBoxMon.isChecked || it.tuesday == view.checkBoxTue.isChecked
                || it.wednesday == view.checkBoxWed.isChecked || it.thursday == view.checkBoxThu.isChecked
                || it.friday == view.checkBoxFri.isChecked || it.saturday == view.checkBoxSat.isChecked
                || it.sunday == view.checkBoxSun.isChecked) && it.type == "routine"}

        checkTasks?.removeIf {
            val newTaskBegin = LocalTime.parse(view.begin_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val newTaskEnd = LocalTime.parse(view.end_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val taskBegin = LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val taskEnd = LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            !((newTaskBegin >= taskBegin && newTaskBegin < taskEnd) || (newTaskEnd > taskBegin && newTaskEnd <= taskEnd))
        }

        if(task != null){
            checkTasks?.removeIf { it.task_id == task.task_id }
        }

        checkFixed()

        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.begin_work_time.text.isNotEmpty() && view.end_work_time.text.isNotEmpty()){
            val time1 = LocalTime.parse(view.begin_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
            val time2 = LocalTime.parse(view.end_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))

            if (view.task_title.text.isNotEmpty()){
                if(time1 > time2 || time1 == time2){
                    this.context?.let { ToastMessages.showMessage(it, "Неверно введено время начала или окончания") }
                } else {
                    if (view.checkBoxMon.isChecked || view.checkBoxTue.isChecked || view.checkBoxWed.isChecked || view.checkBoxThu.isChecked ||
                            view.checkBoxFri.isChecked || view.checkBoxSat.isChecked || view.checkBoxSun.isChecked)
                    {
                        if(checkTasks.isNullOrEmpty()){
                            if(checkEat(view, mySharePreferences.getBreakfast().toString(), mySharePreferences.getBreakfastEnd().toString())
                                    && checkEat(view, mySharePreferences.getLunch().toString(), mySharePreferences.getLunchEnd().toString())
                                    && checkEat(view, mySharePreferences.getDiner().toString(), mySharePreferences.getDinerEnd().toString())){
                                if(task != null){
                                    task.title = view.task_title.text.toString()
                                    task.description = view.task_description.text.toString()
                                    task.begin = view.begin_work_time.text.toString()
                                    task.end = view.end_work_time.text.toString()
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
                                            "routine",
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
                                            false,
                                            null,
                                            view.begin_work_time.text.toString(),
                                            view.end_work_time.text.toString(),
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
                            } else this.context?.let { InfoDialog.onCreateDialog(it, "Прием пищи", "Время задачи совпадает с приемом пищи. Для добавления задачи измените время примема пищи в разделе \"Профиль\".", R.drawable.info_green) }
                        } else this.context?.let { InfoDialog.onCreateDialog(it, "Совпадение задач", "Вы уже добавили задачу на это время.", R.drawable.info_green) }
                    } else this.context?.let { ToastMessages.showMessage(it, "Необходимо выбрать хотя бы один день недели") }
                }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
        } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести время начала и окончания") }
    }

    private fun checkEat(view: View, beginTime: String, endTime: String): Boolean{
        return !((view.begin_work_time.text.toString() >= beginTime
                && view.begin_work_time.text.toString() < endTime)
                || (view.end_work_time.text.toString() > beginTime
                && view.end_work_time.text.toString() <= endTime))
    }

    override fun onItemClick(position: Int) {
        val file = files[position]
        openFile(Uri.parse(file.path))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkFixed(){
        checkTasks?.forEach {
            if(it.type == "fixed"){
                when(LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).dayOfWeek){
                    DayOfWeek.MONDAY-> {
                        checkTasks?.removeIf { !view?.checkBoxMon?.isChecked!!}
                    }
                    DayOfWeek.TUESDAY -> {
                        checkTasks?.removeIf { !view?.checkBoxTue?.isChecked!!}
                    }
                    DayOfWeek.WEDNESDAY -> {
                        checkTasks?.removeIf { !view?.checkBoxWed?.isChecked!!}
                    }
                    DayOfWeek.THURSDAY -> {
                        checkTasks?.removeIf { !view?.checkBoxThu?.isChecked!!}
                    }
                    DayOfWeek.FRIDAY -> {
                        checkTasks?.removeIf { !view?.checkBoxFri?.isChecked!!}
                    }
                    DayOfWeek.SATURDAY -> {
                        checkTasks?.removeIf { !view?.checkBoxSat?.isChecked!!}
                    }
                    DayOfWeek.SUNDAY -> {
                        checkTasks?.removeIf { !view?.checkBoxSun?.isChecked!!}
                    }
                }
            }
            if(checkTasks.isNullOrEmpty()){
                return
            }
        }
    }
}