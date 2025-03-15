package com.practice.fixkan.model

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class ReportData(
    val typeReport: String,
    val photoUri: String,
    val lat: Double,
    val long: Double,
    val admArea: String,
    val subAdmArea: String,
    val local: String,
    val subLocal: String
)
