package com.example.expensenote.presentation.screen.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class SettingViewmodel @Inject constructor() : ViewModel() {

    val _isModalVisible = MutableStateFlow(false)
    val isModalVisible = _isModalVisible

    fun showModalSheet() {
        _isModalVisible.value = true
    }
    fun hideModalSheet() {
        _isModalVisible.value = false
    }
}