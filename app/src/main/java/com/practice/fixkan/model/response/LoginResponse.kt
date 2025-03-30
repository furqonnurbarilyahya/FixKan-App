package com.practice.fixkan.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Location(

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

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class User(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("role")
	val role: Role,

	@field:SerializedName("locationId")
	val locationId: String,

	@field:SerializedName("roleId")
	val roleId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("location")
	val location: Location,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String,

	@field:SerializedName("updatedAt")
	val updatedAt: UpdatedAt
)

data class Role(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class Response(

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)

data class UpdatedAt(

	@field:SerializedName("val")
	val vali: String
)
