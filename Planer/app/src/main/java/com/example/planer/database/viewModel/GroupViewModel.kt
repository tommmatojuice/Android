package com.example.planer.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.repositories.GroupTaskRepository
import com.example.planer.database.repositories.PathToFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository = GroupTaskRepository(application.applicationContext, viewModelScope)

    val allGroups = repository.allGroupe
//    val tasksWithGroup = repository.tasksWithGroup
    val lastGroup = repository.lastGroup

    fun tasksWithGroup(category: String, type: String): LiveData<List<GroupAndAllTasks>> {
        return repository.tasksWithGroup(category, type)
    }

    fun insert(group: GroupTask) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(group)
    }

    fun update(group: GroupTask) = viewModelScope.launch(Dispatchers.IO){
        repository.update(group)
    }

    fun delete(group: GroupTask) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(group)
    }
}