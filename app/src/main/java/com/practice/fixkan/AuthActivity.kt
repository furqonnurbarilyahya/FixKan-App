package com.practice.fixkan

import android.content.Context
import android.content.Intent
import com.practice.fixkan.data.remote.repository.AuthRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.data.remote.retrofit.ApiConfig
import com.practice.fixkan.screen.HomeScreen
import com.practice.fixkan.screen.login.LoginScreen
import com.practice.fixkan.screen.register.RegisterScreen
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")


class AuthActivity: ComponentActivity() {
    private var isCheckingLogin by mutableStateOf(true) // Menunda tampilan UI

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)

        val userPreference = UserPreference.getInstance(dataStore)

        lifecycleScope.launch {
            val userData = userPreference.getUserData().firstOrNull()
            if (userData?.accessToken?.isNotEmpty() == true) {
                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                finish()
            } else {
                isCheckingLogin = false // Jika belum login, baru tampilkan UI
            }
        }

        setContent {
            if (!isCheckingLogin) {
                AuthApp() // Tampilkan UI hanya setelah pengecekan selesai
            }
        }
    }
}


@Composable
fun AuthApp() {
    val navController: NavHostController = rememberNavController()

    val context = LocalContext.current
    val apiService = ApiConfig.ReportApiService()
    val repository = MainRepository(apiService)
    val userPreference = UserPreference.getInstance(context.dataStore) // Gunakan 'this' sebagai context
    val authRepository = AuthRepository(apiService, userPreference)
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, userPreference))



    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, authViewModel)
        }
        composable("register") {
            RegisterScreen(navController, authViewModel, repository)
        }
    }
}