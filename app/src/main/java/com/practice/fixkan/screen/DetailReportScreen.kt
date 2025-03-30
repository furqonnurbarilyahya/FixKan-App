package com.practice.fixkan.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.model.response.DataItem
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DetailReportScreen(
    reportItem: DataItem,
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopBar("Detail Laporan", navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Gambar Laporan
            AsyncImage(
                model = reportItem.image,
                contentDescription = "Gambar Laporan",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row {
                Column {
                    Text(
                        text = "Tipe laporan: ",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Jenis Laporan sebagai Badge
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = reportItem.typeReport,
                            color = Color.White,
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                DateInfo(label = "Diunggah pada:", date = formatDate(reportItem.createdAt))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Informasi Lokasi
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow(label = "Provinsi", value = reportItem.province)
                    DetailRow(label = "Kabupaten", value = reportItem.district)
                    DetailRow(label = "Kecamatan", value = reportItem.subdistrict)
                    DetailRow(label = "Desa", value = reportItem.village)
                    DetailRow(label = "Alamat Tambahan", value = reportItem.addressDetail)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Deskripsi laporan: ",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Deskripsi Laporan
            Text(
                text = reportItem.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

//            // Tanggal Dibuat & Diperbarui
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                DateInfo(label = "Dibuat", date = reportItem.createdAt)
//                DateInfo(label = "Diperbarui", date = reportItem.updatedAt)
//            }
        }
    }
}

// Komponen untuk Menampilkan Detail dengan Ikon
@Composable
fun DetailRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
//        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}

// Komponen untuk Menampilkan Tanggal dengan Ikon
@Composable
fun DateInfo(label: String, date: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.titleSmall)
            Text(text = date, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = Color.Gray)
        }
    }
}

fun formatDate(input: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))

    val date = inputFormat.parse(input)
    return outputFormat.format(date!!)
}
