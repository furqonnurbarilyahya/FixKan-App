package com.practice.fixkan.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practice.fixkan.R

//@Composable
//fun LoginScreen(navController: NavController? = null) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isPasswordVisible by remember { mutableStateOf(false) }
//    var isEmailValid by remember { mutableStateOf(true) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Login",
//            style = MaterialTheme.typography.headlineMedium,
//            color = Color(0xFF276561),
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = {
//                email = it
//                isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
//            },
//            label = { Text("Email") },
//            isError = !isEmailValid,
//            modifier = Modifier.fillMaxWidth(),
//            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
//        )
//        if (!isEmailValid) {
//            Text("Format email tidak valid!", color = Color.Red, style = MaterialTheme.typography.bodySmall)
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                    Icon(
//                        painter = if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(id = R.drawable.baseline_visibility_off_24),
//                        contentDescription = "Toggle Password Visibility"
//                    )
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = { navController?.navigate("home") },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF276561))
//        ) {
//            Text("Login", color = Color.White)
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextButton(onClick = { navController?.navigate("register") }) {
//            Text("Belum punya akun? Daftar", color = Color(0xFF276561))
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
//@Composable
//fun PreviewLoginScreen() {
//    LoginScreen()
//}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }

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
                    Text("Format email tidak valid!", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Password")
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(id = R.drawable.baseline_visibility_off_24),
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF276561)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Masuk", color = Color.White, fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { navController?.navigate("register") }) {
                    Text("Belum punya akun? Daftar", color = Color(0xFF276561))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
@Composable
fun PreviewModernLoginScreen() {
    LoginScreen(navController = rememberNavController())
}