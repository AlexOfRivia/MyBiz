package com.example.mybiz
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDAO
{
    @Query("SELECT * FROM Incomes")
    fun getAllIncomes(): Flow<List<Income>> //returns all incomes in the db

    @Insert
    suspend fun insert(income: Income)      //inserts new income this btw, suspend makes sure, that this function
                                            // can be executed in the background without freezing the app

    @Delete
    suspend fun delete(income: Income)      //deletes existing income
}