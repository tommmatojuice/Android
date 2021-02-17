package com.example.planer.database.entity

import androidx.room.Relation
import androidx.room.Embedded

class TaskAndGroup(
    var task_id: Int,
    var title: String,
    var deadline: String,
    var group: String?
)