@file:Suppress("DEPRECATION")

package com.practice.fixkan.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.practice.fixkan.component.TopBar

@Composable
fun ResultClassificationScreen(imageUri: String?, result: String) {

    val context = LocalContext.current

    val bitmap = remember(imageUri) {
        imageUri?.let { uriStr ->
            try {
                val uri = Uri.parse(uriStr)
                Log.d("ResultScreen", "Final Parsed Uri: $uri")
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            } catch (e: Exception) {
                Log.e("ResultScreen", "Error decoding image: ${e.message}")
                null
            }
        }
    }

    Scaffold (
        topBar = {
            TopBar(title = "Hasil Klasifikasi")
        }
    ) {
        Column(
            Modifier.fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bitmap?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Classified Image",
                    modifier = Modifier.size(400.dp)
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = result,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )
        }
    }
}