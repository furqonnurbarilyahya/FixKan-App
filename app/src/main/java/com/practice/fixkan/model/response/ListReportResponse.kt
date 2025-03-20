package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class ListReportResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("type_report")
	val typeReport: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("address_detail")
	val addressDetail: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("province")
	val province: String,

	@field:SerializedName("subdistrict")
	val subdistrict: String,

	@field:SerializedName("district")
	val district: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("village")
	val village: String,

	@field:SerializedName("longitude")
	val longitude: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
