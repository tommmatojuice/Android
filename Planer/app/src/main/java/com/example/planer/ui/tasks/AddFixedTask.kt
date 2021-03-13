package com.example.planer.ui.tasks

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
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
import com.example.planer.util.TimeDialog
import com.example.planer.util.ToastMessages
import kotlinx.android.synthetic.main.fragment_add_fixed_task.view.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.properties.Delegates

class AddFixedTask() : Fragment(), DatePickerDialog.OnDateSetListener, FilesRecyclerAdapter.OnItemClickListener
{
    private val taskViewModel: TaskViewModel by viewModels()
    private val pathViewModel: PathViewModel by viewModels()
    private var files: MutableList<PathToFile> = mutableListOf()
    private var adapter: FilesRecyclerAdapter? = null
    private var count by Delegates.notNull<Int>()
    private lateinit var list: RecyclerView
    private val PICKFILE_RESULT_CODE = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.fragment_add_fixed_task, container, false)
        val task = arguments?.getSerializable("task") as Task?

        adapter = this.context?.let { FilesRecyclerAdapter(it, files, this) }
        list = view.files_recycler_view
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
        initButtons(view, task)
        initTask(view, task)

        task?.task_id?.let {
            pathViewModel.pathsById(it).observe(
                viewLifecycleOwner, {
                    if (it != null) {
                        this.files = it as MutableList<PathToFile>
                        count = files.size
                        adapter?.setTasks(files)
                        list.adapter = adapter
                    }
                }
            )
        }

        taskViewModel.lastTask.observe(
            viewLifecycleOwner, { _ ->
            }
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                viewHolder2: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val file = files.find { file -> file.path_id == files.get(viewHolder.adapterPosition).path_id }
                file?.let { pathViewModel.delete(it) }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)

        return view
    }

    private fun addFiles(task_id: Int){
        val task = taskViewModel.lastTask.value?.task_id
        files.forEach {
            if(count <= 0){
                if(task != null)
                    pathViewModel.insert(PathToFile(it.path, task_id))
            }
            count--
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            PICKFILE_RESULT_CODE -> {
                if (resultCode == RESULT_OK) {
                    val pathFile = data?.data
                    pathFile?.let { openFile(it) }
                    files.add(PathToFile(pathFile.toString(), -1))
                    adapter?.setTasks(files)
                    list.adapter = adapter
                    this.context?.let { it1 ->
                        ToastMessages.showMessage(
                            it1,
                            files.size.toString()
                        )
                    }
//                    view?.text_file?.text = pathFile?.toString()
//                    pathFile?.let { openFile(it) }
                }
            }
        }
    }

