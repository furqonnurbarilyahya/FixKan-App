package com.practice.fixkan.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.component.AddLocationFailed
import com.practice.fixkan.component.AddLocationSucces
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.model.ReportData
import com.practice.fixkan.navigation.Screen
import com.practice.fixkan.screen.createReport.ReportViewModel
import com.practice.fixkan.ui.theme.FixKanTheme
import com.practice.fixkan.utils.getCurrentLocation
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun ResultClassificationScreen(imageUri: String?, result: String, navController: NavController, repository: MainRepository) {

    val context = LocalContext.current
    var succesDialog by rememberSaveable { mutableStateOf(false) }
    var errorDialog by rememberSaveable { mutableStateOf(false) }

    var latitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var longitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var address by rememberSaveable { mutableStateOf<String?>(null) }

    val reportViewModel: ReportViewModel = viewModel(factory = MainViewModelFactory(repository))

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var locationPermissionGranted by rememberSaveable { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted // Simpan status izin

        if (isGranted) {
            fetchLocation(
                context,
                onSuccess = { lat, lon, adminArea, subAdminArea, locality, subLocality ->
                    latitude = lat
                    longitude = lon
                    address = "$adminArea, $subAdminArea, $locality, $subLocality"
                    succesDialog = true
                    errorDialog = false
                },
                onError = {
                    errorDialog = true
                })
        } else {
//            Toast.makeText(context, "Akses lokasi belum dizinkan", Toast.LENGTH_LONG).show()
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Akses lokasi belum dizinkan")
            }
            errorDialog = true
        }
    }

    val bitmap = remember(imageUri) {
        imageUri?.let { uriStr ->
            try {
                val uri = Uri.parse(uriStr)
                Log.d("ResultScreen", "Final Parsed Uri: $uri")
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: Exception) {
                Log.e("ResultScreen", "Error decoding image: ${e.message}")
                null
            }
        }
    }

    Log.d("Bitmap_ResultClassificationScreen", "Bitmap: $bitmap")
    Log.d("Uri_ResultClassificationScreen", "Bitmap: $imageUri")

    Scaffold(
        topBar = {
            TopBar(title = "Hasil Klasifikasi", navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(12.dp)
                .background(color = Color(android.graphics.Color.parseColor("#E7FFF2")))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bitmap?.let { imgBitmap ->
                AsyncImage(
                    model = imgBitmap,
                    contentDescription = "Classified Image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Tipe Laporan:",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = result,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )
            Spacer(Modifier.height(26.dp))
            Button(
                onClick = {
                    Log.d("LocationDebug", "Button clicked, trying to get location...")
//                    Toast.makeText(context, "Mengambil Lokasi...", Toast.LENGTH_SHORT).show()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Mengambil Lokasi...")
                    }
                    if (ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Izin sudah diberikan, langsung ambil lokasi
                        getCurrentLocation(context) { lat, lon, adminArea, subAdminArea, locality, subLocality ->
                            if (lat != null && lon != null &&
                                !adminArea.isNullOrEmpty() &&
                                !subAdminArea.isNullOrEmpty() &&
                                !locality.isNullOrEmpty() &&
                                !subLocality.isNullOrEmpty()
                            ) {
                                latitude = lat
                                longitude = lon
                                address = "$adminArea, $subAdminArea, $locality, $subLocality"
                                succesDialog = true
                                errorDialog = false

                                // Set data ke ViewModel
                                reportViewModel.setReportData(
                                    typeReport = result,
                                    photoUri = Uri.encode(imageUri.toString()),
                                    lat = lat,
                                    long = lon,
                                    admArea = adminArea,
                                    subAdmArea = subAdminArea,
                                    local = locality,
                                    subLocal = subLocality
                                )
                                // Logging untuk memastikan data berhasil dikirim ke ViewModel
                                Log.d("LocationDebug", "Data set to ViewModel: photo: $imageUri typeReport=$result lat=$lat, lon=$lon, " +
                                            "adminArea=$adminArea, subAdminArea=$subAdminArea, " +
                                            "locality=$locality, subLocality=$subLocality"
                                )
                            } else {
                                // Jika data lokasi belum lengkap, coba lagi setelah delay 2 detik
                                Handler(Looper.getMainLooper()).postDelayed({
                                    getCurrentLocation(context) { newLat, newLon, newAdmin, newSubAdmin, newLocal, newSubLocal ->
                                        if (newLat != null && newLon != null &&
                                            !newAdmin.isNullOrEmpty() &&
                                            !newSubAdmin.isNullOrEmpty() &&
                                            !newLocal.isNullOrEmpty() &&
                                            !newSubLocal.isNullOrEmpty()
                                        ) {
                                            latitude = newLat
                                            longitude = newLon
                                            address = "$newAdmin, $newSubAdmin, $newLocal, $newSubLocal"
                                            succesDialog = true
                                            errorDialog = false

                                            // Set data ke ViewModel
                                            reportViewModel.setReportData(
                                                typeReport = result,
                                                photoUri = Uri.encode(imageUri.toString()),
                                                lat = newLat,
                                                long = newLon,
                                                admArea = newAdmin,
                                                subAdmArea = newSubAdmin,
                                                local = newLocal,
                                                subLocal = newSubLocal
                                            )
                                        } else {
                                            errorDialog = true
                                        }
                                    }
                                }, 2000)
                            }
                        }
                    } else {
                        // Jika izin belum diberikan, gunakan ActivityResultLauncher untuk meminta izin
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(height = 45.dp, width = 220.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#FFCB22"))
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 4.dp,
                )
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.padding(end = 10.dp),
                    tint = Color.Black
                )
                Text(
                    text = "Tambahkan Lokasi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
            // Dialog untuk menampilkan latitude, longitude, dan alamat
            if (succesDialog) {
                AddLocationSucces(
                    latitude = latitude,
                    longitude = longitude,
                    address = address,
                    onDismiss = {
                        succesDialog = false
                    }
                )
            }

            if (errorDialog) {
                AddLocationFailed(
                    onDismiss = {
                        errorDialog = false
                    }
                )
            }
            Spacer(Modifier.height(60.dp))
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    reportViewModel.setReportData(
                        typeReport = result,
                        photoUri = Uri.encode(imageUri.toString())
                    )
                    val reportData = reportViewModel.reportData.value ?: ReportData(
                        typeReport = result,
                        photoUri = Uri.encode(imageUri.toString())
                    )
                    val jsonData = Uri.encode(Json.encodeToString(reportData))
                    Log.d("Uri_ResultClassificationScreen", "Original URI: $imageUri")
                    Log.d("Uri_ResultClassificationScreen", "Parsed URI: ${Uri.parse(imageUri)}")
                    Log.d("Uri_ResultClassificationScreen", "Encoded JSON: $jsonData")
                    navController.navigate("create_report/$jsonData")
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
                    text = "Laporkan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Button(
                onClick = {
                    navController.navigate(Screen.Classification.route)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#FFFFFF"))
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 4.dp,
                )
            ) {
                Text(
                    text = "Unggah Ulang Foto",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(android.graphics.Color.parseColor("#276561"))
                )
            }
        }
    }
}

fun fetchLocation(
    context: Context,
    onSuccess: (Double, Double, String, String, String, String) -> Unit,
    onError: () -> Unit
) {
//    Toast.makeText(context, "Mengambil lokasi...", Toast.LENGTH_SHORT).show()
//
//    coroutineScope.launch {
//        snackbarHostState.showSnackbar("Mengambil lokasi...")
//    }

    getCurrentLocation(context) { lat, lon, adminArea, subAdminArea, locality, subLocality ->
        if (lat != null &&
            lon != null &&
            !adminArea.isNullOrEmpty() &&
            !subAdminArea.isNullOrEmpty() &&
            !locality.isNullOrEmpty() &&
            !subLocality.isNullOrEmpty()
        ) {
            onSuccess(lat, lon, adminArea, subAdminArea, locality, subLocality)
        } else {
            // Jika lokasi pertama gagal, coba kembali setelah 2 detik
            Handler(Looper.getMainLooper()).postDelayed({
                getCurrentLocation(context) { newLat, newLon, newAdmin, newSubAdmin, newLocal, newSubLocal ->
                    if (newLat != null && newLon != null &&
                        !newAdmin.isNullOrEmpty() &&
                        !newSubAdmin.isNullOrEmpty() &&
                        !newLocal.isNullOrEmpty() &&
                        !newSubLocal.isNullOrEmpty()
                    ) {
                        onSuccess(newLat, newLon, newAdmin, newSubAdmin, newLocal, newSubLocal)
                    } else {
                        onError()
                    }
                }
            }, 2000)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
@Composable
private fun ResultClassificationScreenPreview() {
    FixKanTheme {
//        ResultClassificationScreen(
//            null,
//            "Hasil Klasifikasi",
//            navController = rememberNavController(),
//            repository =
//        )
    }
}