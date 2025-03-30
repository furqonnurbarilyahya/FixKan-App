package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class ReportResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("status")
	val status: String
)

data class DataItemx(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("type_report")
	val typeReport: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("region")
	val region: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("longitude")
	val longitude: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
