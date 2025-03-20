package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class ProvinceResponse(

	@field:SerializedName("ProvinceResponse")
	val provinceResponse: List<ProvinceResponseItem>
)

data class ProvinceResponseItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
