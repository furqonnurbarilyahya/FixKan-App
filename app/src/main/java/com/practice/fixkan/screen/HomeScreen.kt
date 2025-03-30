package com.practice.fixkan.screen

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.practice.fixkan.R
import com.practice.fixkan.component.AutoScrollBanner
import com.practice.fixkan.component.ComingSoonDialog
import com.practice.fixkan.data.pref.UserPreference
import com.practice.fixkan.ui.theme.FixKanTheme

@Composable
fun HomeScreen(navController: NavHostController, userPreference: UserPreference) {

    var showDialog by remember { mutableStateOf(false) }
    val userData by userPreference.getUserData().collectAsState(initial = null)
    val banners = listOf(
        R.drawable.ban1,
        R.drawable.ban2,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#E7FFF2")))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
                    ambientColor = Color.Black.copy(alpha = 0.2f),
                    spotColor = Color.Black.copy(alpha = 0.4f)
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF276561), // Warna Brand - Deep Green
                            Color(0xFF3A857F)  // Warna Brand - Lighter Green
                        )
                    ),
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile Section
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Selamat Datang,",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        )
                        userData?.user?.let {
                            Text(
                                text = it.name,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Notification Button
                IconButton(
                    onClick = { /* Aksi Notifikasi */ },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        AutoScrollBanner(banners)

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Fitur Tersedia",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 20.dp, end = 20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp, // Memberikan efek shadow
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp)
                    .clickable { navController.navigate("classification") }
            ) {
                Column(
                    Modifier
                        .wrapContentSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier
                            .size(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu1),
                            contentDescription = null,
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#2a7974")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Unggah Laporan",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp)
                    .clickable {
                        navController.navigate("statistics")
                    }
            ) {
                Column(
                    Modifier
                        .wrapContentSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier
                            .size(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu2),
                            contentDescription = null,
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#2a7974")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Rekapitulasi",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp, // Memberikan efek shadow
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 8.dp)
                    .clickable { navController.navigate("history_report") }
            ) {
                Column(
                    Modifier
                        .wrapContentSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier
                            .size(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu3),
                            contentDescription = null,
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#2a7974")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Riwayat Laporan",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 8.dp)
                    .clickable { showDialog = true }
            ) {
                Column(
                    Modifier
                        .wrapContentSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier
                            .size(55.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#276561")),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_menu4),
                            contentDescription = null,
                            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        )
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(28.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#2a7974")),
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Mode Relawan",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            softWrap = true,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Tips Pengunaan Aplikasi",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(14.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .height(160.dp)
                    .width(240.dp)
                    .clickable {
                        navController.navigate("tips_a")
                    },
                colors = CardDefaults.cardColors(Color.White),
            ) {
                Column(
                    Modifier.fillMaxSize(),
                ) {
                    Box(
                        Modifier
                            .width(240.dp)
                            .height(120.dp)
                            .background(Color.Gray)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tips2),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp),
                        text = "Cara membuat laporan",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .height(160.dp)
                    .width(240.dp)
                    .clickable {
                        navController.navigate("tips_b")
                    },
                colors = CardDefaults.cardColors(Color.White),
            ) {
                Column(
                    Modifier.fillMaxSize(),
                ) {
                    Box(
                        Modifier
                            .width(240.dp)
                            .height(120.dp)
                            .background(Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tips1),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp),
                        text = "Solusi Model Belum Siap",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.width(14.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .height(160.dp)
                    .width(240.dp)
                    .clickable {
                        navController.navigate("tips_c")
                    },
                colors = CardDefaults.cardColors(Color.White),
            ) {
                Column(
                    Modifier.fillMaxSize(),
                ) {
                    Box(
                        Modifier
                            .width(240.dp)
                            .height(120.dp)
                            .background(Color.White)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tips3),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp),
                        text = "Hal yang harus dihindari",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        if (showDialog) {
            ComingSoonDialog ( onDismiss = { showDialog = false } )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3A_XL, showSystemUi = true)
@Composable
fun HomePreview() {
    FixKanTheme {
//        HomeScreen(navController = rememberNavController())
    }
}