package com.practice.fixkan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun LocationInfoDialogButton() {
    var showDialog by remember { mutableStateOf(false) }

    // IconButton untuk menampilkan dialog
    IconButton(onClick = { showDialog = true }) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
            tint = Color.DarkGray,
            modifier = Modifier.size(24.dp)
        )
    }

    // Dialog ketika showDialog = true
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Ikon besar sebagai dekorasi
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color(android.graphics.Color.parseColor("#276561")),
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Judul dialog
                    Text(
                        text = "Tentang Koordinat Lokasi",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Isi pesan dalam dialog
                    Text(
                        text = "Saat pengguna klik tombol 'Simpan Koordinat' dan izin akses lokasi pada aplikasi diberikan, aplikasi akan menyimpan koordinat (Latitude & Longitude) dan pengguna bisa menyertakannya otomatis di halaman Buat Laporan.",
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tombol tutup dialog
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(android.graphics.Color.parseColor("#276561")),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Text("Mengerti")
                    }
                }
            }
        }
    }
}
