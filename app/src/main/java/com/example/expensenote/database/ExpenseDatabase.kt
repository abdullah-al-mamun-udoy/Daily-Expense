package com.example.expensenote.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expensenote.database.dao.ExpenseItemDao
import com.example.expensenote.database.entities.ExpenseItemEntity


@Database(entities = [ExpenseItemEntity::class], version = 1)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun ExpenseItemDao(): ExpenseItemDao
}