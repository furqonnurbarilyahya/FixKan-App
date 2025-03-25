package com.practice.fixkan

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.data.pref.dataStore
import com.practice.fixkan.data.remote.repository.AuthRepository
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.data.remote.retrofit.ApiConfig
import com.practice.fixkan.navigation.NavigationItem
import com.practice.fixkan.navigation.Screen
import com.practice.fixkan.screen.ClassificationScreen
import com.practice.fixkan.screen.HomeScreen
import com.practice.fixkan.screen.ResultClassificationScreen
import com.practice.fixkan.screen.SubmitReportScreen
import com.practice.fixkan.screen.listReport.ListReportScreen
import com.practice.fixkan.screen.listReport.ListReportViewModel
import com.practice.fixkan.screen.profile.ProfileScreen
import com.practice.fixkan.ui.theme.FixKanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContent {
            FixKanTheme {
                FixKanApp()
            }
        }
    }
}

@Composable
fun FixKanApp(navController: NavHostController = rememberNavController()) {

    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomNav = currentRoute in listOf("home", "report", "profile")

    val apiService = ApiConfig.reportApiService(context)
    val reportRepository = MainRepository(apiService)
    val listReportViewModel: ListReportViewModel = viewModel(factory = MainViewModelFactory(reportRepository))

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color(android.graphics.Color.parseColor("#276561")),
            darkIcons = false
        )
    }
    val userPreference = UserPreference.getInstance(context.dataStore) // Gunakan 'this' sebagai context
    val authRepository = AuthRepository(apiService, userPreference)
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, userPreference))

    Scaffold (
        bottomBar = {
            if (showBottomNav) {
            BottomBar(navController)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerpadding ->
        NavHost (
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerpadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController, userPreference)
            }
            composable(Screen.Report.route) {
                ListReportScreen(context, listReportViewModel = listReportViewModel, repository = reportRepository)
            }
            composable(Screen.Classification.route) {
                ClassificationScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(authViewModel, userPreference)
            }
            composable(Screen.ResultClassification.route) { backstackEntry ->
                val imageUri = backstackEntry.arguments?.getString("imageUri")
                val result = backstackEntry.arguments?.getString("result") ?: "Hasil Tidak Tersedia"
                ResultClassificationScreen(Uri.decode(imageUri), Uri.decode(result), navController = navController, repository = reportRepository)
            }
            composable(Screen.CreateReport.route) {
                SubmitReportScreen(navController = navController, backStackEntry = it, repository = reportRepository, userPreference = userPreference)
            }
        }
    }
}

@Composable
fun BottomBar(
//    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavigationBar (
        modifier = Modifier,
        containerColor = Color(android.graphics.Color.parseColor("#276561")),
        contentColor = Color.White
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = "Beranda",
                icon = R.drawable.bbi_home_24,
                screen = Screen.Home
            ),
            NavigationItem(
                title = "Laporan",
                icon = R.drawable.bbi_report_24,
                screen = Screen.Report
            ),
            NavigationItem(
                title = "Profil",
                icon = R.drawable.bbi_profile_24,
                screen = Screen.Profile
            )
        )
        navigationItems.map {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,
                    )
                },
                label = {
                    Text(
                        text = it.title,
                        color = Color.White
                    )
                },
                selected = currentRoute == it.screen.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(android.graphics.Color.parseColor("#276561")),
                    unselectedIconColor = Color.White
                ),
                onClick = {
                    navController.navigate(it.screen.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A_XL, showSystemUi = true)
@Composable
fun HomePreview() {
    FixKanTheme {
        FixKanApp()
    }
}