package com.practice.fixkan.data.remote.retrofit

import com.practice.fixkan.model.response.DataItem
import com.practice.fixkan.model.response.DistrictResponseItem
import com.practice.fixkan.model.response.ProvinceResponseItem
import com.practice.fixkan.model.response.RegenciesResponseItem
import com.practice.fixkan.model.response.ReportResponse
import com.practice.fixkan.model.response.VillageResponseItem
import retrofit2.http.GET
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
}