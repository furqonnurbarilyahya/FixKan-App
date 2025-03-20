package com.practice.fixkan.data.remote.retrofit

import com.practice.fixkan.model.response.DataItem
import com.practice.fixkan.model.response.ListReportResponse
import com.practice.fixkan.model.response.ReportResponse
import retrofit2.http.GET

interface ApiService {
    @GET("reports?sortBy=updatedAt&order=DESC")
    suspend fun getListReport(): ListReportResponse

}