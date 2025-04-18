package com.practice.fixkan.data.remote.repository

import android.graphics.Bitmap
import android.util.Log
import com.practice.fixkan.data.remote.retrofit.ApiService
import com.practice.fixkan.model.response.CreateReportResponse
import com.practice.fixkan.model.response.DataItem
import com.practice.fixkan.model.response.HistoryReportResponse
import com.practice.fixkan.model.response.StatisticResponse
import com.practice.fixkan.utils.bitmaptoMultipart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
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

    suspend fun fetchStatistics(
        province: String?,
        district: String?,
        subdistrict: String?,
        village: String?
    ): Result<StatisticResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStatisticData(province, district, subdistrict, village)
                Result.success(response)
            } catch (e: HttpException) {
                Log.e("StatisticRepository", "HTTP error: ${e.code()} - ${e.message()}")
                Result.failure(e)
            } catch (e: Exception) {
                Log.e("StatisticRepository", "Unknown error: ${e.message}")
                Result.failure(e)
            }
        }
    }

    suspend fun getHistoryReports(userId: String): HistoryReportResponse {
        Log.d("HistoryReportRepository", "Fetching reports for user: $userId")
        val response = apiService.getHistoryReports(userId)
        Log.d("HistoryReportRepository", "Received response: $response")
        return response
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