package com.practice.fixkan.screen.ListReport

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.fixkan.data.UiState
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.model.response.DataItem
import com.practice.fixkan.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListReportViewModel(private val repository: MainRepository): ViewModel() {
    val listReportState = repository.listReportState

    private val _uiState = MutableStateFlow<UiState<List<DataItem>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<DataItem>>> = _uiState

    init {
        getListReport()
    }

    fun getListReport(){
        viewModelScope.launch {
            repository.getListReport()
//            try {
//                Log.d("API_CALL", "Fetching Data from API......... ")
//
//                val response = repository.getListReport()
//
//                if (response.isNotEmpty()) {
//                    _uiState.value = UiState.Success(response)
//                    Log.d("API_SUCCESS", "Data retrieved: $response")  // ✅ Pastikan ini muncul
//                } else {
//                    _uiState.value = UiState.Error("Data kosong")
//                    Log.e("API_ERROR", "Data kosong")
//                }
//
//                Log.d("API_SUCCES", "Data retrieved: ")
//            } catch (e: Exception) {
//                _uiState.value = UiState.Error(e.message ?: "Unknown Error")
//                Log.e("API ERROR", "Error Fetching Data: ${e.message}")
//            }
        }
    }
}