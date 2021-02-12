package com.example.planer.database.entity

import java.sql.Time
import java.util.*

class FixedTask (
    var task_id: Int,
    var title: String,
    var description: String?,
    var category: String,
    var date: String?,
    var begin: String?,
    var end: String?,
    var group: Int?
) {
}