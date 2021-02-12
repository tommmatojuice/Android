package com.example.planer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.database.entity.FixedTask
import com.example.planer.database.entity.RoutineTask
import com.example.planer.database.entity.Task
import com.example.planer.database.entity.TaskAndAllPaths

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
//    @Transaction
//    @Query("SELECT * FROM task_table")
//    fun tasksWithPaths(): LiveData<List<TaskAndAllPaths>>

    @Query("SELECT * FROM task_table WHERE category = :category AND type = :type ORDER BY deadline DESC")
    fun getByCategoryAndType(category: String, type: String): LiveData<List<Task>>

    @Query("SELECT task_id, title, description, category, date, `begin`, `end` FROM task_table WHERE type = \"fixed\" AND category = :category ORDER BY deadline DESC")
    fun getFixedTasks(category: String): LiveData<List<FixedTask>>

    @Query("SELECT task_id, title, description, category, monday, tuesday, wednesday, thursday, friday, saturday, sunday, `begin`, `end` FROM task_table WHERE type = \"routine\" AND category = :category ORDER BY deadline DESC")
    fun getRoutineTasks(category: String): LiveData<List<RoutineTask>>
}

/*import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {
    @Insert()
    fun insert(item:Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("DELETE FROM item_table")
    fun deleteAllItems()

    @Query("SELECT * FROM item_table ORDER BY priority DESC")
    fun allItems(): LiveData<List<Item>>

}*/