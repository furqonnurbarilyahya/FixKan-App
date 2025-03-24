package com.practice.fixkan.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.practice.fixkan.R

//@Composable
//fun RegisterScreen(navController: NavController? = null) {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isPasswordVisible by remember { mutableStateOf(false) }
//    var isEmailValid by remember { mutableStateOf(true) }
//    var province by remember { mutableStateOf("") }
//    var city by remember { mutableStateOf("") }
//    var district by remember { mutableStateOf("") }
//    var village by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Register",
//            style = MaterialTheme.typography.headlineMedium,
//            color = Color(0xFF276561),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Nama") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
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
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(value = province, onValueChange = { province = it }, label = { Text("Provinsi") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Kabupaten/Kota") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(value = district, onValueChange = { district = it }, label = { Text("Kecamatan") }, modifier = Modifier.fillMaxWidth())
//        Spacer(modifier = Modifier.height(8.dp))
//        OutlinedTextField(value = village, onValueChange = { village = it }, label = { Text("Desa") }, modifier = Modifier.fillMaxWidth())
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = { navController?.navigate("login") },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF276561))
//        ) {
//            Text("Daftar", color = Color.White)
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        TextButton(onClick = { navController?.navigate("login") }) {
//            Text("Sudah punya akun? Login", color = Color(0xFF276561))
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewRegisterScreen() {
//    RegisterScreen()
//}

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var province by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7FFF2))
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Buat Akun",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF276561)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    label = { Text("Email") },
                    isError = !isEmailValid,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
                )
                if (!isEmailValid) {
                    Text("Format email tidak valid!", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(id = R.drawable.baseline_visibility_off_24),
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    }
                )

                listOf("Provinsi" to province, "Kabupaten/Kota" to city, "Kecamatan" to district, "Desa" to village)
                    .forEach { (label, value) ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue ->
                                when (label) {
                                    "Provinsi" -> province = newValue
                                    "Kabupaten/Kota" -> city = newValue
                                    "Kecamatan" -> district = newValue
                                    "Desa" -> village = newValue
                                }
                            },
                            label = { Text(label) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF276561)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Daftar", color = Color.White, fontSize = 18.sp)
                }

                TextButton(onClick = { navController?.navigate("login") }) {
                    Text("Sudah punya akun? Login", color = Color(0xFF276561))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(navController = rememberNavController())
}

