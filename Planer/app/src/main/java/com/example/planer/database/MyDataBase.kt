package com.example.planer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.planer.database.dao.GroupTaskDao
import com.example.planer.database.dao.PathToFileDao
import com.example.planer.database.dao.TaskDao
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.PathToFile
import com.example.planer.database.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Task::class, GroupTask::class, PathToFile::class], version = 1)
abstract class MyDataBase() : RoomDatabase()
{
    abstract fun taskDao(): TaskDao
    abstract fun groupTaskDao(): GroupTaskDao
    abstract fun pathToFileDao(): PathToFileDao

    companion object
    {
        @Volatile
        private var INSTANCE: MyDataBase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ):MyDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDataBase::class.java,
                    "planer_database"
                ).fallbackToDestructiveMigration()
                        .addCallback(MyDatabaseCallBack(scope))
                        .build()

                INSTANCE = instance
                instance
            }
        }

        fun populateDatabase(taskDao: TaskDao, groupTaskDao: GroupTaskDao, pathToFileDao: PathToFileDao){
            groupTaskDao.insert(GroupTask("groupTitle1"))
            taskDao.insert(Task("one_time", "taskTitle1", "Description1", "work", "2020-03-21",
                30, 1,true, true, false, true, false, false,
                true, false, null, null, null, 1))
            pathToFileDao.insert(PathToFile("path1", 1))
        }

        private class MyDatabaseCallBack(
            private val scope: CoroutineScope
        ):RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO){
                        populateDatabase(database.taskDao(), database.groupTaskDao(), database.pathToFileDao())
                    }
                }
            }
        }
    }
}