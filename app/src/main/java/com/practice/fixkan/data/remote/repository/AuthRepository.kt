package com.practice.fixkan.data.remote.repository

import android.util.Log
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.data.remote.retrofit.ApiService
import com.practice.fixkan.model.response.LoginResponse
import com.practice.fixkan.model.response.RegisterResponse

class AuthRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {


    suspend fun register(
        name: String, email: String, password: String,
        province: String, district: String,
        subdistrict: String, village: String
    ): Result<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password, province, district, subdistrict, village)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(email, password)

            // Periksa apakah login gagal dari respons API
            if (response.status == "error") {
                Log.e("LoginResponse", "Login failed: ${response.message}")
                return Result.failure(Exception(response.message))
            }

            // Jika login sukses, simpan data user
            userPreference.saveUserData(
                response.response.user,
                response.response.accessToken,
                response.response.refreshToken
            )

            Log.d("LoginResponse", "Success: ${response.message}")
            Result.success(response)

        } catch (e: Exception) {
            Log.e("LoginResponse", "Error: ${e.message}")
            Result.failure(e)
        }
    }


//    fun getSession(): Flow<UserData> {
//        return userPreferences.getSession()
//    }
//
//    suspend fun saveSession(user: UserData) {
//        userPreferences.saveSession(user)
//    }

//    suspend fun logout() {
//        userPreferences.clearToken()
//    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreference)
            }.also { instance = it }
    }
}
