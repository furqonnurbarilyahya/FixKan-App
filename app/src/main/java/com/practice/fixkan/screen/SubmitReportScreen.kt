package com.practice.fixkan.screen

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.component.ConfirmationDialog
import com.practice.fixkan.component.EditableDropdownSelectorFL
import com.practice.fixkan.component.EditableDropdownSelectorProvinceFL
import com.practice.fixkan.component.SuccessDialog
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.model.ReportData
import com.practice.fixkan.navigation.Screen
import com.practice.fixkan.screen.createReport.ReportViewModel
import com.practice.fixkan.ui.theme.FixKanTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun SubmitReportScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    repository: MainRepository,
    userPreference: UserPreference
) {

    val userData by userPreference.getUserData().collectAsState(initial = null)

    val context = LocalContext.current
    val jsonData = backStackEntry.arguments?.getString("reportData") ?: ""
    val reportData = Json.decodeFromString<ReportData>(Uri.decode(jsonData))

    var typeReport by remember { mutableStateOf(reportData.typeReport) }

    var detailAddress by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var latitude = reportData.lat.toString()
    var longitude = reportData.long.toString()

    var selectedProvince by remember {
        mutableStateOf<String?>(
            reportData.admArea?.uppercase() ?: ""
        )
    }
    var selectedRegency by remember {
        mutableStateOf<String?>(
            reportData.subAdmArea?.uppercase() ?: ""
        )
    }
    var selectedDistrict by remember {
        mutableStateOf<String?>(
            reportData.local?.uppercase()?.replace(Regex("(?i)Kecamatan "), "") ?: ""
        )
    }
    var selectedVillage by remember {
        mutableStateOf<String?>(
            reportData.subLocal?.uppercase() ?: ""
        )
    }

    val reportViewModel: ReportViewModel = viewModel(factory = MainViewModelFactory(repository))

    val provinces by reportViewModel.provinces.collectAsState()
    val regencies by reportViewModel.regencies.collectAsState()
    val districts by reportViewModel.districts.collectAsState()
    val villages by reportViewModel.villages.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccesDialog by remember { mutableStateOf(false) }

    val imageUri = reportData.photoUri
    val bitmap = remember(imageUri) {
        imageUri.let { uriStr ->
            try {
                val uri = Uri.parse(uriStr)
                Log.d("Uri_SubmitReportScreen", "Final Parsed Uri: $uri")
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: Exception) {
                Log.e("ResultScreen", "Error decoding image: ${e.message}")
                null
            }
        }
    }

    LaunchedEffect(Unit) {
        reportViewModel.fetchProvinces()
        Log.d("SubmitScreen", "Received Photo URI: ${reportData.photoUri}")
    }

    Scaffold(
        topBar = {
            TopBar("Buat Laporan", navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(android.graphics.Color.parseColor("#E7FFF2")))
                .padding(innerPadding)
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text("Foto Laporan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            bitmap?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Classified Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            } ?: Text("Failed to load image")

            Spacer(Modifier.height(12.dp))

            ReportTypeDropdown(typeReport = typeReport, onTypeSelected = { typeReport = it })

            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.width(10.dp))

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    enabled = false,
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.height(12.dp))

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

            OutlinedTextField(
                value = detailAddress,
                onValueChange = { detailAddress = it },
                label = { Text("Alamat Tambahan") },
                placeholder = {
                    Text(
                        "Misal:\n" +
                                "Berjarak sekitar 30 meter di sebelah utara Indosat Center."
                    )
                },
                trailingIcon = {
                    if (detailAddress.isNotEmpty()) {
                        IconButton(onClick = { detailAddress = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 3,
                singleLine = false
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                placeholder = { Text(text = "Jelaskan penyebab masalah tersebut lebih detail, untuk keperluan analisis lebih lanjut") },
                trailingIcon = {
                    if (description.isNotEmpty()) {
                        IconButton(onClick = { description = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 3,
                singleLine = false
            )

            Spacer(Modifier.height(16.dp))

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    if (
                        selectedProvince.isNullOrEmpty() ||
                        selectedRegency.isNullOrEmpty() ||
                        selectedDistrict.isNullOrEmpty() ||
                        selectedVillage.isNullOrEmpty() ||
                        detailAddress.isEmpty() ||
                        description.isEmpty()
                    ) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Pastikan semua form terisi dengan benar")
                        }
                    } else {
                        // Validasi apakah pilihan user sesuai dengan data API
                        val isValidProvince = provinces.map { it.name }.contains(selectedProvince)
                        val isValidRegency = regencies.map { it.name }.contains(selectedRegency)
                        val isValidDistrict = districts.map { it.name }.contains(selectedDistrict)
                        val isValidVillage = villages.map { it.name }.contains(selectedVillage)

                        when {
                            !isValidProvince -> coroutineScope.launch {
                                snackbarHostState.showSnackbar("Nama Provinsi tidak valid!")
                            }

                            !isValidRegency -> coroutineScope.launch {
                                snackbarHostState.showSnackbar("Nama Kabupaten tidak valid!")
                            }

                            !isValidDistrict -> coroutineScope.launch {
                                snackbarHostState.showSnackbar("Nama Kecamatan tidak valid!")
                            }

                            !isValidVillage -> coroutineScope.launch {
                                snackbarHostState.showSnackbar("Nama Desa tidak valid!")
                            }

                            else -> {
                                // Jika semua valid, tampilkan dialog konfirmasi
                                showConfirmationDialog = true
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#276561"))
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 4.dp,
                )
            ) {
                Text(
                    text = "Kirim Laporan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            userData?.user.let { user ->
                if (showConfirmationDialog) {
                    ConfirmationDialog(
                        onDismiss = { showConfirmationDialog = false },
                        onConfirm = {
                            user?.let {
                                reportViewModel.uploadReport(
                                    imageReport = bitmap!!,
                                    typeReport = typeReport,
                                    userId = it.id,
                                    description = description,
                                    province = selectedProvince!!,
                                    district = selectedRegency!!,
                                    subdistrict = selectedDistrict!!,
                                    village = selectedVillage!!,
                                    addressDetail = detailAddress,
                                    longitude = longitude,
                                    latitude = latitude,
                                )
                            }
                            showConfirmationDialog = false
                            showSuccesDialog = true
                            coroutineScope.launch {
                                delay(3000)
                                showSuccesDialog = false
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }

            if (showSuccesDialog) {
                SuccessDialog()
            }
            Log.d("SubmitReportScreen", "Received Photo URI: $")
        }
    }
}

@Composable
fun ReportTypeDropdown(typeReport: String, onTypeSelected: (String) -> Unit) {
    val reportOptions = listOf(
        "Jalan Rusak",
        "Sampah Berserakan",
        "Bangunan Rusak",
        "Bangunan Roboh",
        "Jembatan Rusak"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(typeReport) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Dropdown Button dengan Border
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text("Pilih Jenis Laporan") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true, // Agar tidak bisa diketik manual
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            reportOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        onTypeSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun SubmitReportScreenPreview() {
    FixKanTheme {
    }
}