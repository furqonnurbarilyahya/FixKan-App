package com.practice.fixkan.screen.listReport

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.fixkan.data.UiState
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.model.response.DataItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListReportViewModel(private val repository: MainRepository) : ViewModel() {

    private val _listReportState = MutableStateFlow<UiState<List<DataItem>>>(UiState.Loading)
    val listReportState: StateFlow<UiState<List<DataItem>>> get() = _listReportState

    private val _showNoDataDialog = MutableStateFlow(false)
//    val showNoDataDialog: StateFlow<Boolean> get() = _showNoDataDialog

    private val _selectedFilter = MutableStateFlow<Map<String, String?>>(emptyMap())
    val selectedFilter: StateFlow<Map<String, String?>> get() = _selectedFilter

    fun getListReport(
        typeReport: String? = null,
        province: String? = null,
        district: String? = null,
        subdistrict: String? = null,
        village: String? = null,
        sortBy: String? = "createdAt",
        orderBy: String? = "DESC"
    ) {
        viewModelScope.launch {
            _listReportState.value = UiState.Loading
            _showNoDataDialog.value = false
            _selectedFilter.value = mapOf(
                "typeReport" to typeReport,
                "province" to province,
                "district" to district,
                "subdistrict" to subdistrict,
                "village" to village,
                "orderby" to orderBy
            )
            val result = repository.getListReport(typeReport, province, district, subdistrict, village, sortBy, orderBy)
            Log.d("ListReportViewModel", "Response Data: ${result.getOrNull()}")


            _listReportState.value = result.fold(
                onSuccess = { data ->
                    UiState.Success(data)
                },
                onFailure = { UiState.Error(it.message ?: "Terjadi kesalahan") }
            )
        }
    }
}