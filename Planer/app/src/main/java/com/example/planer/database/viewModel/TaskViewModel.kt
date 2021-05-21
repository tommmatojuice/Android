package com.example.planer.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndGroup
import com.example.planer.database.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository = TaskRepository(application.applicationContext, viewModelScope)

    val allTasks = repository.allTasks
    val lastTask = repository.lastTask

    fun tasksMon(type: String): LiveData<List<Task>> {
        return repository.tasksMon(type)
    }

    fun tasksTue(type: String): LiveData<List<Task>> {
        return repository.tasksTue(type)
    }

    fun tasksWen(type: String): LiveData<List<Task>> {
        return repository.tasksWen(type)
    }

    fun tasksThu(type: String): LiveData<List<Task>> {
        return repository.tasksThu(type)
    }

    fun tasksFri(type: String): LiveData<List<Task>> {
        return repository.tasksFri(type)
    }

    fun tasksSat(type: String): LiveData<List<Task>> {
        return repository.tasksSat(type)
    }

    fun tasksSun(type: String): LiveData<List<Task>> {
        return repository.tasksSun(type)
    }

    fun fixedTasksByDate(date: String): LiveData<List<Task>> {
        return repository.fixedTasksByDate(date)
    }

    fun taskAndGroup(category: String, type: String): LiveData<List<TaskAndGroup>> {
        return repository.taskAndGroup(category, type)
    }

    fun taskByGroup(id: Int): LiveData<List<TaskAndGroup>> {
        return repository.taskByGroup(id)
    }

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(task)
    }

    fun update(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(task)
    }
}