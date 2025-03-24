package com.practice.fixkan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practice.fixkan.screen.HomeScreen
import com.practice.fixkan.screen.login.LoginScreen
import com.practice.fixkan.screen.register.RegisterScreen

class AuthActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContent {
            AuthApp()
        }
    }
}

@Composable
fun AuthApp() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
    }
}