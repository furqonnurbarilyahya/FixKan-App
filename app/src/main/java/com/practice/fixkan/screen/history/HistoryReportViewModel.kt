package com.practice.fixkan.screen.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.model.response.ReportsItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoryReportViewModel(private val repository: MainRepository) : ViewModel() {

    private val _reports = MutableStateFlow<List<ReportsItem>>(emptyList())
    val reports: StateFlow<List<ReportsItem>> = _reports

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchHistoryReports(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.d("HistoryReportViewModel", "Fetching reports for user: $userId")
            try {
                val response = repository.getHistoryReports(userId)
                Log.d("HistoryReportViewModel", "Response: ${response.data.reports}")
                _reports.value = response.data.reports
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengambil data: ${e.message}"
                Log.e("HistoryReportViewModel", "Error: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}