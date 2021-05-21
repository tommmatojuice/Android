package com.example.planer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask

@Dao
interface GroupTaskDao
{
    @Insert
    fun insert(group: GroupTask)

    @Update
    fun update(group: GroupTask)

    @Delete
    fun delete(group: GroupTask)

    @Query("DELETE FROM group_task_table")
    fun deleteAllGroups()

    @Query("SELECT * FROM group_task_table")
    fun allGroups(): LiveData<List<GroupTask>>

    @Query("SELECT * FROM group_task_table ORDER BY group_task_id DESC LIMIT 1")
    fun getLastGroup(): LiveData<GroupTask>

    @Transaction
    @Query("SELECT DISTINCT group_task_id, group_task_table.title FROM group_task_table, task_table WHERE group_task_id = `group` AND task_table.category = :category AND type = :type ORDER BY group_task_table.title")
    fun tasksWithGroup(category: String, type: String): LiveData<List<GroupAndAllTasks>>
}