package com.practice.fixkan.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.practice.fixkan.R
import com.practice.fixkan.ui.theme.FixKanTheme

@Composable
fun AddLocationSucces(
    latitude: Double?,
    longitude: Double?,
    address: String?,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Efek shadow
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animasi Icon
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(android.graphics.Color.parseColor("#276561")), // Warna hijau sukses
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Judul Dialog
                Text(
                    text = "Berhasil Menambahkan Lokasi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Detail lokasi dalam card dengan background abu-abu
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "📍 Latitude: $latitude", fontSize = 14.sp, color = Color.Black)
                        Text(text = "📍 Longitude: $longitude", fontSize = 14.sp, color = Color.Black)
                        Text(text = "🛣️ Alamat: $address", fontSize = 14.sp, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol "Lanjutkan"
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#276561"))),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lanjutkan", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}


@Composable
fun AddLocationFailed(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp) // Efek shadow
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animasi Icon
                Icon(
                    painter = painterResource(id = R.drawable.baseline_location_off_24),
                    contentDescription = "Success",
                    tint = Color(android.graphics.Color.parseColor("#276561")), // Warna hijau sukses
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Judul Dialog
                Text(
                    text = "Gagal Menambahkan Lokasi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Detail lokasi dalam card dengan background abu-abu
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Pastikan bahwa izin lokasi untuk aplikasi ini telah diberikan. Anda dapat mengaturnya melalui:\nInfo Aplikasi → Permission → Location → Izinkan Lokasi\n\n" +
                                    "Catatan:\n" +
                                    "- Jika izin sudah diberikan, coba klik tombol 'Tambahkan Lokasi' lagi",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol "Lanjutkan"
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(android.graphics.Color.parseColor("#276561"))),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lanjutkan", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccesDialogPreview() {
    FixKanTheme {
        AddLocationSucces(
            latitude = 0.88888,
            longitude = 08.08765,
            address = "Jl. Contoh No. 123",
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FailedDialogPreview() {
    FixKanTheme {
        AddLocationFailed(
            onDismiss = {}
        )
    }
}
