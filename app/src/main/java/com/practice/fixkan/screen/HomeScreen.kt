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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.practice.fixkan.R
import com.practice.fixkan.ui.theme.FixKanTheme

@Composable
fun HomeScreen(navController: NavHostController) {
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
                .background(
                    Color(android.graphics.Color.parseColor("#276561")),
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
        ) {
            Row(
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
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner_home),
                contentDescription = "banner_home"
            )
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
                                    Color(android.graphics.Color.parseColor("#276561")),
                                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Buat Laporan",
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
                        .clickable { }
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
                                    Color(android.graphics.Color.parseColor("#276561")),
                                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Statistik",
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
                        .clickable { }
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
                                    Color(android.graphics.Color.parseColor("#276561")),
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
                        .clickable { }
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
                                    Color(android.graphics.Color.parseColor("#276561")),
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

        }
        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 14.dp),
            text = "Tips Pengunaan Aplikasi",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .height(160.dp)
                    .width(240.dp)
                    .clickable { },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
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
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .height(160.dp)
                    .width(240.dp)
                    .clickable { },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
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
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .height(160.dp)
                    .width(240.dp)
                    .clickable { },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
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
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .height(160.dp)
                    .width(240.dp)
                    .clickable { },
                colors = CardDefaults.cardColors(Color(android.graphics.Color.parseColor("#276561"))),
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
}

@Preview(showBackground = true, device = Devices.PIXEL_3A_XL, showSystemUi = true)
@Composable
fun HomePreview() {
    FixKanTheme {
        HomeScreen(navController = rememberNavController())
    }
}