package com.practice.fixkan.screen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.material3.Icon
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.practice.fixkan.R
import com.practice.fixkan.component.LoadingDialog
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.model.ImageClassificationHelper
import com.practice.fixkan.ui.theme.FixKanTheme
import kotlinx.coroutines.delay

@Composable
fun ClassificationScreen(navController: NavController) {
    

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val classifier = remember { ImageClassificationHelper(context) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }


    var showDialog by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }

    // Camera Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                val uri = Uri.parse(
                    MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        it,
                        null,
                        null
                    )
                )
                imageUri = uri
            }
        }
    )

    // Gallery Launcher
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
//                imageUri = it
////            imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
////            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
////                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
////                } else {
////                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
////            }
//                val inputStream = context.contentResolver.openInputStream(it)
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                inputStream?.close()
//                imageBitmap = bitmap


//                context.contentResolver.takePersistableUriPermission(
//                    it,
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION
//                )
//                val bmp = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
//                imageUri = it
//                imageBitmap = bmp
                try {
                    // Ambil izin baca untuk akses persisten
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    imageUri = it
                    // Konversi URI ke Bitmap
                    val inputStream = context.contentResolver.openInputStream(it)
                    imageBitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                } catch (e: Exception) {
                    Log.e("ClassificationScreen", "Error loading image: ${e.message}")
                }
            }
        }

    if (showResult) {
//        ResultClassificationScreen()
    } else {
        Scaffold(
            topBar = {
                TopBar(title = "Buat Laporan", navController = navController)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(20.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Gunakan AI untuk Deteksi & Laporkan Masalah dengan Mudah!",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    text = "Unggah foto infrastruktur atau lingkungan yang rusak. AI kami akan mengklasifikasikan jenis kerusakan secara otomatis!",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 8.dp, end = 4.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .zIndex(2f),
                            onClick = {
                                imageUri = null
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear Image",
                                tint = Color.White
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.idcamp),
                            contentDescription = "Default Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            cameraLauncher.launch(null)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.size(height = 45.dp, width = 140.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(android.graphics.Color.parseColor("#276561"))
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_camera_alt_24),
                            contentDescription = "Camera",
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(
                            text = "Kamera",
                            fontSize = 16.sp
                        )
                    }
                    Button(
                        onClick = {
                            galleryLauncher.launch(arrayOf("image/*"))
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.size(height = 45.dp, width = 140.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(android.graphics.Color.parseColor("#276561"))
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_insert_photo_24),
                            contentDescription = "Camera",
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(
                            text = "Galeri",
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    text = "Tips:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    text = "\"Pastikan foto dan pencahayaan jelas agar AI bisa bekerja optimal\"",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = {
                        showDialog = true
                    },
                    enabled = imageUri != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
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
                        text = "Analisis Foto",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        LaunchedEffect(showDialog) {
            if (showDialog && imageUri != null){
                delay(3000)

//                val result = imageBitmap?.let {
//                    classifier.classifyImage(it)
//                } ?: "Hasil Tidak Tersedia"

                imageBitmap?.let {
                    val result = classifier.classifyImage(it)
                    val encodedUri = Uri.encode(imageUri.toString())
                    val encodedResult = Uri.encode(result)
                navController.navigate("result_classification/${Uri.encode(encodedUri.toString())}/${Uri.encode(encodedResult)}")
                }

                showDialog = false
                showResult = true
            } else {
                showDialog = false
            }
        }

        if (showDialog) {
            LoadingDialog()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
@Composable
private fun TopBarPreview() {
    FixKanTheme {
//        ClassificationScreen(navHostController = n)
    }
}