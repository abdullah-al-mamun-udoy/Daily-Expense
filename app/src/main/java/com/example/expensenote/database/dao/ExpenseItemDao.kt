package com.example.expensenote.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expensenote.database.entities.ExpenseItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseItemDao {
    @Query(value = "SELECT COUNT(id) FROM ExpenseItem")
    fun GetExpenseAllItemSize(): Int

//    @Query(value = "DELETE FROM ExpenseItem WHERE id = :itemId ")
//    fun DeleteExpenseItemById(itemId: Int)

    @Query(value = "SELECT * FROM ExpenseItem ")
    fun ReadAllExpenseItem():  Flow<List<ExpenseItemEntity>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun AddExpenseItem(expenseItemEntity: ExpenseItemEntity)

//    @Delete
//    fun DeleteAllItem(expenseItemEntity: ExpenseItemEntity)

    @Delete
    fun DeleteExpenseItem(expenseItemEntity: ExpenseItemEntity)

    @Query("DELETE FROM ExpenseItem")
    suspend fun DeleteAllExpenseItems()


    @Update
    fun UpdateExpenseItem(expenseItemEntity: ExpenseItemEntity)


}