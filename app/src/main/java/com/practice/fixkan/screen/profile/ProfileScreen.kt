package com.practice.fixkan.screen.profile

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.fixkan.AuthActivity
import com.practice.fixkan.AuthViewModel
import com.practice.fixkan.data.pref.UserPreference
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, userPreference: UserPreference) {
    val context = LocalContext.current
    val userData by userPreference.getUserData().collectAsState(initial = null)
    var isLocationPermissionGranted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF276561), Color(0xFF6AB7A0))))
            .padding(start = 16.dp, end = 16.dp, top = 20.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ) {
        userData?.let { user ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar dengan efek elevation
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .shadow(8.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(100.dp),
                        tint = Color(0xFF276561)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Informasi Akun dalam Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Informasi Akun",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF276561)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileItem("Nama pengguna", user.user.name)
                        ProfileItem("Email", user.user.email)
                        ProfileItem("Akun dibuat sejak", formatDate(user.user.createdAt))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Informasi Lokasi dalam Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Alamat",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileItem("Provinsi", user.user.location.province)
                        ProfileItem("Kab/Kota", user.user.location.district)
                        ProfileItem("Kecamatan", user.user.location.subdistrict)
                        ProfileItem("Desa/Kelurahan", user.user.location.village)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Pengaturan",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Baris dengan teks dan saklar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Perizinan Lokasi",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Switch(
                                checked = isLocationPermissionGranted,
                                onCheckedChange = { isLocationPermissionGranted = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor = Color.Gray
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Tombol Logout dengan desain modern
                Button(
                    onClick = {
                        authViewModel.logout {
                            Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, AuthActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Logout", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}


fun formatDate(isoDate: String): String {
    return try {
        val instant = Instant.parse(isoDate) // Ubah string ke Instant
        val localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate() // Konversi ke LocalDate
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID")) // Format Indonesia
        localDate.format(formatter) // Format sebagai string
    } catch (e: Exception) {
        "Format tanggal salah" // Jika gagal, tampilkan pesan error
    }
}


