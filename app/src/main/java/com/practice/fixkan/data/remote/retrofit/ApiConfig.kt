package com.practice.fixkan.data.remote.retrofit

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.data.pref.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    fun authApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://sec-prediction-app-backend.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    fun reportApiService(context: Context): ApiService {

        val userPreference = UserPreference.getInstance(context.dataStore)

        lateinit var apiService: ApiService

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context,userPreference) { apiService })
            .build()

        val retrofit = Retrofit.Builder()
//            .baseUrl("https://sec-prediction-app-backend.vercel.app/")
            .baseUrl("https://fixkan-api.zainal-saputra.click/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiService = retrofit.create(ApiService::class.java) // Inisialisasi setelah Retrofit dibuat

        return apiService
    }


    fun locationApiService(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.emsifa.com/api-wilayah-indonesia/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}


class AuthInterceptor(
    private val context: Context,
    private val userPreference: UserPreference,
    private val apiServiceProvider: () -> ApiService
) : Interceptor {

    @Volatile
    private var isRefreshing = false // Flag untuk mencegah infinite loop

    override fun intercept(chain: Interceptor.Chain): Response {
        var accessToken = runBlocking {
            userPreference.getUserData().firstOrNull()?.accessToken.orEmpty()
        }
        Log.d("AuthInterceptor", "Access Token (Before Request): $accessToken")

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(request)
        Log.d("AuthInterceptor", "Response Code: ${response.code}")

        if (response.code == 401 || response.code == 403) { // Jika token expired atau unauthorized
            response.close()

            if (isRefreshing) {
                Log.e("AuthInterceptor", "Already refreshing token. Preventing infinite loop.")
                return response // Cegah multiple refresh token dalam satu request
            }

            isRefreshing = true // Set flag saat mulai refresh token

            val refreshToken = runBlocking {
                userPreference.getUserData().firstOrNull()?.refreshToken.orEmpty()
            }
            Log.d("AuthInterceptor", "Refresh Token: $refreshToken")

            if (refreshToken.isNotEmpty()) {
                val newToken = runBlocking {
                    try {
                        val refreshResponse = apiServiceProvider()
                            .refreshToken(refreshToken)
                            .execute()

                        if (refreshResponse.isSuccessful) {
                            refreshResponse.body()?.accessToken
                        } else {
                            Log.e("AuthInterceptor", "Refresh Token Failed: ${refreshResponse.errorBody()?.string()}")
                            null
                        }
                    } catch (e: Exception) {
                        Log.e("AuthInterceptor", "Error refreshing token", e)
                        null
                    }
                }

                if (!newToken.isNullOrEmpty()) {
                    runBlocking {
                        val userData = userPreference.getUserData().firstOrNull()
                        if (userData?.user != null) {
                            userPreference.saveUserData(userData.user, newToken, refreshToken)
                        }
                    }

                    accessToken = runBlocking {
                        userPreference.getUserData().firstOrNull()?.accessToken.orEmpty()
                    }
                    Log.d("AuthInterceptor", "Access Token (After Refresh): $accessToken")

                    val newRequest = chain.request().newBuilder()
                        .header("Authorization", "Bearer $accessToken")
                        .build()

                    isRefreshing = false // Reset flag setelah refresh selesai
                    return chain.proceed(newRequest)
                } else {
                    Log.e("AuthInterceptor", "Refresh token failed, forcing user to logout.")
//                    runBlocking { userPreference.logout() } // Logout user
                    // Tampilkan Toast di UI Thread
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Sesi habis. Mohon untuk login ulang.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        isRefreshing = false // Reset flag setelah request selesai
        return response
    }
}



