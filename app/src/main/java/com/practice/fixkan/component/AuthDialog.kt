package com.practice.fixkan.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.practice.fixkan.ui.theme.FixKanTheme
import kotlinx.coroutines.delay

@Composable
fun AuthErrorDialog(onDismiss: () -> Unit, message: String) {
    CustomAlertDialog(
        title = "Login Gagal",
        message = message,
        icon = Icons.Rounded.Close,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        iconColor = MaterialTheme.colorScheme.error,
        onDismiss = onDismiss
    )
}

@Composable
fun AuthSuccessDialog(onDismiss: () -> Unit, message: String) {
    CustomAlertDialog(
        title = "Login Berhasil",
        message = message,
        icon = Icons.Rounded.CheckCircle,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        iconColor = MaterialTheme.colorScheme.primary,
        onDismiss = onDismiss
    )
}

@Composable
fun RegisterSuccessDialog(onDismiss: () -> Unit, message: String) {
    CustomAlertDialog(
        title = "Pendaftaran Berhasil",
        message = message,
        icon = Icons.Rounded.Person,
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        iconColor = MaterialTheme.colorScheme.secondary,
        onDismiss = onDismiss
    )
}

@Composable
fun RegisterFailedDialog(onDismiss: () -> Unit, message: String) {
    CustomAlertDialog(
        title = "Pendaftaran Berhasil",
        message = message,
        icon = Icons.Rounded.Person,
        backgroundColor = MaterialTheme.colorScheme.errorContainer,
        iconColor = MaterialTheme.colorScheme.error,
        onDismiss = onDismiss
    )
}

@Composable
fun LoadingAuthDialog() {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        title = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Sedang memproses...", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
        }
    )
}

@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(16.dp),
        properties = DialogProperties(dismissOnClickOutside = true),
        containerColor = backgroundColor,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {}
    )
    LaunchedEffect(Unit) {
        delay(3000)
        onDismiss()
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthDiaologPreview() {
    FixKanTheme {
        AuthSuccessDialog(onDismiss = {},message = "Selamat datang")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthDiaologPreview1() {
    FixKanTheme {
        AuthErrorDialog(onDismiss = {}, message = "Email/Kata Sandi salah")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthDiaologPreview2() {
    FixKanTheme {
        RegisterSuccessDialog(onDismiss = {},message = "Akun berhasil dibuat")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthDiaologPreview3() {
    FixKanTheme {
        RegisterFailedDialog(onDismiss = {},message = "Coba lagi")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingDiaologPreview3() {
    FixKanTheme {
        LoadingAuthDialog()
    }
}
