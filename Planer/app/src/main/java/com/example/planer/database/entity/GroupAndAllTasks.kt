package com.example.planer.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class GroupAndAllTasks(
        @Embedded val group: GroupTask,
        @Relation(
                parentColumn = "group_task_id",
                entityColumn = "group"
        )
        val tasks: List<Task>
)