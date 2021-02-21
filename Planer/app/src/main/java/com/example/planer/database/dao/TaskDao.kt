package com.example.planer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.database.entity.*

@Dao
interface TaskDao {
    @Insert()
    fun insert(task:Task)

    @Update()
    fun update(task: Task)

    @Delete()
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAllTasks()

    @Query("SELECT * FROM task_table")
    fun allTasks(): LiveData<List<Task>>

//    @Query("SELECT task_id, task_table.title, deadline, group_task_table.title FROM task_table, group_task_table WHERE `group` = group_task_id")
//    fun taskAndGroup(): LiveData<List<TaskAndGroup>>

    @Query("SELECT task_id, task_table.title, deadline, group_task_table.title as groupTitle, priority, date, `begin`, `end` FROM task_table, group_task_table WHERE `group` = group_task_id AND category = :category AND type = :type ORDER BY deadline, priority DESC")
    fun taskAndGroup(category: String, type: String): LiveData<List<TaskAndGroup>>

    @Query("SELECT * FROM task_table WHERE category = :category AND type = :type ORDER BY deadline DESC")
    fun getByCategoryAndType(category: String, type: String): LiveData<List<Task>>

    @Query("SELECT task_id, title, description, category, date, `begin`, `end` FROM task_table WHERE type = \"fixed\" AND category = :category ORDER BY deadline DESC")
    fun getFixedTasks(category: String): LiveData<List<FixedTask>>

    @Query("SELECT task_id, title, description, category, monday, tuesday, wednesday, thursday, friday, saturday, sunday, `begin`, `end` FROM task_table WHERE type = \"routine\" AND category = :category ORDER BY deadline DESC")
    fun getRoutineTasks(category: String): LiveData<List<RoutineTask>>

    //    @Transaction
//    @Query("SELECT * FROM task_table")
//    fun tasksWithPaths(): LiveData<List<TaskAndAllPaths>>
}