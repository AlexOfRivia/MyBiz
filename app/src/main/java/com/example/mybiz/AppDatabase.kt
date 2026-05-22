package com.example.mybiz

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Income::class, Expense::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun IncomeDAO(): IncomeDAO
    abstract fun ExpenseDAO(): ExpenseDAO

    companion object {
        @Volatile                                           //ensures thread safety in the db
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context) : AppDatabase     //"asking" for database access
        {
            return INSTANCE ?: synchronized(this) {         //database is returned if it already exists
                val instance = Room.databaseBuilder(        //creating a new db otherwise and synchronizing threads
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mybiz_database"
                ).build()
                INSTANCE = instance
                instance                       //db instance is returned
            }

        }
    }
}