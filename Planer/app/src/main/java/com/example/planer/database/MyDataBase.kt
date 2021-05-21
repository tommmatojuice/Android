package com.example.planer.database

import android.content.Context
import android.util.Log
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
                scope: CoroutineScope
        ):MyDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDataBase::class.java,
                        "planer_database"
                ).fallbackToDestructiveMigration().addCallback(MyDatabaseCallBack(scope)).build()
                INSTANCE = instance
                instance
            }
        }

        fun getSimpleDatabase(
                context: Context
        ):MyDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDataBase::class.java,
                        "planer_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

        fun populateDatabase(taskDao: TaskDao, groupTaskDao: GroupTaskDao, pathToFileDao: PathToFileDao, productDao: ProductDao, productListDao: ProductListDao){
            Log.d("populateDatabase", "populateDatabase")
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