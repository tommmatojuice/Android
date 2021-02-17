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

    @Transaction
    @Query("SELECT * FROM group_task_table")
    fun tasksWithGroup(): LiveData<List<GroupAndAllTasks>>
}

/* @Insert()
    fun insert(task: Task)

    @Update()
    fun update(task: Task)

    @Delete()
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAllTasks()

    @Query("SELECT * FROM task_table")
    fun allTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE category = :category AND type = :type ORDER BY deadline DESC")
    fun getByCategoryAndType(category: String, type: String): LiveData<List<Task>>

    @Query("SELECT task_id, title, description, category, date, `begin`, `end` FROM task_table WHERE type = \"fixed\" AND category = :category ORDER BY deadline DESC")
    fun getFixedTasks(category: String): LiveData<List<FixedTask>>

    @Query("SELECT task_id, title, description, category, monday, tuesday, wednesday, thursday, friday, saturday, sunday, `begin`, `end` FROM task_table WHERE type = \"routine\" AND category = :category ORDER BY deadline DESC")
    fun getRoutineTasks(category: String): LiveData<List<RoutineTask>>*/