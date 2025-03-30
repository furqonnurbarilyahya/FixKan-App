package com.practice.fixkan.data.remote.retrofit

import com.practice.fixkan.model.response.CreateReportResponse
import com.practice.fixkan.model.response.DistrictResponseItem
import com.practice.fixkan.model.response.HistoryReportResponse
import com.practice.fixkan.model.response.ListReportResponse
import com.practice.fixkan.model.response.LoginResponse
import com.practice.fixkan.model.response.ProvinceResponseItem
import com.practice.fixkan.model.response.RefreshTokenResponse
import com.practice.fixkan.model.response.RegenciesResponseItem
import com.practice.fixkan.model.response.RegisterResponse
import com.practice.fixkan.model.response.StatisticResponse
import com.practice.fixkan.model.response.VillageResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("reports")
    suspend fun getListReport(
        @Query("type_report") typeReport: String? = null,
        @Query("province") province: String? = null,
        @Query("district") district: String? = null,
        @Query("subdistrict") subdistrict: String? = null,
        @Query("village") village: String? = null,
        @Query("sortBy") sortBy: String? = "createdAt",
        @Query("order") orderBy: String? = "DESC",

        ): ListReportResponse

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
        @Part("userId") userId: RequestBody,
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

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("subdistrict") subdistrict: String,
        @Field("village") village: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/refresh")
    fun refreshToken(@Field("refreshToken") refreshToken: String): Call<RefreshTokenResponse>


    @GET("statistics/reports")
    suspend fun getStatisticData(
        @Query("province") province: String? = null,
        @Query("district") district: String? = null,
        @Query("subdistrict") subdistrict: String? = null,
        @Query("village") village: String? = null
    ): StatisticResponse

    @GET("reports/user/{userId}")
    suspend fun getHistoryReports(
        @Path("userId") userId: String
    ): HistoryReportResponse
}