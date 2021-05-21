package com.example.planer.database.entity

class TaskAndGroup(
    var task_id: Int,
    var title: String,
    var deadline: String?,
    var groupTitle: String?,
    var priority: Boolean,
    var date: String?,
    var begin: String?,
    var end: String?
)