package com.practice.fixkan.data.remote.repository

import android.graphics.Bitmap
import android.provider.ContactsContract.RawContacts.Data
import android.util.Log
import com.practice.fixkan.data.UiState
import com.practice.fixkan.data.remote.retrofit.ApiService
import com.practice.fixkan.model.response.CreateReportResponse
import com.practice.fixkan.model.response.DataItem
import com.practice.fixkan.utils.bitmaptoMultipart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Query
import java.io.File
import kotlin.concurrent.Volatile

class MainRepository (private val apiService: ApiService) {

    suspend fun createReport(
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
    ): CreateReportResponse {
        val imagePart = bitmaptoMultipart(imageReport, "image.jpg")
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val typeReportPart = typeReport.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val provincePart = province.toRequestBody("text/plain".toMediaTypeOrNull())
        val districtPart = district.toRequestBody("text/plain".toMediaTypeOrNull())
        val subdistrictPart = subdistrict.toRequestBody("text/plain".toMediaTypeOrNull())
        val villagePart = village.toRequestBody("text/plain".toMediaTypeOrNull())
        val addressDetailPart = addressDetail.toRequestBody("text/plain".toMediaTypeOrNull())
        val longitudePart = longitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val latitudePart = latitude.toRequestBody("text/plain".toMediaTypeOrNull())

        return apiService.createReport(
            imagePart,
            userIdPart,
            typeReportPart,
            descriptionPart,
            provincePart,
            districtPart,
            subdistrictPart,
            villagePart,
            addressDetailPart,
            longitudePart,
            latitudePart
        )
    }

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