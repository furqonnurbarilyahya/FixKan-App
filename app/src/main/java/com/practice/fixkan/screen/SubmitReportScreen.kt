package com.practice.fixkan.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.practice.fixkan.R
import com.practice.fixkan.component.EditableDropdownSelector
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.model.ReportData
import com.practice.fixkan.screen.CreateReport.ReportViewModel
import com.practice.fixkan.ui.theme.FixKanTheme
import kotlinx.serialization.json.Json

@Composable
fun SubmitReportScreen(navController: NavController, backStackEntry: NavBackStackEntry) {
    val jsonData = backStackEntry.arguments?.getString("reportData") ?: return
    val reportData = Json.decodeFromString<ReportData>(Uri.decode(jsonData))

    var typeReport by remember { mutableStateOf(reportData.typeReport) }
    var latitude by remember { mutableStateOf(reportData.lat.toString()) }
    var longitude by remember { mutableStateOf(reportData.long.toString()) }

    var detailAddress by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedProvince by remember { mutableStateOf(reportData.admArea) }
    var selectedRegency by remember { mutableStateOf(reportData.subAdmArea) }
    var selectedDistrict by remember { mutableStateOf(reportData.local) }
    var selectedVillage by remember { mutableStateOf(reportData.subLocal) }

    val reportViewModel: ReportViewModel = viewModel()

    val provinces by reportViewModel.provinces.collectAsState()
    val regencies by reportViewModel.regencies.collectAsState()
    val districts by reportViewModel.districts.collectAsState()
    val villages by reportViewModel.villages.collectAsState()

//    val context = LocalContext.current
//    val bitmap = remember(reportData.photoUri) {
//        reportData.photoUri?.let { uriString ->
//            val uri = Uri.parse(uriString)  // Konversi String ke Uri
//            decodeUriToBitmap(context, uri)
//        }
//    }

    LaunchedEffect(Unit) {
        reportViewModel.fetchProvinces()
        Log.d("SubmitScreen", "Received Photo URI: ${reportData.photoUri}")
    }

    Scaffold(
        topBar = {
            TopBar("Submit Laporan", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Foto Laporan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

//            bitmap?.let {
//                Image(
//                    bitmap = it.asImageBitmap(),
//                    contentDescription = "Selected Image",
//                    modifier = Modifier.size(200.dp)
//                )
//            } ?: Text("Failed to load image")

            Spacer(Modifier.height(12.dp))

            ReportTypeDropdown(typeReport = typeReport, onTypeSelected = {typeReport = it})

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
            EditableDropdownSelector(
                label = "Pilih Provinsi",
                options = provinces.map { it.name },
                selectedOption = selectedProvince,
                onOptionSelected = { selectedName ->
                    val selected = provinces.find { it.name == selectedName }
                    selectedProvince = selected?.name ?: "" // Simpan nama provinsi
                    selectedRegency = "" // Reset kabupaten/kota saat provinsi berubah
                    selectedDistrict = "" // Reset kecamatan
                    selectedVillage = "" // Reset desa
                    selected?.let { reportViewModel.fetchRegencies(it.id) } // Ambil kabupaten berdasarkan provinsi
                }
            )

            // Dropdown Kabupaten/Kota
            EditableDropdownSelector(
                label = "Pilih Kabupaten/Kota",
                options = regencies.map { it.name },
                selectedOption = selectedRegency,
                onOptionSelected = { selectedName ->
                    val selected = regencies.find { it.name == selectedName }
                    selectedRegency = selected?.name ?: ""
                    selectedDistrict = "" // Reset kecamatan
                    selectedVillage = "" // Reset desa
                    selected?.let { reportViewModel.fetchDistricts(it.id) } // Ambil kecamatan berdasarkan kabupaten
                }
            )

            // Dropdown Kecamatan
            EditableDropdownSelector(
                label = "Pilih Kecamatan",
                options = districts.map { it.name },
                selectedOption = selectedDistrict,
                onOptionSelected = { selectedName ->
                    val selected = districts.find { it.name == selectedName }
                    selectedDistrict = selected?.name ?: ""
                    selectedVillage = "" // Reset desa
                    selected?.let { reportViewModel.fetchVillages(it.id) } // Ambil desa berdasarkan kecamatan
                }
            )

            // Dropdown Desa
            EditableDropdownSelector(
                label = "Pilih Desa/Kelurahan",
                options = villages.map { it.name },
                selectedOption = selectedVillage,
                onOptionSelected = { selectedName ->
                    val selected = villages.find { it.name == selectedName }
                    selectedVillage = selected?.name ?: "" // Simpan nama desa
                }
            )

            OutlinedTextField(
                value = detailAddress,
                onValueChange = { detailAddress = it },
                label = { Text("Alamat Tambahan") },
                placeholder = { Text("Contoh:" +
                        "Sebelah Utara Indosat Center") },
                trailingIcon = {
                    if (detailAddress.isNotEmpty()) {
                        IconButton(onClick = { detailAddress = "" }){
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Lebih tinggi agar terlihat luas
                maxLines = 3, // Maksimum 3 baris
                singleLine = false // Agar bisa multiline
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi") },
                placeholder = { Text("Deskripsi Lengkap Laporan") },
                trailingIcon = {
                    if (description.isNotEmpty()) {
                        IconButton(onClick = { description = "" }){
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
                onClick = { /* Simpan laporan */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
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
        }
    }
}

@Composable
fun ReportTypeDropdown(typeReport: String, onTypeSelected: (String) -> Unit) {
    val reportOptions = listOf("Jalan Rusak", "Sampah Berserakan", "Bangunan Retak", "Bangunan Roboh", "Jembatan Rusak")
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

fun decodeUriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        Log.e("SubmitScreen", "Failed to decode URI: ${e.message}")
        null
    }
}



@Preview
@Composable
private fun SubmitReportScreenPreview() {
    FixKanTheme {
//        SubmitReportScreen(
//
//        )
    }
}