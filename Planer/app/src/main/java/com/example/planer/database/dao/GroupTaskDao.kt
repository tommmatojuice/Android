package com.example.planer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.TaskAndGroup

@Dao
interface GroupTaskDao
{
    @Insert()
    fun insert(task: GroupTask)

    @Update()
    fun update(task: GroupTask)

    @Delete()
    fun delete(task: GroupTask)

    @Query("DELETE FROM group_task_table")
    fun deleteAllGroups()

    @Query("SELECT * FROM group_task_table")
    fun allGroups(): LiveData<List<GroupTask>>

//    @Transaction
//    @Query("SELECT * FROM group_task_table")
//    fun tasksWithGroup(): LiveData<List<GroupAndAllTasks>>

    @Transaction
    @Query("SELECT DISTINCT group_task_id, group_task_table.title FROM group_task_table, task_table WHERE group_task_id = `group` AND category = :category AND type = :type ORDER BY group_task_table.title")
    fun tasksWithGroup(category: String, type: String): LiveData<List<GroupAndAllTasks>>
}