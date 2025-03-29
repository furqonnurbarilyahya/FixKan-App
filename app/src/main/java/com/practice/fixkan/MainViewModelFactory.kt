package com.practice.fixkan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.screen.createReport.ReportViewModel
import com.practice.fixkan.screen.listReport.ListReportViewModel
import com.practice.fixkan.screen.statistics.StatisticViewModel

class MainViewModelFactory (private val mainRepository: MainRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListReportViewModel::class.java)) {
            return ListReportViewModel(mainRepository) as T
        } else if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            return ReportViewModel(mainRepository) as T
        } else if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
            return StatisticViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}