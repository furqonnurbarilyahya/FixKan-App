package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class HistoryReportResponse(

	@field:SerializedName("data")
	val data: DataHistory,

	@field:SerializedName("status")
	val status: String
)

data class ReportsItem(

	@field:SerializedName("address_detail")
	val addressDetail: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("type_report")
	val typeReport: String,

	@field:SerializedName("locationId")
	val locationId: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: LocationHistory,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String,

	@field:SerializedName("longitude")
	val longitude: String
)

data class DataHistory(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("reports")
	val reports: List<ReportsItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class LocationHistory(

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("subdistrict")
	val subdistrict: String,

	@field:SerializedName("district")
	val district: String,

	@field:SerializedName("village")
	val village: String
)
