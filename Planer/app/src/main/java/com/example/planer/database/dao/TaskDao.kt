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

    @Query("SELECT task_id, task_table.title, deadline, \"\" as groupTitle, priority, date, `begin`, `end` FROM task_table WHERE `group` = :idGroup AND task_table.title != \"\"  ORDER BY deadline, priority DESC")
    fun taskByGroup(idGroup: Int): LiveData<List<TaskAndGroup>>

    @Query("SELECT * FROM task_table WHERE category = :category AND type = :type AND task_table.title != \"\" ORDER BY complexity, deadline DESC")
    fun getByCategoryAndType(category: String, type: String): LiveData<List<Task>>

    @Query("SELECT task_id, title, description, category, date, `begin`, `end` FROM task_table WHERE type = \"fixed\" AND category = :category ORDER BY deadline DESC")
    fun getFixedTasks(category: String): LiveData<List<FixedTask>>

    @Query("SELECT task_id, title, description, category, monday, tuesday, wednesday, thursday, friday, saturday, sunday, `begin`, `end` FROM task_table WHERE type = \"routine\" AND category = :category ORDER BY deadline DESC")
    fun getRoutineTasks(category: String): LiveData<List<RoutineTask>>

    @Query("SELECT * FROM task_table WHERE date = :d AND type='fixed' ORDER BY `begin` DESC")
    fun fixedTasksByDate(d: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='routine' AND monday = 1 ORDER BY `begin` DESC")
    fun routineMon(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='routine' AND tuesday = 1 ORDER BY `begin` DESC")
    fun routineTue(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='routine' AND wednesday = 1 ORDER BY `begin` DESC")
    fun routineWen(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='routine' AND thursday = 1 ORDER BY `begin` DESC")
    fun routineThu(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='routine' AND friday = 1 ORDER BY `begin` DESC")
    fun routineFri(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='routine' AND saturday = 1 ORDER BY `begin` DESC")
    fun routineSat(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='routine' AND sunday = 1 ORDER BY `begin` DESC")
    fun routineSun(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND monday = :monday AND tuesday = :tuesday AND wednesday = :wednesday AND thursday = :thursday AND friday = :friday AND saturday = :saturday AND sunday = :sunday ORDER BY complexity DESC")
    fun oneTimeTasks(category: String, monday: Int, tuesday: Int, wednesday: Int, thursday: Int, friday: Int, saturday: Int, sunday: Int): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND monday = 1 ORDER BY complexity DESC, deadline ")
    fun oneTimeMon(category: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND tuesday = 1 ORDER BY complexity DESC,deadline ")
    fun oneTimeTue(category: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND wednesday = 1 ORDER BY complexity DESC,deadline ")
    fun oneTimeWen(category: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND thursday = 1 ORDER BY complexity DESC,deadline ")
    fun oneTimeThu(category: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND friday = 1 ORDER BY complexity DESC,deadline ")
    fun oneTimeFri(category: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND saturday = 1 ORDER BY complexity DESC,deadline ")
    fun oneTimeSat(category: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE type='one_time' AND category = :category AND sunday = 1 ORDER BY complexity DESC,deadline ")
    fun oneTimeSun(category: String): LiveData<List<Task>>
}