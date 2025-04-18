package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class DistrictResponse(

	@field:SerializedName("DistrictResponse")
	val districtResponse: List<DistrictResponseItem>
)

data class DistrictResponseItem(

	@field:SerializedName("regency_id")
	val regencyId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
