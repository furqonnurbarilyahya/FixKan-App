package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class StatisticResponse(

	@field:SerializedName("data")
	val data: DataStat,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataStat(

	@field:SerializedName("Bangunan Rusak")
	val bangunanRusak: Int,

	@field:SerializedName("Jembatan Rusak")
	val jembatanRusak: Int,

	@field:SerializedName("Sampah Berserakan")
	val sampahBerserakan: Int,

	@field:SerializedName("Bangunan Roboh")
	val bangunanRoboh: Int,

	@field:SerializedName("Jalan Rusak")
	val jalanRusak: Int
)
