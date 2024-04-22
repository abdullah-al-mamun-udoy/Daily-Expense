package com.example.expensenote.repository

import com.example.expensenote.database.ExpenseDatabase
import com.example.expensenote.database.entities.ExpenseItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepo @Inject constructor( private val db: ExpenseDatabase ) {

    fun ReadAllExpenseList(): Flow<List<ExpenseItemEntity>>? = db.ExpenseItemDao().ReadAllExpenseItem()

    fun AddExpenseItem(expenseItemEntity: ExpenseItemEntity) = db.ExpenseItemDao().AddExpenseItem(expenseItemEntity)

    fun DeleteExpenseItem(expenseItemEntity: ExpenseItemEntity) = db.ExpenseItemDao().DeleteExpenseItem(expenseItemEntity)

    fun UpdateExpenseItem(expenseItemEntity: ExpenseItemEntity) = db.ExpenseItemDao().UpdateExpenseItem(expenseItemEntity)
}