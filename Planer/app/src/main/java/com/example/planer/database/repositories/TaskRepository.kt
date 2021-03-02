package com.example.planer.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.planer.database.MyDataBase
import com.example.planer.database.dao.TaskDao
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndGroup
import kotlinx.coroutines.CoroutineScope

class TaskRepository(context: Context, scope: CoroutineScope)
{
    private val taskDao: TaskDao
    val allTasks: LiveData<List<Task>>
    val lastTask: LiveData<Task>
    val routineMon: LiveData<List<Task>>
    val routineTue: LiveData<List<Task>>
    val routineWen: LiveData<List<Task>>
    val routineThu: LiveData<List<Task>>
    val routineFri: LiveData<List<Task>>
    val routineSat: LiveData<List<Task>>
    val routineSun: LiveData<List<Task>>

    init {
        val database: MyDataBase = MyDataBase.getDatabase(context, scope)
        taskDao = database.taskDao()
        allTasks = taskDao.allTasks()
        lastTask = taskDao.getLastTask()
        routineMon = taskDao.routineMon()
        routineTue = taskDao.routineTue()
        routineWen = taskDao.routineWen()
        routineThu = taskDao.routineThu()
        routineFri = taskDao.routineFri()
        routineSat = taskDao.routineSat()
        routineSun = taskDao.routineSun()
    }

    fun fixedTasksByDate(date: String): LiveData<List<Task>> {
        return taskDao.fixedTasksByDate(date)
    }

    fun taskById(id: Int): LiveData<Task> {
        return taskDao.taskById(id)
    }

    fun taskAndGroup(category: String, type: String): LiveData<List<TaskAndGroup>> {
        return taskDao.taskAndGroup(category, type)
    }

    fun taskByGroup(id: Int): LiveData<List<TaskAndGroup>> {
        return taskDao.taskByGroup(id)
    }

    fun insert(task: Task){
        taskDao.insert(task)
    }

    fun update(task: Task){
        taskDao.update(task)
    }

    fun delete(task: Task){
        taskDao.delete(task)
    }
}