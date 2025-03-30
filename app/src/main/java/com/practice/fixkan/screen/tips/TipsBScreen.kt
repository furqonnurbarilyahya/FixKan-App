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
fun TipsBScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = "Solusi \"AI belum siap\"", navController = navController)
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
                text = "Agar aplikasi dapat digunakan dengan maksimal dan laporan yang dikirimkan bermanfaat, hindari beberapa hal berikut:",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Apa yang harus dilakukan?",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                textAlign = TextAlign.Start
            )
            TipsBItem("1.","Tunggu hingga proses unduhan selesai 100%.")
            TipsBItem("2.","Jangan menutup aplikasi selama proses berlangsung agar tidak terjadi kegagalan dalam pengunduhan.")
            TipsBItem("3.","Setelah model AI siap digunakan, Anda dapat mulai membuat laporan seperti biasa.")
            TipsBItem("4.","Jika terjadi kendala saat pengunduhan, pastikan koneksi internet stabil dan coba kembali dengan cara hapus cache/data aplikasi, kemudian buka kembali aplikasi.")

            Spacer(Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Dengan menyelesaikan proses diatas, model AI akan berfungsi dengan optimal dalam membantu analisis laporan Anda.",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun TipsBItem(number: String, description: String) {
    Spacer(Modifier.height(8.dp))
    Row (
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = number,
            fontSize = 16.sp
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = Devices.PIXEL_3A_XL)
@Composable
private fun Tips2Preview() {
    FixKanTheme {
        TipsBScreen(navController = rememberNavController())
    }
}