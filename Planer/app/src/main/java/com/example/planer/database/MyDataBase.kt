package com.example.planer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.planer.database.dao.*
import com.example.planer.database.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Task::class, GroupTask::class, PathToFile::class, Product::class, ProductList::class], version = 3)
abstract class MyDataBase() : RoomDatabase()
{
    abstract fun taskDao(): TaskDao
    abstract fun groupTaskDao(): GroupTaskDao
    abstract fun pathToFileDao(): PathToFileDao
    abstract fun productDao(): ProductDao
    abstract fun productListDao(): ProductListDao

    companion object
    {
        @Volatile
        private var INSTANCE: MyDataBase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope?
        ): MyDataBase? {
            return INSTANCE ?: synchronized(this){
                val instance = scope?.let { MyDatabaseCallBack(it) }?.let {
                    Room.databaseBuilder(
                            context.applicationContext,
                            MyDataBase::class.java,
                            "planer_database"
                    ).fallbackToDestructiveMigration().addCallback(it).build()
                }

                INSTANCE = instance
                instance
            }
        }

        fun populateDatabase(taskDao: TaskDao, groupTaskDao: GroupTaskDao, pathToFileDao: PathToFileDao, productDao: ProductDao, productListDao: ProductListDao){
//            groupTaskDao.insert(GroupTask("groupTitle1"))
//            groupTaskDao.insert(GroupTask("groupTitle2"))
//            taskDao.insert(Task("one_time", "taskTitle1", "Description1", "work", "2020-03-21",
//                30, 1,true, true, false, true, false, false,
//                true, false, null, null, null, 1))
//            taskDao.insert(Task("one_time", "taskTitle2", "Description1", "rest", "2020-03-21",
//                30, 1,true, true, false, true, false, false,
//                true, true, null, null, null, 2))
//            taskDao.insert(Task("one_time", "taskTitle3", "Description1", "other", "2020-03-21",
//                30, 1,true, true, false, true, false, false,
//                true, false, null, null, null, 1))
//
//            taskDao.insert(Task("fixed", "taskTitle4", "Description1", "work", null,
//                30, 1,true, true, false, true, false, false,
//                true, false, "2020-03-21", null, null, 1))
//            taskDao.insert(Task("fixed", "taskTitle5", "Description1", "rest", null,
//                30, 1,true, true, false, true, false, false,
//                true, true, "2020-03-21", null, null, 2))
//            taskDao.insert(Task("fixed", "taskTitle6", "Description1", "other", null,
//                30, 1,true, true, false, true, false, false,
//                true, false, "2020-03-21", null, null, 1))
//
//            taskDao.insert(Task("routine", "taskTitle7", "Description1", "work", null,
//                30, 1,true, true, false, true, false, false,
//                true, false, null, "10:00", "12:00", 1))
//            taskDao.insert(Task("routine", "taskTitle8", "Description1", "rest", null,
//                30, 1,true, true, false, true, false, false,
//                true, true, null, "10:00", "12:00", 2))
//            taskDao.insert(Task("routine", "taskTitle9", "Description1", "other", null,
//                30, 1,true, true, false, true, false, false,
//                true, false, null, "10:00", "12:00", 1))
//            pathToFileDao.insert(PathToFile("path1", 1))
        }

        private class MyDatabaseCallBack(
            private val scope: CoroutineScope
        ):RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO){
                        populateDatabase(database.taskDao(), database.groupTaskDao(), database.pathToFileDao(), database.productDao(), database.productListDao())
                    }
                }
            }
        }
    }
}