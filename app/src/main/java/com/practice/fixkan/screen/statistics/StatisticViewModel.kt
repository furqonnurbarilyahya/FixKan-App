package com.practice.fixkan.screen.statistics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.model.response.StatisticResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class StatisticViewModel(private val repository: MainRepository) : ViewModel() {
    private val _statistics = MutableStateFlow<StatisticResponse?>(null)
    val statistics: StateFlow<StatisticResponse?> = _statistics

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getStatistics(province: String? = null,
                      district: String? = null,
                      subdistrict: String? = null,
                      village: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.fetchStatistics(province, district, subdistrict, village)
            Log.d("StatisticViewModel", "API Response: $result")
            result.onSuccess { data ->
                _statistics.value = data
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.localizedMessage
            }
            _isLoading.value = false
        }
    }
}
