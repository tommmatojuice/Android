package com.example.planer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.database.entity.PathToFile

@Dao
interface PathToFileDao
{
    @Insert()
    fun insert(path: PathToFile)

    @Update()
    fun update(path: PathToFile)

    @Delete()
    fun delete(path: PathToFile)

    @Query("DELETE FROM path_to_file_table")
    fun deleteAllTasks()

    @Query("SELECT * FROM path_to_file_table")
    fun allPaths(): LiveData<List<PathToFile>>

    @Query("SELECT * FROM path_to_file_table WHERE task =:id")
    fun pathsById(id: Int): LiveData<List<PathToFile>>
}