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

    @Query("SELECT * FROM task_table WHERE task_id = :id")
    fun taskById(id: Int): LiveData<Task>

    @Query("SELECT * FROM task_table ORDER BY task_id DESC LIMIT 1")
    fun getLastTask(): LiveData<Task>

    @Query("SELECT task_id, task_table.title, deadline, group_task_table.title as groupTitle, priority, date, `begin`, `end` FROM task_table, group_task_table WHERE `group` = group_task_id AND category = :category AND type = :type AND task_table.title != \"\" UNION SELECT task_id, task_table.title, deadline, null as groupTitle, priority, date, `begin`, `end` FROM task_table WHERE `group` is null AND category = :category AND type = :type AND task_table.title != \"\" ORDER BY deadline, priority DESC")
    fun taskAndGroup(category: String, type: String): LiveData<List<TaskAndGroup>>

    @Query("SELECT task_id, task_table.title, deadline, '' as groupTitle, priority, date, `begin`, `end` FROM task_table WHERE `group` = :idGroup AND task_table.title != ''  ORDER BY deadline, priority DESC")
    fun taskByGroup(idGroup: Int): LiveData<List<TaskAndGroup>>

    @Query("SELECT * FROM task_table WHERE category = :category AND type = :type AND task_table.title != '' ORDER BY complexity, deadline DESC")
    fun getByCategoryAndType(category: String, type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE date = :d AND type='fixed' ORDER BY `begin` DESC")
    fun fixedTasksByDate(d: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type= :type AND monday = 1 ORDER BY complexity DESC, deadline, `begin` DESC")
    fun tasksMon(type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type=:type AND tuesday = 1 ORDER BY complexity DESC, deadline, `begin` DESC")
    fun tasksTue(type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type=:type AND wednesday = 1 ORDER BY complexity DESC, deadline, `begin` DESC")
    fun tasksWen(type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type=:type AND thursday = 1 ORDER BY complexity DESC, deadline, `begin` DESC")
    fun tasksThu(type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type=:type AND friday = 1 ORDER BY complexity DESC, deadline, `begin` DESC")
    fun tasksFri(type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type=:type AND saturday = 1 ORDER BY complexity DESC, deadline, `begin` DESC")
    fun tasksSat(type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type=:type AND sunday = 1 ORDER BY complexity DESC, deadline, `begin` DESC")
    fun tasksSun(type: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE ((:begin>=`begin` AND :begin<`end`) OR (:end>`begin` AND :end<=`end`)) AND type = 'fixed' AND date = :date")
    fun checkTimeFixed(begin: String, end: String, date: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE ((:begin>=`begin` AND :begin<`end`) OR (:end>`begin` AND :end<=`end`)) AND type = 'routine' AND (monday = :monday OR tuesday = :tuesday OR wednesday = :wednesday OR thursday = :thursday OR friday = :friday OR saturday = :saturday OR sunday = :sunday)")
    fun checkTimeRoutine(begin: String, end: String, monday: Int, tuesday: Int, wednesday: Int, thursday: Int, friday: Int, saturday: Int, sunday: Int): LiveData<List<Task>>
}