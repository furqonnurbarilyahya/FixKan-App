package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class RegenciesResponse(

	@field:SerializedName("RegenciesResponse")
	val regenciesResponse: List<RegenciesResponseItem>
)

data class RegenciesResponseItem(

	@field:SerializedName("province_id")
	val provinceId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
