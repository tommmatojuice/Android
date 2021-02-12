package com.example.planer.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.entity.Task
import com.example.planer.database.repositories.PathToFileRepository
import com.example.planer.database.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PathViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository = PathToFileRepository(application.applicationContext, viewModelScope)

    val allPaths = repository.allPaths

    fun insert(path: PathToFile) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(path)
    }

    fun update(path: PathToFile) = viewModelScope.launch(Dispatchers.IO){
        repository.update(path)
    }

    fun delete(path: PathToFile) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(path)
    }
}