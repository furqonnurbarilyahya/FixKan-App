package com.practice.fixkan.screen.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practice.fixkan.AuthViewModel
import com.practice.fixkan.MainActivity
import com.practice.fixkan.R
import com.practice.fixkan.component.AuthErrorDialog
import com.practice.fixkan.component.AuthSuccessDialog
import com.practice.fixkan.component.LoadingAuthDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var passwordError by remember { mutableStateOf(false) }
    val loginState by authViewModel.loginState.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        loginState?.let { result ->
            launch {
                showLoadingDialog = true
                delay(2000)
                showLoadingDialog = false

                result.onSuccess {
                    showSuccessDialog = true
                    delay(3000)
                    showSuccessDialog = false

                    // Pindah ke MainActivity hanya jika login berhasil
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)

                    authViewModel.resetLoginState() // Reset state setelah login sukses
                }
            }

            launch {
                showLoadingDialog = true
                delay(2000)
                showLoadingDialog = false

                result.onFailure {
                    showErrorDialog = true
                    delay(3000)
                    showErrorDialog = false

                    authViewModel.resetLoginState() // Reset state setelah login gagal
                }
            }
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7FFF2)), // Background hijau muda yang soft
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Masuk",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF276561)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    label = { Text("Email") },
                    isError = !isEmailValid,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Email, contentDescription = "Email")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )
                if (!isEmailValid) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Format email tidak valid!",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = it.length < 8
                    },
                    label = { Text("Kata Sandi") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24)
                                else painterResource(id = R.drawable.baseline_visibility_off_24),
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    isError = passwordError,
                    supportingText = {
                        if (passwordError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Kata sandi minimal terdiri dari 8 karakter",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (
                            email.isEmpty() ||
                            password.isEmpty()
                        ) {
                            Toast.makeText(context, "Semua form harus diisi", Toast.LENGTH_SHORT)
                                .show()
                        } else if (!isEmailValid) {
                            Toast.makeText(context, "Email tidak valid", Toast.LENGTH_SHORT)
                                .show()
                        } else if (passwordError) {
                            Toast.makeText(context, "Kata sandi tidak valid", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            authViewModel.login(
                                email = email,
                                password = password
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF276561)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Masuk", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Belum punya akun? Daftar", color = Color(0xFF276561))
                }
            }
        }
        if (showSuccessDialog) {
            AuthSuccessDialog(onDismiss = { showSuccessDialog = false },"Selamat datang!")
        }
        if (showErrorDialog) {
            AuthErrorDialog(onDismiss = { showErrorDialog = false },"Email/Kata sandi salah")
        }
        if (showLoadingDialog) {
            LoadingAuthDialog()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
@Composable
fun PreviewModernLoginScreen() {
//    LoginScreen(navController = rememberNavController())
}