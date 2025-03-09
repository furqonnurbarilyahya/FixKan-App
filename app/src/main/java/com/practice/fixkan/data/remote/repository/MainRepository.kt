package com.practice.fixkan.data.remote.repository

import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import com.practice.fixkan.data.UiState
import com.practice.fixkan.data.remote.retrofit.ApiService
import com.practice.fixkan.model.response.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.http.Query
import kotlin.concurrent.Volatile

class MainRepository (private val apiService: ApiService) {

    //UI State List Report
//    private val _listReportState: MutableStateFlow<UiState<List<DataItem>>> = MutableStateFlow(UiState.Loading)
//    val listReportState: StateFlow<UiState<List<DataItem>>> get() = _listReportState
//
//    suspend fun getListReport() {
//        try {
//            _listReportState.value = UiState.Success(apiService.getListReport())
//        } catch (e: Exception) {
//            _listReportState.value = UiState.Error(e.message.toString())
//        }
//    }
//
//    private val listReport = mutableListOf<DataItem>()
//
//    fun getReport(): List<DataItem> {
//        return listReport
//    }
//
//    fun searchReport(query: String): List<DataItem> {
//        return listReport.filter {
//            it.typeReport.contains(query, ignoreCase = true)
//        }
//    }

    // UI State untuk List Report
    private val _listReportState = MutableStateFlow<UiState<List<DataItem>>>(UiState.Loading)
    val listReportState: StateFlow<UiState<List<DataItem>>> get() = _listReportState

    suspend fun getListReport() {
        try {
            val response = apiService.getListReport()
            _listReportState.value = UiState.Success(response.data)
            Log.d("API_SUCCESS", "Fetched ${response.data.size} reports")
        } catch (e: Exception) {
            _listReportState.value = UiState.Error(e.message ?: "Unknown error")
            Log.e("API_ERROR", "Error fetching reports: ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance (apiService: ApiService): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService)
            }.also { instance == it }
    }
}