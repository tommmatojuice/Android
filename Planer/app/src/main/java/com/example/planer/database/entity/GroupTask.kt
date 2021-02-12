package com.example.planer.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "group_task_table")
class GroupTask(
    var title: String
):Serializable {
    @PrimaryKey(autoGenerate = true)
    var group_task_id = 0
}