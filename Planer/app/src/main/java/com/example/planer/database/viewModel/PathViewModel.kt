package com.example.planer.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.repositories.PathToFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PathViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository = PathToFileRepository(application.applicationContext, viewModelScope)

    fun pathsById(id: Int): LiveData<List<PathToFile>> {
        return repository.pathsById(id)
    }

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