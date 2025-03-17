package com.practice.fixkan.model

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class ReportData(
    val typeReport: String,
    val photoUri: String,
    val lat: Double? = 0.0,
    val long: Double? = 0.0,
    val admArea: String? = null,
    val subAdmArea: String? = null,
    val local: String? = null,
    val subLocal: String? = null
)
