package com.example.planer.util

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.database.entity.Task
import com.example.planer.database.viewModel.TaskViewModel

class TasksDataBase: AppCompatActivity()
{
    private val taskViewModel: TaskViewModel by viewModels()

    fun updateTask(task: Task){
        taskViewModel.update(task)
    }
}