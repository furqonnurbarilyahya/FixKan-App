package com.practice.fixkan.navigation

sealed class Screen (val route: String) {
    data object Home: Screen("home")
    data object Report: Screen("report")
    data object Profile: Screen("profile")
    data object Classification: Screen("classification")
    data object ResultClassification: Screen("result_classification/{imageUri}/{result}")
    data object CreateReport: Screen("create_report/{reportData}")
}