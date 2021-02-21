package com.example.planer.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.repositories.GroupTaskRepository
import com.example.planer.database.repositories.PathToFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewModel(application: Application, category: String, type: String) : AndroidViewModel(application)
{
    private val repository = GroupTaskRepository(application.applicationContext, viewModelScope, category, type)

    val allGroups = repository.allGroupe
    val tasksWithGroup = repository.tasksWithGroup

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