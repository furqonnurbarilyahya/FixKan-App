package com.practice.fixkan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.practice.fixkan.navigation.NavigationItem
import com.practice.fixkan.navigation.Screen
import com.practice.fixkan.screen.HomeScreen
import com.practice.fixkan.ui.theme.FixKanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FixKanTheme {
                FixKanApp()
            }
        }
    }
}

@Composable
fun FixKanApp(navController: NavHostController = rememberNavController()) {

    Scaffold (
        bottomBar = {
            BottomBar(navController)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerpadding ->
        NavHost (
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerpadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen()
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

@Composable
fun BottomBar(
//    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavigationBar (
        modifier = Modifier,
        containerColor = Color(android.graphics.Color.parseColor("#276561")),
        contentColor = Color.White
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            NavigationItem(
                title = "Beranda",
                icon = R.drawable.bbi_home_24,
                screen = Screen.Home
            ),
            NavigationItem(
                title = "Laporan",
                icon = R.drawable.bbi_report_24,
                screen = Screen.Report
            ),
            NavigationItem(
                title = "Profil",
                icon = R.drawable.bbi_profile_24,
                screen = Screen.Profile
            )
        )
        navigationItems.map {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = it.title,
                    )
                },
                label = {
                    Text(
                        text = it.title,
                        color = Color.White
                    )
                },
                selected = currentRoute == it.screen.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(android.graphics.Color.parseColor("#276561")),
                    unselectedIconColor = Color.White
                ),
                onClick = {
                    navController.navigate(it.screen.route){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_3A_XL, showSystemUi = true)
@Composable
fun HomePreview() {
    FixKanTheme {
        FixKanApp()
    }
}