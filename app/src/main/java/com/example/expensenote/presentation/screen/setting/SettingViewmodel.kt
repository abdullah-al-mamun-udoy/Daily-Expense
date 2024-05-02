package com.example.expensenote.presentation.screen.setting

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.repository.DatabaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SettingViewmodel @Inject constructor(private val dbRepo: DatabaseRepo) : ViewModel() {

    val _isModalVisible = MutableStateFlow(false)
    val isModalVisible = _isModalVisible

    private val _selectedImageUri = MutableStateFlow("")
    val selectedImageUri = _selectedImageUri.asStateFlow()

    fun setSelectedImageUri(uri: String) {
        _selectedImageUri.value = uri
    }

    fun showModalSheet() {
        _isModalVisible.value = true
    }
    fun hideModalSheet() {
        _isModalVisible.value = false
    }

    suspend fun deleteAllExpenseItem() {
      dbRepo.DeleteAllExpenseItems()
    }


}