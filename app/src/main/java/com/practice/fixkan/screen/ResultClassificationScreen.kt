package com.practice.fixkan.screen
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.navigation.Screen
import com.practice.fixkan.ui.theme.FixKanTheme
import com.practice.fixkan.utils.getCurrentLocation

@Composable
fun ResultClassificationScreen(imageUri: String?, result: String, navController: NavController) {

    var succesDialog by rememberSaveable { mutableStateOf(false) }
    var errorDialog by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    var latitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var longitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var address by rememberSaveable { mutableStateOf<String?>(null) }
    
    var locationPermissionGranted by rememberSaveable { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted // Simpan status izin

        if (isGranted) {
            fetchLocation(context, onSuccess = { lat, lon, addr ->
                latitude = lat
                longitude = lon
                address = addr
                succesDialog = true
                errorDialog = false
            }, onError = {
                errorDialog = true
            })
        } else {
            Toast.makeText(context, "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
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

    Scaffold(
        topBar = {
            TopBar(title = "Hasil Klasifikasi", navController = navController)
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(color = Color(android.graphics.Color.parseColor("#E7FFF2")))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bitmap?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Classified Image",
                    modifier = Modifier.size(400.dp)
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = result,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )
            Spacer(Modifier.height(26.dp))
            Button(
                onClick = {
                    Log.d("LocationDebug", "Button clicked, trying to get location...")

                    if (ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            100
                        )
                        // Jika izin sudah diberikan, coba ambil lokasi
                        getCurrentLocation(context) { lat, lon, addr ->
                            if (lat != null && lon != null && !addr.isNullOrEmpty()) {
                                latitude = lat
                                longitude = lon
                                address = addr
                                succesDialog = true
                                errorDialog = false
                            } else {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    getCurrentLocation(context) { newLat, newLon, newAddr ->
                                        if (newLat != 0.0 && newLon != 0.0 && !newAddr.isNullOrEmpty()) {
                                            latitude = newLat
                                            longitude = newLon
                                            address = newAddr
                                            succesDialog = true
                                            errorDialog = false
                                        } else {
                                            errorDialog = true
                                        }
                                    }
                                }, 2000)
                            }
                        }
                    } else {
                        // Jika izin belum diberikan, minta izin terlebih dahulu
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
                AlertDialog(
                    onDismissRequest = { succesDialog = false },
                    title = { Text(text = "Berhasil Menambahkan Lokasi") },
                    text = {
                        Column {
                            Text(text = "Latitude: $latitude")
                            Text(text = "Longitude: $longitude")
                            Text(text = "Alamat: $address\n\n")
                            Text(text = "Laporan sudah siap")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                succesDialog = false
                            } // Tutup dialog saat tombol "Lanjutkan" ditekan
                        ) {
                            Text("Lanjut")
                        }
                    }
                )
            }

            if (errorDialog) {
                AlertDialog(
                    onDismissRequest = { errorDialog = false },
                    title = { Text(text = "Gagal Menambahkan Lokasi") },
                    text = {
                        Text(
                            text = "- Pastikan bahwa izin lokasi untuk aplikasi ini telah diberikan. Anda dapat mengaturnya melalui: Info Aplikasi → Permission → Location → Izinkan\n\n" +
                                    "Catatan:\n" +
                                    "- Jika izin sudah diberikan, coba klik tombol 'Tambahkan Lokasi' lagi"
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                Log.d("LocationDebug", "Lanjutkan diklik, menutup dialog gagal...")
                                errorDialog = false
                            }
                        ) {
                            Text("Oke")
                        }
                    }
                )
            }
            Spacer(Modifier.height(60.dp))
            Button(
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.Home.route)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#FFFFFF"))
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 4.dp,
                )
            ) {
                Text(
                    text = "Kembali ke Beranda",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(android.graphics.Color.parseColor("#276561"))
                )
            }
        }
    }
}

fun fetchLocation(context: Context, onSuccess: (Double, Double, String) -> Unit, onError: () -> Unit) {
    Toast.makeText(context, "Mengambil lokasi...", Toast.LENGTH_SHORT).show()

    getCurrentLocation(context) { lat, lon, addr ->
        if (lat != 0.0 && lon != 0.0 && !addr.isNullOrEmpty()) {
            onSuccess(lat!!, lon!!, addr)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                getCurrentLocation(context) { newLat, newLon, newAddr ->
                    if (newLat != 0.0 && newLon != 0.0 && !newAddr.isNullOrEmpty()) {
                        onSuccess(newLat!!, newLon!!, newAddr)
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
        ResultClassificationScreen(
            null,
            "Hasil Klasifikasi",
            navController = rememberNavController()
        )
    }
}