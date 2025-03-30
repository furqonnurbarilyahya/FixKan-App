package com.practice.fixkan.navigation

sealed class Screen (val route: String) {
    data object Home: Screen("home")
    data object Report: Screen("report")
    data object DetailReport: Screen("detail_report")
    data object Profile: Screen("profile")
    data object Classification: Screen("classification")
    data object ResultClassification: Screen("result_classification/{imageUri}/{result}")
    data object CreateReport: Screen("create_report/{reportData}")
    data object Statistics: Screen("statistics")
    data object TipsA: Screen("tips_a")
    data object TipsB: Screen("tips_b")
    data object TipsC: Screen("tips_c")
    data object HistoryReport: Screen("history_report")
}