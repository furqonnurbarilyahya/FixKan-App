package com.practice.fixkan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.screen.ListReport.ListReportViewModel

class MainViewModelFactory (private val mainRepository: MainRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListReportViewModel::class.java)) {
            return ListReportViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}