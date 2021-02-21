package com.example.planer.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.planer.database.MyDataBase
import com.example.planer.database.dao.GroupTaskDao
import com.example.planer.database.dao.TaskDao
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndGroup
import kotlinx.coroutines.CoroutineScope

class GroupTaskRepository(context: Context, scope: CoroutineScope, category: String, type: String)
{
    private val groupeDao: GroupTaskDao
    val allGroupe: LiveData<List<GroupTask>>
    val tasksWithGroup: LiveData<List<GroupAndAllTasks>>

    init {
        val database: MyDataBase = MyDataBase.getDatabase(context, scope)
        groupeDao = database.groupTaskDao()
        allGroupe = groupeDao.allGroups()
        tasksWithGroup = groupeDao.tasksWithGroup(category, type)
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