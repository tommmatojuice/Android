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
    val routineMon = repository.routineMon
    val routineTue = repository.routineTue
    val routineWen = repository.routineWen
    val routineThu = repository.routineThu
    val routineFri = repository.routineFri
    val routineSat = repository.routineSat
    val routineSun = repository.routineSun

    fun fixedTasksByDate(date: String): LiveData<List<Task>> {
        return repository.fixedTasksByDate(date)
    }

    fun taskById(id: Int): LiveData<Task> {
        return repository.taskById(id)
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