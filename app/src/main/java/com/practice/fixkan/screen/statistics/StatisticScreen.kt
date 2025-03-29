package com.practice.fixkan.screen.statistics

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.component.EditableDropdownSelectorFL
import com.practice.fixkan.component.EditableDropdownSelectorProvinceFL
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.di.Injection
import com.practice.fixkan.screen.createReport.ReportViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticScreen(
    context: Context,
    viewModel: StatisticViewModel = viewModel(
        factory = MainViewModelFactory(
            Injection.provideMainRepository(
                context
            )
        )
    ),
    navController: NavController,
    repository: MainRepository
) {
    val statistics by viewModel.statistics.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var selectedRegency by remember { mutableStateOf<String?>(null) }
    var selectedDistrict by remember { mutableStateOf<String?>(null) }
    var selectedVillage by remember { mutableStateOf<String?>(null) }

    val reportViewModel: ReportViewModel = viewModel(factory = MainViewModelFactory(repository))

    val provinces by reportViewModel.provinces.collectAsState()
    val regencies by reportViewModel.regencies.collectAsState()
    val districts by reportViewModel.districts.collectAsState()
    val villages by reportViewModel.villages.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    var showBottomSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopBar("Rekapitulasi Laporan", navController = navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\uD83D\uDCCA \uD83D\uDD0E Temukan wawasan dari data laporan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gunakan filter di bawah untuk menyaring laporan berdasarkan provinsi, kabupaten/kota, kecamatan, dan kelurahan/desa. Pencarian spesifik ini membantu Anda memahami tren laporan dan mendukung analisis serta pengambilan keputusan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "\uD83D\uDCCC Manfaat Menggunakan Filter:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            "✔ Pilih wilayah yang ingin dianalisis untuk mendapatkan data yang lebih relevan.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "✔ Gunakan kombinasi filter untuk mempersempit pencarian sesuai kebutuhan.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "✔ Identifikasi pola atau permasalahan utama yang muncul di berbagai daerah.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Dropdown Provinsi
                    EditableDropdownSelectorProvinceFL(
                        label = "Provinsi",
                        options = provinces.map { it.name },
                        selectedOption = selectedProvince,
                        onOptionSelected = { selectedName ->
                            val selected = provinces.find { it.name == selectedName }

                            selectedProvince = selected?.name ?: selectedName

                            if (selected != null) {
                                selectedRegency = null
                                selectedDistrict = null
                                selectedVillage = null
                                reportViewModel.fetchRegencies(selected.id) // Ambil kabupaten/kota berdasarkan provinsi
                            }
                        },
                        onUserStartsTyping = { reportViewModel.fetchProvinces() } // Hanya fetch saat user mulai mengetik
                    )

                    // Dropdown Kabupaten/Kota (Aktif hanya jika Provinsi valid)
                    EditableDropdownSelectorFL(
                        label = "Kabupaten/Kota",
                        options = if (selectedProvince != null && provinces.any { it.name == selectedProvince }) regencies.map { it.name } else emptyList(),
                        selectedOption = selectedRegency,
                        onOptionSelected = { selectedName ->
                            val selected = regencies.find { it.name == selectedName }
                            selectedRegency = selected?.name ?: selectedName?.ifBlank { null }
                            selectedDistrict = null
                            selectedVillage = null
                            selected?.let { reportViewModel.fetchDistricts(it.id) }
                        },
                        enabled = selectedProvince != null && provinces.any { it.name == selectedProvince }
                    )

                    // Dropdown Kecamatan (Aktif hanya jika Kabupaten valid)
                    EditableDropdownSelectorFL(
                        label = "Kecamatan",
                        options = if (selectedRegency != null && regencies.any { it.name == selectedRegency }) districts.map { it.name } else emptyList(),
                        selectedOption = selectedDistrict,
                        onOptionSelected = { selectedName ->
                            selectedDistrict = selectedName?.ifBlank { null }
                            selectedVillage = null
                            districts.find { it.name == selectedName }
                                ?.let { reportViewModel.fetchVillages(it.id) }
                        },
                        enabled = selectedRegency != null && regencies.any { it.name == selectedRegency }
                    )

                    // Dropdown Kelurahan/Desa (Aktif hanya jika Kecamatan valid)
                    EditableDropdownSelectorFL(
                        label = "Kelurahan/Desa",
                        options = if (selectedDistrict != null && districts.any { it.name == selectedDistrict }) villages.map { it.name } else emptyList(),
                        selectedOption = selectedVillage,
                        onOptionSelected = { selectedName ->
                            selectedVillage = selectedName?.ifBlank { null }
                        },
                        enabled = selectedDistrict != null && districts.any { it.name == selectedDistrict }
                    )

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val isValidProvince = provinces.map { it.name }.contains(selectedProvince)
                            val isValidRegency = selectedRegency.isNullOrBlank() || regencies.map { it.name }.contains(selectedRegency)
                            val isValidDistrict = selectedDistrict.isNullOrBlank() || districts.map { it.name }.contains(selectedDistrict)
                            val isValidVillage = selectedVillage.isNullOrBlank() || villages.map { it.name }.contains(selectedVillage)

                            when {
                                !isValidProvince -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Nama Provinsi tidak valid!")
                                }
                                !isValidRegency -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Nama Kabupaten/Kota tidak valid!")
                                }
                                !isValidDistrict -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Nama Kecamatan tidak valid!")
                                }
                                !isValidVillage -> coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Nama Kelurahan/Desa tidak valid!")
                                }
                                else -> {
                                    // Jika semua valid, terapkan filter
                                    viewModel.getStatistics(
                                        selectedProvince,
                                        selectedRegency,
                                        selectedDistrict,
                                        selectedVillage
                                    )
                                    showBottomSheet = true
                                }
                            }

                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(android.graphics.Color.parseColor("#276561"))
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 4.dp,
                        )
                    ) {
                        Text(
                            text = "Terapkan Filter",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Button(
                        onClick = {
                            selectedProvince = null
                            selectedRegency = null
                            selectedDistrict = null
                            selectedVillage = null
                        },
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 4.dp,
                        )
                    ) {
                        Text(
                            text = "Reset Filter",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }

                    errorMessage?.let {
                        Text("Error: $it", color = Color.Red)
                    }

                    Spacer(Modifier.height(16.dp))

                    statistics?.let { data ->
                        val barsData = listOf(
                            BarData(
                                point = Point(0f, data.data.bangunanRusak.toFloat()),
                                color = Color.Red,
                                label = "BNK"
                            ),
                            BarData(
                                point = Point(1f, data.data.jembatanRusak.toFloat()),
                                color = Color.Blue,
                                label = "JMR"
                            ),
                            BarData(
                                point = Point(2f, data.data.sampahBerserakan.toFloat()),
                                color = Color.Magenta,
                                label = "SMN"
                            ),
                            BarData(
                                point = Point(3f, data.data.bangunanRoboh.toFloat()),
                                color = Color.Green,
                                label = "BNH"
                            ),
                            BarData(
                                point = Point(4f, data.data.jalanRusak.toFloat()),
                                color = Color.DarkGray,
                                label = "JLK"
                            )
                        )

                        val maxValue = barsData.maxOf { it.point.y }
                        val stepSize = if (maxValue > 0) (maxValue / 5).coerceAtLeast(1f) else 1f

                        val xAxisData = AxisData.Builder()
                            .axisStepSize(30.dp)
                            .steps(barsData.size)
                            .bottomPadding(5.dp)
                            .axisLabelAngle(20f)
                            .labelData { index -> barsData.getOrNull(index)?.label.orEmpty() }
                            .axisLineColor(MaterialTheme.colorScheme.tertiary)
                            .axisLabelColor(MaterialTheme.colorScheme.tertiary)
                            .build()

                        val yAxisData = AxisData.Builder()
                            .steps(5)
                            .topPadding(10.dp)
                            .labelAndAxisLinePadding(10.dp)
                            .axisOffset(10.dp)
                            .labelData { index -> ((index * stepSize).toInt()).toString() }
                            .axisLineColor(MaterialTheme.colorScheme.tertiary)
                            .axisLabelColor(MaterialTheme.colorScheme.tertiary)
                            .build()

                        val barChartData = BarChartData(
                            chartData = barsData,
                            xAxisData = xAxisData,
                            yAxisData = yAxisData,
                            backgroundColor = MaterialTheme.colorScheme.surface
                        )

                        if (showBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    showBottomSheet = false
                                },
                                sheetState = sheetState
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .verticalScroll(rememberScrollState()),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Berikut adalah rekapitulasi laporan:",
                                        fontSize = 20.sp,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Spacer(Modifier.height(16.dp))

                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
//                                                textAlign = TextAlign.Center,
                                                text = when {
                                                    selectedProvince == null && selectedRegency == null && selectedDistrict == null && selectedVillage == null -> "Seluruh Indonesia"
                                                    selectedProvince != null && selectedRegency == null && selectedDistrict == null && selectedVillage == null -> "Provinsi: $selectedProvince"
                                                    selectedProvince != null && selectedRegency != null && selectedDistrict == null && selectedVillage == null -> "Provinsi: $selectedProvince\nKab/Kota: ${
                                                        selectedRegency!!.replace(
                                                            "Kabupaten ",
                                                            "Kab. "
                                                        )
                                                    }"

                                                    selectedProvince != null && selectedRegency != null && selectedDistrict != null && selectedVillage == null -> "Provinsi: $selectedProvince\nKab/Kota: ${
                                                        selectedRegency!!.replace(
                                                            "Kabupaten ",
                                                            "Kab. "
                                                        )
                                                    }\nKecamatan: $selectedDistrict"

                                                    else -> "Provinsi: $selectedProvince\nKab/Kota: ${
                                                        selectedRegency!!.replace(
                                                            "Kabupaten ",
                                                            "Kab. "
                                                        )
                                                    }\nKecamatan: $selectedDistrict\nKelurahan/Desa: $selectedVillage"
                                                },
                                                fontSize = 16.sp,
                                                style = MaterialTheme.typography.bodyMedium,
                                                textAlign = TextAlign.Center,
                                                lineHeight = 30.sp
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(20.dp))

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "Grafik Laporan",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                BarChart(
                                                    modifier = Modifier.height(350.dp),
                                                    barChartData = barChartData
                                                )
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(20.dp))

                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "Keterangan dan Jumlah:",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            SummaryRow(
                                                "BNK  -->",
                                                "Bangunan Rusak",
                                                data.data.bangunanRusak
                                            )
                                            SummaryRow(
                                                "JMR  -->",
                                                "Jembatan Rusak",
                                                data.data.jembatanRusak
                                            )
                                            SummaryRow(
                                                "SMN  -->",
                                                "Sampah Berserakan",
                                                data.data.sampahBerserakan
                                            )
                                            SummaryRow(
                                                "BNH  -->",
                                                "Bangunan Roboh",
                                                data.data.bangunanRoboh
                                            )
                                            SummaryRow(
                                                "JLK  -->",
                                                "Jalan Rusak",
                                                data.data.jalanRusak
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryRow(abbreviation: String, label: String, value: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = abbreviation,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
