package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("accessToken")
	val accessToken: String
)

data class RefreshTokenRequest(
	@SerializedName("refresh_token")
	val refreshToken: String
)
