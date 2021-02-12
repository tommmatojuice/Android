package com.example.planer.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class TaskAndAllPaths
{
    @Embedded
    var task: Task? = null

    @Relation(parentColumn = "task_id",
              entityColumn = "task")
    var paths: List<PathToFile> = ArrayList()
}


/*
* @Embedded
   var user: User? = null

   @Relation(parentColumn = “userId”,
             entityColumn = “owner”)
   var pets: List<Pet> = ArrayList()*/