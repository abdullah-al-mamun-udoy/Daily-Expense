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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ExpenseItemViewModel @Inject constructor(private val dbRepo: DatabaseRepo) : ViewModel() {

    private val _isLottieVisible = MutableStateFlow(false)
    var isLottieVisible = _isLottieVisible.asStateFlow()

    fun showLottie() {
        _isLottieVisible.value = true
    }

    fun hideLottie() {
        _isLottieVisible.value = false
    }

    fun updateLottieVisibility(expenseList: List<ExpenseItemEntity>) {
        _isLottieVisible.value = expenseList.isEmpty()
    }

    private val _isModalSheetVisible = MutableStateFlow(false)
    var isModalSheetVisible = _isModalSheetVisible.asStateFlow()


    var selectedExpenseItem: ExpenseItemEntity? by mutableStateOf(null)

    fun showModalSheet() {
        _isModalSheetVisible.value = true
    }

    fun hideModalSheet() {
        _isModalSheetVisible.value = false
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