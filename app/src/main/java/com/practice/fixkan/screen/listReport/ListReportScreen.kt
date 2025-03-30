package com.practice.fixkan.screen.listReport

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.component.SearchWithFilter
import com.practice.fixkan.data.UiState
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.di.Injection
import com.practice.fixkan.model.response.DataItem
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.MaterialTheme as MaterialTheme1

@Composable
fun ListReportScreen(
    context: Context,
    listReportViewModel: ListReportViewModel = viewModel(
        factory = MainViewModelFactory(
            Injection.provideMainRepository(
                context
            )
        )
    ),
    repository: MainRepository,
    navController: NavController
) {
    var searchReport by remember { mutableStateOf("") }
    val uiState by listReportViewModel.listReportState.collectAsState()

    val selectedFilter by listReportViewModel.selectedFilter.collectAsState()

    LaunchedEffect(Unit) {
        listReportViewModel.getListReport()
    }

    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    Box(
                        Modifier.background(Color(android.graphics.Color.parseColor("#276561")))
                    ) {
                        SearchWithFilter(
                            context,
                            repository,
                            listReportViewModel,
                            onSearchQuery = { searchReport = it })
                    }
                }
                // Loading di tengah
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

        }

        is UiState.Success -> {
            val report = (uiState as UiState.Success<List<DataItem>>).data

            if (report.isEmpty()) {
//                showNoDataDialog
                Column(
                    Modifier.fillMaxSize()
                ) {
                    Box(
                        Modifier.background(Color(android.graphics.Color.parseColor("#276561")))
                    ) {
                        SearchWithFilter(
                            context,
                            repository,
                            listReportViewModel,
                            onSearchQuery = { searchReport = it }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "No Report",
                                    tint = Color.Red,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Laporan tidak ditemukan",
                                    style = MaterialTheme1.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Berdasarkan filter pencarian:",
                                    style = MaterialTheme1.typography.bodyLarge,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                val district =
                                    selectedFilter["district"]?.replace("KABUPATEN ", "KAB. ")
                                        ?: "-"
                                val orderBy = selectedFilter["orderby"]?.replace("DESC", "Terbaru")
                                    ?.replace("ASC", "Terlama") ?: "-"

                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "• Tipe Laporan: ${selectedFilter["typeReport"] ?: "-"}",
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "• Provinsi: ${selectedFilter["province"] ?: "-"}",
                                        color = Color.Gray
                                    )
                                    Text(text = "• Kabupaten/Kota: $district", color = Color.Gray)
                                    Text(
                                        text = "• Kecamatan: ${selectedFilter["subdistrict"] ?: "-"}",
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "• Desa: ${selectedFilter["village"] ?: "-"}",
                                        color = Color.Gray
                                    )
                                    Text(text = "• Urut Berdasarkan: $orderBy", color = Color.Gray)
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { listReportViewModel.getListReport() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF007BFF
                                        )
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Segarkan Laporan")
                                }
                            }
                        }
                    }
                }
            } else {
                Column(
                    Modifier.fillMaxSize()
                ) {
                    Box(
                        Modifier.background(Color(android.graphics.Color.parseColor("#276561")))
                    ) {
                        SearchWithFilter(
                            context,
                            repository,
                            listReportViewModel,
                            onSearchQuery = { searchReport = it }
                        )
                    }
                    ListReportItem(
                        context = context,
                        reportItem = report,
//                        navigateToReportDetail = {},
                        navController = navController,
                        searchQuery = searchReport,
                        listReportViewModel = listReportViewModel
                    )
                }
            }
        }

        is UiState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "No Connection",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFFD32F2F) // Warna merah untuk error indication
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Laporan Gagal Dimuat",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pastikan perangkat terhubung dengan koneksi internet yang stabil.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Button(
                    onClick = { listReportViewModel.getListReport() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(
                            0xFF007BFF
                        )
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Segarkan Laporan")
                }
            }

        }
    }
}


@Composable
fun ListReportItem(
    context: Context,
    reportItem: List<DataItem>,
//    navigateToReportDetail: (String) -> Unit,
    navController: NavController,
    searchQuery: String,
    listReportViewModel: ListReportViewModel = viewModel(
        factory = MainViewModelFactory(
            Injection.provideMainRepository(
                context
            )
        )
    )
) {
    val filteredReports = reportItem.filter {
        it.typeReport.contains(searchQuery, ignoreCase = true)
    }

    if (filteredReports.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tipe laporan tidak ditemukan",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn {
            items(filteredReports) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .clickable {
                            listReportViewModel.selectReport(it)
                            navController.navigate("detail_report")
                        }
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Color.Black.copy(alpha = 0.2f),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)), // Glassmorphism effect
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF1B5E20).copy(alpha = 0.3f)
                    ), // Border branding hijau tua
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat untuk efek kaca
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image Section dengan Overlay Gradient
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.Gray.copy(alpha = 0.2f)) // Placeholder jika gambar loading
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it.image)
                                    .crossfade(true)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .scale(Scale.FILL)
                                    .build(),
                                contentDescription = "Report Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.4f)
                                            )
                                        )
                                    )
                            ) // Gradient Overlay untuk premium look
                        }

                        // Informasi Laporan
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            // Judul Laporan
                            Text(
                                text = it.typeReport,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20), // Branding hijau tua
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Baris 1: Latitude & Longitude
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(
//                                    imageVector = Icons.Outlined.LocationOn,
//                                    contentDescription = "Coordinates",
//                                    tint = Color(0xFF388E3C),
//                                    modifier = Modifier.size(16.dp)
//                                )
//                                Spacer(modifier = Modifier.width(6.dp))
//                                Column {
//                                    Text(
//                                        text = "Lat: ${it.latitude}",
//                                        fontSize = 12.sp,
//                                        color = Color.Gray
//                                    )
//                                    Text(
//                                        text = "Long: ${it.longitude}",
//                                        fontSize = 12.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Baris 2: Lokasi Detail (Provinsi, Kabupaten, Kecamatan, Desa)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Place,
                                    contentDescription = "Location",
                                    tint = Color(0xFF388E3C),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Column {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Provinsi: ${it.province}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Kab/Kota: ${it.district.replace("KABUPATEN ", "KAB. ")}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Kecamatan: ${it.subdistrict}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Desa/Kel: ${it.village}",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Baris 3: Tanggal Upload
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.DateRange,
                                    contentDescription = "Upload Date",
                                    tint = Color(0xFF388E3C),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Diunggah pada: " + formatDate(it.createdAt),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))

    return try {
        val date = inputFormat.parse(dateString) ?: return "Format Error"
        outputFormat.format(date)
    } catch (e: Exception) {
        "Format Error"
    }
}