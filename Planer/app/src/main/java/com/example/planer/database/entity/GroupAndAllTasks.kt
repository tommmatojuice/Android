package com.example.planer.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class GroupAndAllTasks
{
    @Embedded
    var group: GroupTask? = null

    @Relation(parentColumn = "group_task_id",
        entityColumn = "group")
    var tasks: List<Task> = ArrayList()
}