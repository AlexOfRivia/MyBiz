package com.example.mybiz
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO
{
    @Query("SELECT * FROM Expenses")
    fun getAllExpenses(): Flow<List<Expense>>   //returns all expenses in db

    @Insert
    suspend fun insert(expense: Expense)        //inserts new expense

    @Delete
    suspend fun delete(expense: Expense)        //deletes existing expense
}