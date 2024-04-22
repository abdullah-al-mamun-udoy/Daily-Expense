package com.example.expensenote.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.repository.DatabaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class ExpenseItemViewModel @Inject constructor(private val dbRepo: DatabaseRepo) : ViewModel() {


    private val _isModalSheetVisible = MutableLiveData(false)
    val isModalSheetVisible: LiveData<Boolean> = _isModalSheetVisible


    var selectedExpenseItem: ExpenseItemEntity? by mutableStateOf(null)

    fun showModalSheet() {
        _isModalSheetVisible.value = true
    }

    fun addExpenseItem(expenseItemEntity: ExpenseItemEntity) {
        dbRepo.AddExpenseItem(expenseItemEntity)
    }

    fun deleteExpenseItem(expenseItemEntity: ExpenseItemEntity) {
        dbRepo.DeleteExpenseItem(expenseItemEntity)
    }

    fun updateExpenseItem(expenseItemEntity: ExpenseItemEntity) {
        dbRepo.UpdateExpenseItem(expenseItemEntity)
    }

    fun readAllExpenseList(): Flow<List<ExpenseItemEntity>>? {
        return dbRepo.ReadAllExpenseList()
    }
}