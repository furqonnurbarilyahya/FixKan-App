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
import androidx.compose.ui.text.font.FontWeight
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
fun TipsCScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = "Hal yang harus dihindari", navController = navController)
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
                text = "Agar aplikasi dapat digunakan dengan maksimal dan laporan yang dikirimkan bermanfaat, hindari beberapa hal berikut:\n",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify
            )
            TipsItem("1.","Menutup Aplikasi Saat Model AI Sedang Diunduh", "~ Pastikan Anda tidak menutup aplikasi saat sistem sedang mengunduh model AI agar tidak terjadi kegagalan dalam proses pengunduhan.")
            TipsItem("2.","Mengunggah Foto yang Tidak Relevan", "~ Pastikan foto yang diunggah benar-benar menggambarkan masalah infrastruktur atau lingkungan.\n" +
                    "~ Jangan mengunggah foto yang buram atau tidak jelas.")
            TipsItem("3.","Memasukkan Alamat yang Tidak Akurat", "~ Pastikan alamat lokasi sesuai dengan lokasi masalah yang dilaporkan.\n" +
                    "~ Hindari memasukkan alamat secara asal, karena hal ini dapat menghambat tindak lanjut laporan.")
            TipsItem("4.","Memanipulasi Informasi Laporan", "~ Jangan mengubah atau memberikan informasi yang tidak sesuai dengan kondisi sebenarnya.\n" +
                    "~ Laporan yang tidak valid dapat menghambat perbaikan dan mengurangi efektivitas aplikasi.")
            TipsItem("5.", "Konfirmasi dan Kirim Laporan", "- Pastikan semua data yang diinput sudah benar.\n" +
                    "- Klik tombol \"Submit\" untuk mengirim laporan Anda.\n" +
                    "- Laporan yang Anda kirim akan diteruskan kepada pihak terkait untuk divalidasi dan ditindaklanjuti.")


            Spacer(Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Dengan menghindari kesalahan-kesalahan ini, Anda dapat memastikan bahwa laporan yang dikirimkan valid dan bermanfaat bagi pihak yang bertanggung jawab.",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start
            )
        }
    }
}


@Preview(showSystemUi = true, showBackground = true, device = Devices.PIXEL_3_XL)
@Composable
private fun Tips2Preview() {
    FixKanTheme {
        TipsCScreen(navController = rememberNavController())
    }
}