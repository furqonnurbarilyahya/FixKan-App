package com.practice.fixkan.navigation

import androidx.compose.ui.graphics.painter.Painter

data class NavigationItem(
    val title: String,
    val icon: Int,
    val screen: Screen
)