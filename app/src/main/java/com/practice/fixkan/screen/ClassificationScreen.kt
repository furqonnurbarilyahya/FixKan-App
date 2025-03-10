package com.practice.fixkan.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.material3.Icon
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.rememberAsyncImagePainter
import com.practice.fixkan.R
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.ui.theme.FixKanTheme

@Composable
fun ClassificationScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
//    val classifier = remember { ImageClassifierHelper(context) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

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
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = it
//            imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
//            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
//                } else {
//                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
//            }
                val inputStream = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                imageBitmap = bitmap
            }
        }

    Scaffold(
        topBar = {
            TopBar(title = "Buat Laporan")
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
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
                    .padding(horizontal = 20.dp),
                text = "Ambil atau pilih foto infrastruktur atau lingkungan yang rusak. AI kami akan mengklasifikasikan jenis kerusakan secara otomatis!",
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
                        galleryLauncher.launch("image/*")
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
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp),
                text = "Tips:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp),
                text = "\"Pastikan foto dan pencahayaan jelas agar AI bisa bekerja optimal\"",
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#276561"))
                )
            ) {
                Text(
                    text = "Analisis Foto",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
@Composable
private fun TopBarPreview() {
    FixKanTheme {
        ClassificationScreen()
    }
}