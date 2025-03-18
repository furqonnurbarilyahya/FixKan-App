package com.practice.fixkan.data.remote.retrofit

import com.practice.fixkan.model.response.CreateReportResponse
import com.practice.fixkan.model.response.DataItem
import com.practice.fixkan.model.response.DistrictResponseItem
import com.practice.fixkan.model.response.ProvinceResponseItem
import com.practice.fixkan.model.response.RegenciesResponseItem
import com.practice.fixkan.model.response.ReportResponse
import com.practice.fixkan.model.response.VillageResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("reports?sortBy=updatedAt&order=DESC")
    suspend fun getListReport(): ReportResponse

    @GET("provinces.json")
    suspend fun getProvince(): List<ProvinceResponseItem>

    @GET("regencies/{province_id}.json")
    suspend fun getRegencies(@Path("province_id") provinceId: String): List<RegenciesResponseItem>

    @GET("districts/{regency_id}.json")
    suspend fun getDistricts(@Path("regency_id") regencyId: String): List<DistrictResponseItem>

    @GET("villages/{district_id}.json")
    suspend fun getVillages(@Path("district_id") districtId: String): List<VillageResponseItem>

    @Multipart
    @POST("reports")
    suspend fun createReport(
        @Part image: MultipartBody.Part,
        @Part ("userId") userId: RequestBody,
        @Part("type_report") typeReport: RequestBody,
        @Part("description") description: RequestBody,
        @Part("province") province: RequestBody,
        @Part("district") district: RequestBody,
        @Part("subdistrict") subdistrict: RequestBody,
        @Part("village") village: RequestBody,
        @Part("address_detail") addressDetail: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("latitude") latitude: RequestBody,
    ): CreateReportResponse
}