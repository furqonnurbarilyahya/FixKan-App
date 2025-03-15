package com.practice.fixkan.screen.CreateReport

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.fixkan.data.remote.retrofit.ApiConfig
import com.practice.fixkan.model.ReportData
import com.practice.fixkan.model.response.DistrictResponseItem
import com.practice.fixkan.model.response.ProvinceResponseItem
import com.practice.fixkan.model.response.RegenciesResponseItem
import com.practice.fixkan.model.response.VillageResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel : ViewModel() {
    private val _reportData = MutableLiveData<ReportData>()
    val reportData: LiveData<ReportData> = _reportData

    private val _provinces = MutableStateFlow<List<ProvinceResponseItem>>(emptyList())
    val provinces: StateFlow<List<ProvinceResponseItem>> = _provinces

    private val _regencies = MutableStateFlow<List<RegenciesResponseItem>>(emptyList())
    val regencies: StateFlow<List<RegenciesResponseItem>> = _regencies

    private val _districts = MutableStateFlow<List<DistrictResponseItem>>(emptyList())
    val districts: StateFlow<List<DistrictResponseItem>> = _districts

    private val _villages = MutableStateFlow<List<VillageResponseItem>>(emptyList())
    val villages: StateFlow<List<VillageResponseItem>> = _villages

    fun setReportData(
        typeReport: String,
        photoUri: Uri?,
        lat: Double,
        long: Double,
        admArea: String,
        subAdmArea: String,
        local: String,
        subLocal: String
    ) {
        Log.d(
            "ReportViewModel",
            "setReportData called with: lat=$lat, long=$long, admArea=$admArea"
        )
        viewModelScope.launch(Dispatchers.Main) {
            _reportData.value = ReportData(
                typeReport = typeReport,
                photoUri = photoUri.toString(),
                lat = lat,
                long = long,
                admArea = admArea,
                subAdmArea = subAdmArea,
                local = local,
                subLocal = subLocal
            )

            Log.d("ReportViewModel", "New reportData: ${_reportData.value}")
        }
        Log.d("ReportViewModel", "New reportData: ${_reportData.value}")
    }


    fun fetchProvinces() {
        viewModelScope.launch {
            try {
                _provinces.value = ApiConfig.LocationApiService().getProvince()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRegencies(provinceId: String) {
        viewModelScope.launch {
            try {
                _regencies.value = ApiConfig.LocationApiService().getRegencies(provinceId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchDistricts(regencyId: String) {
        viewModelScope.launch {
            try {
                _districts.value = ApiConfig.LocationApiService().getDistricts(regencyId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchVillages(districtId: String) {
        viewModelScope.launch {
            try {
                _villages.value = ApiConfig.LocationApiService().getVillages(districtId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}