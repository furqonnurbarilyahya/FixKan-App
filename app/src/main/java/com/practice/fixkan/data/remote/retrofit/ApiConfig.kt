package com.practice.fixkan.data.remote.retrofit

import android.content.Context
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.data.pref.dataStore
import com.practice.fixkan.model.response.RefreshTokenRequest
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
            .addInterceptor(AuthInterceptor(userPreference) { apiService }) // Lazy ApiService
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://sec-prediction-app-backend.vercel.app/")
//            .baseUrl("http://18.141.58.71:3000")
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
    private val userPreference: UserPreference,
    private val apiServiceProvider: () -> ApiService // Lazy initialization
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            userPreference.getUserData().firstOrNull()?.accessToken.orEmpty()
        }

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        var response = chain.proceed(request)

        if (response.code == 401) { // Jika token expired
            val refreshToken = runBlocking {
                userPreference.getUserData().firstOrNull()?.refreshToken.orEmpty()
            }

            if (refreshToken.isNotEmpty()) {
                val newToken = runBlocking {
                    apiServiceProvider().refreshToken(RefreshTokenRequest(refreshToken))
                        .body()?.accessToken
                }

                if (!newToken.isNullOrEmpty()) {
                    runBlocking {
                        val userData = userPreference.getUserData().firstOrNull()
                        if (userData?.user != null) {
                            userPreference.saveUserData(userData.user, newToken, refreshToken)
                        }
                    }

                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                    response.close()
                    response = chain.proceed(newRequest)
                }
            }
        }
        return response
    }
}