//    fun openFile(path: Uri) {
////        val intent = Intent(Intent.ACTION_VIEW)
////        intent.data = path
////        startActivity(intent)
//
//        val data = this.context?.let { FileProvider.getUriForFile(it, "com.example.myapp.fileprovider", File(path.path)) };
//        context?.grantUriPermission(this.context?.packageName, data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        val intent = Intent(Intent.ACTION_VIEW)
//                .setDataAndType(data, "*/*")
//                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        context?.startActivity(intent);
//
////        val uri = Uri.parse(path)
////        val intent = Intent(Intent.ACTION_VIEW)
////        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
////        intent.setDataAndType(path, "*/*") // "video/mp4"
////
////        startActivity(intent)
//
////        val DIR_IMAGE = "/data/data/it.android.myprogram/images/"
////        val filePath = DIR_IMAGE + "fileName"
////        val fileInputStream = FileInputStream(File(filePath))
////        val fileOutputStream: FileOutputStream = openFileOutput("fileName", Activity.MODE_WORLD_READABLE)
////        ByteStreams.copy(fileInputStream, fileOutputStream)
////        val intent = Intent(Intent.ACTION_VIEW)
////        intent.setDataAndType(path, "*/*")
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun openFile(uri: Uri)
    {
        val src_uri = Uri.parse(uri.path)
        val dst_uri = Uri.parse("file:///mnt/sdcard/download/testing.pdf")
        val req = DownloadManager.Request(src_uri)
        req.setDestinationUri(dst_uri)
        val dm = this.context?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(req)

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        if (uri.toString().contains(".doc") || uri.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword")
        } else if (uri.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf")
        } else if (uri.toString().contains(".ppt") || uri.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        } else if (uri.toString().contains(".xls") || uri.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel")
        } else if (uri.toString().contains(".zip") || uri.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav")
        } else if (uri.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf")
        } else if (uri.toString().contains(".wav") || uri.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav")
        } else if (uri.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif")
        } else if (uri.toString().contains(".jpg") || uri.toString()
                .contains(".jpeg") || uri.toString().contains(".png")
        ) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg")
        } else if (uri.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain")
        } else if (uri.toString().contains(".3gp") || uri.toString()
                .contains(".mpg") || uri.toString().contains(".mpeg") || uri.toString()
                .contains(".mpe") || uri.toString().contains(".mp4") || uri.toString()
                .contains(".avi")
        ) {
            // Video files
            intent.setDataAndType(uri, "video/*")
        } else {
            // Other files
            intent.setDataAndType(uri, "*/*")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

//    fun openFile() {
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            type = "*/*"
//            addCategory(Intent.CATEGORY_OPENABLE)
//        }
//        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
//        startActivityForResult(intent, 1)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == Activity.RESULT_OK) {
//            val fullPhotoUri: Uri = data.data
//            // Do work with full size photo saved at fullPhotoUri
//            ...
//        }
//    }

    private fun initTask(view: View, task: Task?){
        if(task != null) {
            view.task_title.setText(task.title)
            view.task_description.setText(task.description)
            view.begin_work_time.text = task.begin
            view.end_work_time.text = task.end
            view.date.text = task.date
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initUI(view: View)
    {
        var color: Int? = this.context?.let { ContextCompat.getColor(it, R.color.blue) }
        when(arguments?.getString("category")){
            "rest" -> {
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_green) }!!
            }
            "other" -> {
                color = this.context?.let { ContextCompat.getColor(it, R.color.dark_orange) }!!
            }
        }
        color?.let { view.begin_work_button.setBackgroundColor(it) }
        color?.let { view.end_work_button.setBackgroundColor(it) }
        color?.let { view.deadline_button.setBackgroundColor(it) }
        color?.let { view.save_button.setBackgroundColor(it) }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initButtons(view: View, task: Task?)
    {
        view.save_button.setOnClickListener {
            saveTask(view, task)
        }

        view.begin_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.begin_work_time, it1) }
        }

        view.end_work_button.setOnClickListener {
            this.context?.let { it1 -> TimeDialog.getTime(view.end_work_time, it1) }
        }

        view.deadline_button.setOnClickListener {
            val datePicker = this.context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
            }
            datePicker?.show()
        }

        view.add_file.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICKFILE_RESULT_CODE)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int)
    {
        val newMonth = month+1
        if(newMonth < 10){
            if (day < 10){
                view?.date?.text = "$year-0$newMonth-0$day"
            } else view?.date?.text = "$year-0$newMonth-$day"
        } else {
            if (day < 10){
                view?.date?.text = "$year-$newMonth-0$day"
            } else view?.date?.text = "$year-$newMonth-$day"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTask(view: View, task: Task?)
    {
        val group: Int? = if(arguments?.getInt("group") == 0)
            null
        else arguments?.getInt("group")

        if (view.begin_work_time.text.isNotEmpty() && view.end_work_time.text.isNotEmpty()){
            val date1 = LocalTime.parse(
                view.begin_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(
                    FormatStyle.SHORT
                )
            )
            val date2 = LocalTime.parse(
                view.end_work_time.text.toString(), DateTimeFormatter.ofLocalizedTime(
                    FormatStyle.SHORT
                )
            )

            if (view.task_title.text.isNotEmpty()){
                if(date1 > date2 || date1 == date2){
                    this.context?.let { ToastMessages.showMessage(
                        it,
                        "Неверно введено время начала или окончания"
                    ) }
                } else {
                    if (view.date.text.isNotEmpty())
                    {
                        if(task != null){
                            task.title = view.task_title.text.toString()
                            task.description = view.task_description.text.toString()
                            task.date = view.date.text.toString()
                            task.begin = view.begin_work_time.text.toString()
                            task.end = view.end_work_time.text.toString()
                            task.let { taskViewModel.update(it) }
                            addFiles(task.task_id)
                        } else {
                            taskViewModel.insert(
                                Task(
                                    "fixed",
                                    view.task_title.text.toString(),
                                    view.task_description.text.toString(),
                                    arguments?.getString("category").toString(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    false,
                                    view.date.text.toString(),
                                    view.begin_work_time.text.toString(),
                                    view.end_work_time.text.toString(),
                                    group
                                )
                            )
                            taskViewModel.lastTask.value?.task_id?.plus(1)?.let { addFiles(it) }
                        }

                        val navBuilder = NavOptions.Builder()
                        when {
                            group == null -> {
                                arguments?.putString("choice", "all")
                                val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.all_tasks, true).build()
                                Navigation.findNavController(view).navigate(
                                        R.id.all_tasks,
                                        arguments,
                                        navOptions
                                )
                            }
                            arguments?.getBoolean("back") != null -> {
                                Navigation.findNavController(view).navigate(R.id.navigation_plan)
                            }
                            else -> {
                                arguments?.putString("choice", "groups")
                                val navOptions: NavOptions = navBuilder.setPopUpTo(
                                        R.id.group_tasks,
                                        true
                                ).build()
                                Navigation.findNavController(view).navigate(
                                        R.id.group_tasks,
                                        arguments,
                                        navOptions
                                )
                            }
                        }
                    } else this.context?.let { ToastMessages.showMessage(
                        it,
                        "Необходимо ввести дату события"
                    ) }
                }
            } else this.context?.let { ToastMessages.showMessage(it, "Необходимо ввести название") }
        } else this.context?.let { ToastMessages.showMessage(
            it,
            "Необходимо ввести время начала и окончания"
        ) }
    }

    override fun onItemClick(position: Int) {
        val file = files.find { file -> file.path_id == files[position].path_id }
        openFile(Uri.parse(file?.path))
    }
}