package com.example.expensenote.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "ExpenseItem")
data class ExpenseItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var expenseName: String,
    var expenseAmount: String,
    var date:String,
    var expenseDescription: String?="",
)