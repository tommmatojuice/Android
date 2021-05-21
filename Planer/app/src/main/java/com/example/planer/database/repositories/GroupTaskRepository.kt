package com.example.planer.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.planer.database.MyDataBase
import com.example.planer.database.dao.GroupTaskDao
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import kotlinx.coroutines.CoroutineScope

class GroupTaskRepository(context: Context, scope: CoroutineScope)
{
    private val groupeDao: GroupTaskDao
    val allGroupe: LiveData<List<GroupTask>>
    val lastGroup: LiveData<GroupTask>

    init {
        val database: MyDataBase = MyDataBase.getDatabase(context, scope)
        groupeDao = database.groupTaskDao()
        allGroupe = groupeDao.allGroups()
        lastGroup = groupeDao.getLastGroup()
    }

    fun tasksWithGroup(category: String, type: String): LiveData<List<GroupAndAllTasks>> {
        return groupeDao.tasksWithGroup(category, type)
    }

    fun insert(group: GroupTask){
        groupeDao.insert(group)
    }

    fun update(group: GroupTask){
        groupeDao.update(group)
    }

    fun delete(group: GroupTask){
        groupeDao.delete(group)
    }
}