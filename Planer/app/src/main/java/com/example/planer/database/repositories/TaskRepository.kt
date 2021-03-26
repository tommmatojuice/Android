
package com.example.planer.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Query
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

    init {
        val database: MyDataBase = MyDataBase.getDatabase(context, scope)
        taskDao = database.taskDao()
        allTasks = taskDao.allTasks()
        lastTask = taskDao.getLastTask()
    }

    fun checkTimeFixed(begin: String, end: String, date: String): LiveData<List<Task>>{
        return taskDao.checkTimeFixed(begin, end, date)
    }

    fun checkTimeRoutine(begin: String, end: String, monday: Int, tuesday: Int, wednesday: Int, thursday: Int, friday: Int, saturday: Int, sunday: Int): LiveData<List<Task>>{
        return taskDao.checkTimeRoutine(begin, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
    }

    fun tasksMon(type: String): LiveData<List<Task>> {
        return taskDao.tasksMon(type)
    }

    fun tasksTue(type: String): LiveData<List<Task>> {
        return taskDao.tasksTue(type)
    }

    fun tasksWen(type: String): LiveData<List<Task>> {
        return taskDao.tasksWen(type)
    }

    fun tasksThu(type: String): LiveData<List<Task>> {
        return taskDao.tasksThu(type)
    }

    fun tasksFri(type: String): LiveData<List<Task>> {
        return taskDao.tasksFri(type)
    }

    fun tasksSat(type: String): LiveData<List<Task>> {
        return taskDao.tasksSat(type)
    }

    fun tasksSun(type: String): LiveData<List<Task>> {
        return taskDao.tasksSun(type)
    }

//    fun oneTimeTasks(category: String, monday: Int, tuesday: Int, wednesday: Int, thursday: Int, friday: Int, saturday: Int, sunday: Int): LiveData<List<Task>> {
//        return taskDao.oneTimeTasks(category, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
//    }

    fun getByCategoryAndType(category: String, type: String): LiveData<List<Task>> {
        return taskDao.getByCategoryAndType(category, type)
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