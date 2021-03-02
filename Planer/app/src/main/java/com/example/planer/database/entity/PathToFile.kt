package com.example.planer.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "path_to_file_table",
        foreignKeys = arrayOf(
            ForeignKey(entity = Task::class,
                parentColumns = arrayOf("task_id"),
                childColumns = arrayOf("task"),
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE
            )
        )
)
class PathToFile(
    var path: String,
    var task: Int
):Serializable {
    @PrimaryKey(autoGenerate = true)
    var path_id = 0
}