package com.practice.fixkan.screen.ListReport

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.component.SearchBar
import com.practice.fixkan.component.SearchWithFilter
import com.practice.fixkan.data.UiState
import com.practice.fixkan.di.Injection
import com.practice.fixkan.model.response.DataItem
import com.practice.fixkan.ui.theme.FixKanTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ListReportScreen(
    context: Context,
    listReportViewModel: ListReportViewModel = viewModel(factory = MainViewModelFactory(Injection.provideMainRepository(context)))
) {
    var searchReport by remember { mutableStateOf("") }
//    var listReport by remember { mutableStateOf(emptyList<DataItem>()) }

//    var searchQuery by remember { mutableStateOf("") }
//    var selectedType by remember { mutableStateOf("") }
//    var selectedProvince by remember { mutableStateOf("") }
//    var selectedDistrict by remember { mutableStateOf("") }
//    var selectedSubDistrict by remember { mutableStateOf("") }
//    var selectedVillage by remember { mutableStateOf("") }
//    var sortByCreatedAt by remember { mutableStateOf(true) }
//    var isAscending by remember { mutableStateOf(true) }

    val uiState by listReportViewModel.uiState.collectAsState()

    listReportViewModel.listReportState.collectAsState(initial = UiState.Loading).value.let {
        LaunchedEffect(Unit) {
            listReportViewModel.getListReport()
        }

        Log.d("UI_Update", "Current State: $uiState")

        when(it) {
            is UiState.Loading -> {
                Log.d("UI_STATE", "Loading state")
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color.Black
                    )
                }
            }
            is UiState.Success -> {
                val report = it.data
                Log.d("UI_STATE", "Succes state with data: $report")
                Column (
                    Modifier.fillMaxSize()
                ) {
                    Box(
                        Modifier.background(Color(android.graphics.Color.parseColor("#276561")))
                    ) {
                        SearchWithFilter()
                    }
                    ListReportItem(
                        reportItem = report,
                        navigateToReportDetail = {},
                        searchQuery = searchReport
                    )
                }
            }

            else -> {
                Toast.makeText(context, "Maaf Tidak ada Koneksi Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun ListReportItem(
    reportItem: List<DataItem>,
    navigateToReportDetail: (String) -> Unit,
    searchQuery: String,
) {
    val filteredReports = reportItem.filter {
        it.typeReport.contains(searchQuery, ignoreCase = true)
    }
    Log.d("LIST_RENDER", "Rendering list with ${reportItem.size} items")
    LazyColumn (
    ) {
        items(filteredReports) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clickable { }
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = Color.Black.copy(alpha = 0.2f),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)), // Glassmorphism effect
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color(0xFF1B5E20).copy(alpha = 0.3f)), // Border branding hijau tua
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
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Coordinates",
                                tint = Color(0xFF388E3C),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Lat: ${it.latitude}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Long: ${it.longitude}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Baris 2: Lokasi Detail (Provinsi, Kabupaten, Kecamatan, Desa)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Place,
                                contentDescription = "Location",
                                tint = Color(0xFF388E3C),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${it.province}, ${it.district}, ${it.subdistrict}, ${it.village}",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
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

@Preview(showBackground = true)
@Composable
private fun ListReportItemPreview() {
    FixKanTheme {
        BuatTampilanListReport()
    }
}

@Composable
fun BuatTampilanListReport() {
    Card (
        Modifier
            .clickable { }
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Gray),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row (
            Modifier.fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = com.practice.fixkan.R.drawable.ic_menu2),
                contentDescription = null
            )
            Column (
                Modifier.fillMaxWidth()
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "Jalan Rusak",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row (
                    Modifier.fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Report"
                    )
                    Text(
                        text = "Sumenep"
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Diunggah pada: "
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A_XL, showSystemUi = true)
@Composable
private fun ListReportPreview() {
    FixKanTheme {
//        ListReportScreen()
    }
}