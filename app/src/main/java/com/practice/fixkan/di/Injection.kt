package com.practice.fixkan.di

import android.content.Context
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.data.remote.retrofit.ApiConfig

object Injection {
    fun provideMainRepository(context: Context): MainRepository {
        val apiService = ApiConfig.ReportApiService()
        return MainRepository.getInstance(apiService)
    }
}