package com.example.planer.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Time
import java.util.*

@Entity(tableName = "task_table",
        foreignKeys = arrayOf(
                ForeignKey(entity = GroupTask::class,
                        parentColumns = arrayOf("group_task_id"),
                        childColumns = arrayOf("group"),
                        onUpdate = ForeignKey.CASCADE
                )
        )
)
class Task (
    var type: String,
    var title: String,
    var description: String?,
    var category: String,
    var deadline: String?,
    var duration: Int?,
    var complexity: Int?,
    var monday: Boolean?,
    var tuesday: Boolean?,
    var wednesday: Boolean?,
    var thursday: Boolean?,
    var friday: Boolean?,
    var saturday: Boolean?,
    var sunday: Boolean?,
    var priority: Boolean?,
    var date: String?,
    var begin: String?,
    var end: String?,
    var group: Int?
):Serializable
{
    @PrimaryKey(autoGenerate = true)
    var task_id = 0
}