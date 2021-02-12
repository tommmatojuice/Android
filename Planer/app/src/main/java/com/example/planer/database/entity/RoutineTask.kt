package com.example.planer.database.entity

import java.sql.Time
import java.util.*

class RoutineTask (
    var task_id: Int,
    var title: String,
    var description: String?,
    var category: String,
    var monday: Boolean?,
    var tuesday: Boolean?,
    var wednesday: Boolean?,
    var thursday: Boolean?,
    var friday: Boolean?,
    var saturday: Boolean?,
    var sunday: Boolean?,
    var begin: String?,
    var end: String?,
    var group: Int?
) {
}