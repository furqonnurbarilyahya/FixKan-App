package com.practice.fixkan.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practice.fixkan.ui.theme.FixKanTheme

@Composable
fun SuccessDialog() {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.fillMaxWidth()
                        .size(40.dp),
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Berhasil",
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Laporan Berhasil Dikirim", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Text("Laporan Anda telah berhasil dibuat.")
//                Spacer(modifier = Modifier.height(8.dp))
                Text("Anda akan dialihkan ke halaman utama...")
            }
        },
        confirmButton = {}, // Tidak ada tombol karena auto-close dalam 3 detik
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
    )
}

@Preview(showBackground = true)
@Composable
private fun SuccesDialogPreview() {
    FixKanTheme {
        SuccessDialog()
    }
}