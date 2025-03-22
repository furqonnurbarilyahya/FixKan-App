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

class MainRepository(private val apiService: ApiService) {

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

    suspend fun getListReport(
        typeReport: String? = null,
        province: String? = null,
        district: String? = null,
        subdistrict: String? = null,
        village: String? = null,
        sortBy: String? = "createdAt",
        orderBy: String? = "ASC"
    ): Result<List<DataItem>> {
        return try {
            val response = apiService.getListReport(
                typeReport,
                province,
                district,
                subdistrict,
                village,
                sortBy,
                orderBy
            )
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(apiService: ApiService): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService)
            }.also { instance == it }
    }
}