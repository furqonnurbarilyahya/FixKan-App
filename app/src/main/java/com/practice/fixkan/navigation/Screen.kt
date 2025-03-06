package com.practice.fixkan.navigation

sealed class Screen (val route: String) {
    data object Home: Screen("home")
    data object Report: Screen("report")
    data object Profile: Screen("profile")
}