package com.practice.fixkan.screen.createReport

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.fixkan.data.Result
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.data.remote.retrofit.ApiConfig
import com.practice.fixkan.model.ReportData
import com.practice.fixkan.model.response.CreateReportResponse
import com.practice.fixkan.model.response.DistrictResponseItem
import com.practice.fixkan.model.response.ProvinceResponseItem
import com.practice.fixkan.model.response.RegenciesResponseItem
import com.practice.fixkan.model.response.VillageResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel(private val repository: MainRepository) : ViewModel() {
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

    private val _uploadState = MutableLiveData<Result<CreateReportResponse>>()
    val uploadState: LiveData<Result<CreateReportResponse>> = _uploadState

    fun setReportData(
        typeReport: String,
        photoUri: String,
        lat: Double? = 0.0,
        long: Double? = 0.0,
        admArea: String? = null,
        subAdmArea: String? = null,
        local: String? = null,
        subLocal: String? = null
    ) {
        Log.d(
            "ReportViewModel",
            "setReportData called with: lat=$lat, long=$long, admArea=$admArea"
        )
        viewModelScope.launch(Dispatchers.Main) {
            _reportData.value = ReportData(
                typeReport = typeReport,
                photoUri = photoUri,
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
                _provinces.value = ApiConfig.locationApiService().getProvince()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchRegencies(provinceId: String) {
        viewModelScope.launch {
            try {
                _regencies.value = ApiConfig.locationApiService().getRegencies(provinceId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchDistricts(regencyId: String) {
        viewModelScope.launch {
            try {
                _districts.value = ApiConfig.locationApiService().getDistricts(regencyId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchVillages(districtId: String) {
        viewModelScope.launch {
            try {
                _villages.value = ApiConfig.locationApiService().getVillages(districtId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // Post Report
    fun uploadReport(
        imageReport: Bitmap,
        userId: String,
        typeReport: String,
        description: String,
        province: String,
        district: String,
        subdistrict: String,
        village: String,
        addressDetail: String,
        longitude: String,
        latitude: String,
    ) {
        viewModelScope.launch {
            try {
                val response = repository.createReport(
                    imageReport = imageReport,
                    userId = userId,
                    typeReport = typeReport,
                    description = description,
                    province = province,
                    district = district,
                    subdistrict = subdistrict,
                    village = village,
                    addressDetail = addressDetail,
                    longitude = longitude,
                    latitude = latitude
                )
                _uploadState.postValue(Result.Success(response))
            } catch (e: Exception) {
                _uploadState.postValue(Result.Error(e.toString()))
            }

        }
    }
}