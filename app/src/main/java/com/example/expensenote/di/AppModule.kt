package com.example.expensenote.di

import android.content.Context
import androidx.room.Room
import com.example.expensenote.database.ExpenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun ProvideDatabase(@ApplicationContext context: Context): ExpenseDatabase{
        return Room.databaseBuilder(context.applicationContext,ExpenseDatabase::class.java, "Expense_Database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}