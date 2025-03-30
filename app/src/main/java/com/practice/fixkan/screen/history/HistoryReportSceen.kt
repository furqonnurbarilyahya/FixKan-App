package com.practice.fixkan.screen.history

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.di.Injection
import com.practice.fixkan.model.response.ReportsItem
import com.practice.fixkan.screen.formatDate

@Composable
fun HistoryReportScreen(
    context: Context,
    historyReportViewModel: HistoryReportViewModel = viewModel(
        factory = MainViewModelFactory(
            Injection.provideMainRepository(
                context
            )
        )
    ),
    userPreference: UserPreference,
    navController: NavController
) {

    val userData by userPreference.getUserData().collectAsState(initial = null)

    val reports by historyReportViewModel.reports.collectAsState()
    val isLoading by historyReportViewModel.isLoading.collectAsState()
    val errorMessage by historyReportViewModel.errorMessage.collectAsState()

    LaunchedEffect(userData) {
        userData?.user?.let {
            Log.d("HistoryReportScreen", "Fetching reports for userId: ${it.id}")
            historyReportViewModel.fetchHistoryReports(it.id)
        }
    }

    Scaffold(
        topBar = {
            TopBar("Riwayat Laporan", navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                isLoading -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(reports) { report ->
                            ReportItem(report)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportItem(report: ReportsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { /* Navigasi ke detail laporan */ },
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Header Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Laporan berhasil dibuat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Konten Utama
            Row {
                Image(
                    painter = rememberAsyncImagePainter(report.image),
                    contentDescription = "Gambar laporan",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = report.typeReport,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF37474F)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = report.addressDetail,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Waktu",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formatDate(report.createdAt),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lokasi Detail
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Lokasi",
                    tint = Color(0xFF0288D1),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${report.location.subdistrict}, ${report.location.district}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0288D1)
                )
            }
        }
    }
}
