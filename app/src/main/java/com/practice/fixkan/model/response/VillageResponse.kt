package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class VillageResponse(

	@field:SerializedName("VillageResponse")
	val villageResponse: List<VillageResponseItem>
)

data class VillageResponseItem(

	@field:SerializedName("regency_id")
	val regencyId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
