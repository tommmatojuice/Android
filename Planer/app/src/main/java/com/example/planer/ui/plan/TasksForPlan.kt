package com.example.planer.ui.plan

import com.example.planer.database.entity.Task
import java.time.LocalTime

class TasksForPlan(
        var begin: LocalTime,
        var end: LocalTime,
        var time: Int?,
        var task: Task?
) {

}