package com.practice.fixkan.screen.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practice.fixkan.component.TopBar
import com.practice.fixkan.ui.theme.FixKanTheme

@Composable
fun TipsAScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = "Cara Membuat Laporan", navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Untuk memastikan laporan yang Anda kirimkan akurat dan dapat ditindaklanjuti oleh pihak terkait, ikuti langkah-langkah berikut:",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify
            )
            TipsItem("1.","Unggah Foto", "Setelah berhasil login, aplikasi akan secara otomatis mengunduh data tambahan untuk model AI. Proses ini harus diselesaikan 100% sebelum AI dapat digunakan untuk mengklasifikasikan laporan. Kemudian pengguna bisa mengunggah foto laporan dari galeri atau melalui kamera.")
            TipsItem("2.","Analisis AI", "Ketika pengguna klik tombol \"Analisis Foto\" AI akan mengklasifikasikan jenis laporan berdasarkan gambar yang Anda unggah.")
            TipsItem("3.","Verifikasi dan Koreksi", "Jika AI salah mengidentifikasi jenis laporan, Anda dapat mengubah tipe laporan yang sesuai secara manual di halaman unggah laporan dengan cara memilih opsi tipe laporan yang benar sesuai dengan foto.")
            TipsItem("4.","Sertakan Latitude dan Longitude (Opsional)", "Jika izin akses lokasi pada aplikasi diberikan, Anda juga bisa menambahkan koordinat (latitude dan longitude) dengan cara klik tombol \"Tambah Koordinat\" untuk memperjelas lokasi secara lebih akurat.")
            TipsItem("5.","Isi Detail Lokasi", "Tambahkan alamat lokasi tempat foto diambil agar petugas dapat dengan mudah menemukan lokasi masalah.")
            TipsItem("6.", "Konfirmasi dan Kirim Laporan", "- Pastikan semua data yang diinput sudah benar.\n" +
                    "- Klik tombol \"Submit\" untuk mengirim laporan Anda.\n" +
                    "- Laporan yang Anda kirim akan diteruskan kepada pihak terkait untuk divalidasi dan ditindaklanjuti.")
        }
    }
}

@Composable
fun TipsItem(number: String, title: String, description: String) {
    Spacer(Modifier.height(8.dp))
    Row (
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify
            )
        }
    }
}


@Preview(showSystemUi = true, showBackground = true, device = Devices.PIXEL_3_XL)
@Composable
private fun Tips1Preview() {
    FixKanTheme {
        TipsAScreen(navController = rememberNavController())
    }
}