package com.practice.fixkan.model

data class UserData (
    val token: String,
    val refreshToken: String,
    val userId: String,
    val name: String,
    val email: String,
    val roleId: String
)