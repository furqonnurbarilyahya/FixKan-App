package com.practice.fixkan.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.practice.fixkan.R
import com.practice.fixkan.ui.theme.FixKanTheme

@Composable
fun LoadingDialog() {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        containerColor = Color.White,
        text = {
            Column (
                modifier = Modifier.padding(16.dp).background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Image(
                    painter = rememberAsyncImagePainter("https://futureparty.com/wp-content/uploads/2024/07/robot_thinking_july_29.gif"),
                    contentDescription = "Gif loading",
                    modifier = Modifier.height(100.dp)
                )
                Spacer(Modifier.height(16.dp))
                LoadingAnimation()
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "AI sedang memproses… Ini hanya butuh beberapa detik"
                )
            }
        }
    )
}

@Composable
fun LoadingAnimation() {
    val infiniteTransitin = rememberInfiniteTransition()
    val progress by infiniteTransitin.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview (showBackground = true)
@Composable
private fun LoadingDialogPreview() {
    FixKanTheme {
        LoadingDialog()
    }
}