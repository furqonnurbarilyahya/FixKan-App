package com.practice.fixkan.screen.ListReport

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    var listReport by remember { mutableStateOf(emptyList<DataItem>()) }

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
                    Box (
                        Modifier.background(Color(android.graphics.Color.parseColor("#276561")))
                            .padding(vertical = 10.dp)
                    ){
                        SearchBar(
                            onSearchTextChanged = {
                                searchReport = it
                            },
                            placeHolder = "Cari Laporan"
                        )
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
        Modifier
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    ) {
        items(filteredReports) {
            Card (
                Modifier
                    .clickable { }
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(android.graphics.Color.parseColor("#ECECEC"))),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Row (
                    Modifier.fillMaxWidth()
                        .padding(12.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.image)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = "Report Image",
                        modifier = Modifier.size(100.dp)
                    )
                    Column (
                        Modifier.fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = it.typeReport,
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
                                text = it.region
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Diunggah pada: " + formatDate(it.createdAt),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
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