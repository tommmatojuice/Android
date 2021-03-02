package com.example.planer.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.planer.database.MyDataBase
import com.example.planer.database.dao.PathToFileDao
import com.example.planer.database.dao.TaskDao
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.entity.Task
import kotlinx.coroutines.CoroutineScope

class PathToFileRepository(context: Context, scope: CoroutineScope)
{
    private val pathDao: PathToFileDao
    val allPaths: LiveData<List<PathToFile>>

    init {
        val database: MyDataBase = MyDataBase.getDatabase(context, scope)
        pathDao = database.pathToFileDao()
        allPaths = pathDao.allPaths()
    }

    fun pathsById(id: Int): LiveData<List<PathToFile>> {
        return pathDao.pathsById(id)
    }

    fun insert(path: PathToFile){
        pathDao.insert(path)
    }

    fun update(path: PathToFile){
        pathDao.update(path)
    }

    fun delete(path: PathToFile){
        pathDao.delete(path)
    }
}