package com.practice.fixkan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.practice.fixkan.ui.theme.FixKanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FixKanTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#E7FFF2")))
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#276561")),
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
        ) {
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp),
                        Color.White
                    )
                }
                Text(
                    text = "Selamat Datang, User",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Spacer(Modifier.width(100.dp))
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(30.dp),
                        Color.White
                    )
                }
            }
        }
//        Image(
//            modifier = Modifier.padding(20.dp),
//            painter = painterResource(id = R.drawable.banner_home),
//            contentDescription = "banner_home"
//        )
        Column (
            Modifier.fillMaxWidth()
                .padding(20.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.banner_home),
                contentDescription = "banner_home"
            )
            Row (
                Modifier.fillMaxWidth()
                    .padding(top = 20.dp, start = 80.dp, end = 80.dp)
            ) {
                Column(
                    Modifier.weight(0.5f)
                        .padding(end = 16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {  },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier.height(50.dp)
                            .width(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu1),
                            contentDescription = null,
                            modifier = Modifier.clip(shape = RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Buat Laporan",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
                Column(
                    Modifier.weight(0.5f)
                        .padding(start = 16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {  },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier.height(50.dp)
                            .width(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu2),
                            contentDescription = null,
                            modifier = Modifier.clip(shape = RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Statistik",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
            Row (
                Modifier.fillMaxWidth()
                    .padding(top = 20.dp, start = 80.dp, end = 80.dp)
            ) {
                Column(
                    Modifier.weight(0.5f)
                        .padding(end = 16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {  },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier.height(50.dp)
                            .width(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu3),
                            contentDescription = null,
                            modifier = Modifier.clip(shape = RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Riwayat Laporan",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
                Column(
                    Modifier.weight(0.5f)
                        .padding(start = 16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {  },
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier.height(50.dp)
                            .width(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu4),
                            contentDescription = null,
                            modifier = Modifier.clip(shape = RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Mode Relawan",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 14.dp),
            text = "Tips Pengunaan Aplikasi",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp)
        ) {
            Card (
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.height(160.dp)
                    .width(240.dp)
                    .clickable {  },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
            ) {
                Column (
                    Modifier.fillMaxSize(),
                ) {
                    Box(
                        Modifier.width(240.dp)
                            .height(120.dp)
                            .background(Color.White)
                    ){
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_menu4),
//                            contentDescription = null
//                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp),
                        text = "Cara Membuat Laporan",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Card (
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.height(160.dp)
                    .width(240.dp)
                    .clickable {  },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
            ) {
                Column (
                    Modifier.fillMaxSize(),
                ) {
                    Box(
                        Modifier.width(240.dp)
                            .height(120.dp)
                            .background(Color.White)
                    ){
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_menu4),
//                            contentDescription = null
//                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp),
                        text = "AI Salah Deteksi Laporan?",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Card (
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.height(160.dp)
                    .width(240.dp)
                    .clickable {  },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
            ) {
                Column (
                    Modifier.fillMaxSize(),
                ) {
                    Box(
                        Modifier.width(240.dp)
                            .height(120.dp)
                            .background(Color.White)
                    ){
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_menu4),
//                            contentDescription = null
//                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp),
                        text = "Cara Membuat Laporan",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Card (
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.height(160.dp)
                    .width(240.dp)
                    .clickable {  },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
            ) {
                Column (
                    Modifier.fillMaxSize(),
                ) {
                    Box(
                        Modifier.width(240.dp)
                            .height(120.dp)
                            .background(Color.White)
                    ){
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_menu4),
//                            contentDescription = null
//                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp),
                        text = "Cara Membuat Laporan",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//    val context =  LocalContext.current
//    val classifier = remember { ImageClassifierHelper(context) }
//    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
//
//    // Camera Launcher
//    val cameraLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicturePreview(),
//        onResult = { bitmap ->
//            bitmap?.let {
//                val uri = Uri.parse(MediaStore.Images.Media.insertImage(context.contentResolver, it, null, null))
//                imageUri = uri
//            }
//        }
//    )
//
//    // Gallery Launcher
//    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        uri?.let {
//            imageUri = it
////            imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
////            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
////                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
////                } else {
////                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
////            }
//            val inputStream = context.contentResolver.openInputStream(it)
//            val bitmap = BitmapFactory.decodeStream(inputStream)
//            inputStream?.close()
//            imageBitmap = bitmap
//        }
//    }
//
//    Column (
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(0.15f)
//                .background(
//                    Color(android.graphics.Color.parseColor("#276561")),
//                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
//                )
//        ) {
//            Row (
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 50.dp, horizontal = 12.dp),
////                        .background(Color(android.graphics.Color.parseColor("#003092"))),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Absolute.SpaceAround
//            ){
//                IconButton(
//                    onClick = {},
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = "Profile",
//                        modifier = Modifier.size(40.dp),
//                        Color.White
//                    )
//                }
//                Text(
//                    text = "Selamat Datang, User",
//                    color = Color.White,
//                    fontSize = 18.sp
//                )
//
//                Spacer(modifier = Modifier.width(130.dp))
//
//                IconButton(
//                    onClick = {},
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Notifications,
//                        contentDescription = "Notifications",
//                        modifier = Modifier.size(30.dp),
//                        Color.White
//                    )
//                }
//            }
////            Column (
////
////            ) {
////
////
////            }
//        }
//
//        Box (
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(0.85f)
//        ) {
//            Column (
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "Gunakan AI untuk Deteksi & Laporkan Masalah dengan Mudah!",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.headlineMedium,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Row (
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    Button(
//                        onClick = {
//                            cameraLauncher.launch(null)
//                        },
//                        shape = RoundedCornerShape(10.dp),
//                        modifier = Modifier.size(height = 45.dp,width = 140.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(android.graphics.Color.parseColor("#276561"))
//                        )
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.baseline_photo_camera_24),
//                            contentDescription = "Camera",
//                            modifier = Modifier.padding(end = 10.dp)
//                        )
//                        Text(
//                            text = "Kamera",
//                            fontSize = 16.sp
//                        )
//                    }
//                    Button(
//                        onClick = {
//                            galleryLauncher.launch("image/*")
//                        },
//                        shape = RoundedCornerShape(10.dp),
//                        modifier = Modifier.size(height = 45.dp,width = 140.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(android.graphics.Color.parseColor("#276561"))
//                        )
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.baseline_galery_24),
//                            contentDescription = "Star",
//                            modifier = Modifier.padding(end = 10.dp)
//                        )
//                        Text(
//                            text = "Galeri",
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Box (
//                    modifier = Modifier
//                        .size(300.dp)
//                        .background(
//                            color = Color.White,
//                            shape = RoundedCornerShape(8.dp)
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    if (imageUri != null) {
//                        Image(
//                            painter = rememberAsyncImagePainter(imageUri),
//                            contentDescription = "Selected Image",
//                            modifier = Modifier.fillMaxSize()
//                        )
//                        IconButton(
//                            modifier = Modifier
//                                .align(Alignment.TopEnd)
//                                .padding(top = 8.dp, end = 4.dp)
//                                .clip(CircleShape)
//                                .background(Color.Black)
//                                .zIndex(2f),
//                            onClick = {
//                                imageUri = null
//                            }
//                        ) {
//                            Icon(
//                                Icons.Default.Clear,
//                                contentDescription = "Clear Image",
//                                tint = Color.White
//                            )
//                        }
//                    } else {
//                        Image(
//                            painter = painterResource(id = R.drawable.upload_image_guide),
//                            contentDescription = "Default Image",
//                            modifier = Modifier.fillMaxSize()
//                            )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(40.dp))
//
////                if (imageUri == null) {
////                    Button(
////                        onClick = {},
////                        shape = RoundedCornerShape(8.dp),
////                        colors = ButtonDefaults.buttonColors(
////                            containerColor = Color.Yellow
////                        ),
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .padding(start = 40.dp, end = 40.dp)
////                    ) {
////                        Text(
////                            text = "Panduan Unggah Gambar",
////                            fontSize = 20.sp,
////                            fontWeight = FontWeight.SemiBold,
////                            style = MaterialTheme.typography.headlineMedium
////                        )
////                    }
////
////                }
//                Button(
//                    onClick = {
//                        imageBitmap?.let { bitmap ->
//                            val result = classifier.classifyImage(bitmap)
//                            Toast.makeText(context, "Hasil $result", Toast.LENGTH_SHORT).show()
//                        } ?: run {
//                            Toast.makeText(context, "Pilih Gambar Terlebih Dahulu", Toast.LENGTH_SHORT).show()
//                        }
//                    },
//                    enabled = imageUri != null,
//                    shape = RoundedCornerShape(8.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(android.graphics.Color.parseColor("#276561"))
//                    ),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 40.dp, end = 40.dp)
//                ) {
//                    Text(
//                        text = "Klasifikasi Foto",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.SemiBold,
//                        style = MaterialTheme.typography.headlineMedium
//                    )
//                }
//            }
//        }
//    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A_XL, showSystemUi = true)
@Composable
fun HomePreview() {
    FixKanTheme {
        HomeScreen()
    }
}