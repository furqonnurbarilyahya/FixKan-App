package com.practice.fixkan.screen.register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.practice.fixkan.AuthViewModel
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.R
import com.practice.fixkan.component.EditableDropdownSelectorFL
import com.practice.fixkan.component.EditableDropdownSelectorProvinceFL
import com.practice.fixkan.component.LoadingAuthDialog
import com.practice.fixkan.component.RegisterFailedDialog
import com.practice.fixkan.component.RegisterSuccessDialog
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.screen.createReport.ReportViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    repository: MainRepository
) {

    val context = LocalContext.current
    val registerState by authViewModel.registerState.collectAsState()


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var selectedRegency by remember { mutableStateOf<String?>(null) }
    var selectedDistrict by remember { mutableStateOf<String?>(null) }
    var selectedVillage by remember { mutableStateOf<String?>(null) }

    val reportViewModel: ReportViewModel = viewModel(factory = MainViewModelFactory(repository))

    val provinces by reportViewModel.provinces.collectAsState()
    val regencies by reportViewModel.regencies.collectAsState()
    val districts by reportViewModel.districts.collectAsState()
    val villages by reportViewModel.villages.collectAsState()

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        registerState?.let { result ->
            launch {
                showLoadingDialog = true
                delay(2000)
                showLoadingDialog = false

                result.onSuccess {
                    showSuccessDialog = true
                    delay(3000)
                    showSuccessDialog = false

                    navController.navigate("login")
                    authViewModel.resetRegisterState()
                }
            }

            launch {
                result.onFailure {
                    showLoadingDialog = true
                    delay(2000)
                    showLoadingDialog = false

                    showErrorDialog = true
                    delay(3000)
                    showErrorDialog = false

                    authViewModel.resetRegisterState()
                }
            }
        }
    }

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
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Format email tidak valid!",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = it.length < 8
                    },
                    label = { Text("Kata Sandi") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

                // Dropdown Provinsi
                EditableDropdownSelectorProvinceFL(
                    label = "Provinsi",
                    options = provinces.map { it.name },
                    selectedOption = selectedProvince,
                    onOptionSelected = { selectedName ->
                        val selected = provinces.find { it.name == selectedName }

                        selectedProvince = selected?.name ?: selectedName

                        if (selected != null) {
                            selectedRegency = null
                            selectedDistrict = null
                            selectedVillage = null
                            reportViewModel.fetchRegencies(selected.id) // Ambil kabupaten/kota berdasarkan provinsi
                        }
                    },
                    onUserStartsTyping = { reportViewModel.fetchProvinces() } // Hanya fetch saat user mulai mengetik
                )

                // Dropdown Kabupaten/Kota (Aktif hanya jika Provinsi valid)
                EditableDropdownSelectorFL(
                    label = "Kabupaten/Kota",
                    options = if (selectedProvince != null && provinces.any { it.name == selectedProvince }) regencies.map { it.name } else emptyList(),
                    selectedOption = selectedRegency,
                    onOptionSelected = { selectedName ->
                        val selected = regencies.find { it.name == selectedName }
                        selectedRegency = selected?.name ?: selectedName?.ifBlank { null }
                        selectedDistrict = null
                        selectedVillage = null
                        selected?.let { reportViewModel.fetchDistricts(it.id) }
                    },
                    enabled = selectedProvince != null && provinces.any { it.name == selectedProvince }
                )

                // Dropdown Kecamatan (Aktif hanya jika Kabupaten valid)
                EditableDropdownSelectorFL(
                    label = "Kecamatan",
                    options = if (selectedRegency != null && regencies.any { it.name == selectedRegency }) districts.map { it.name } else emptyList(),
                    selectedOption = selectedDistrict,
                    onOptionSelected = { selectedName ->
                        selectedDistrict = selectedName?.ifBlank { null }
                        selectedVillage = null
                        districts.find { it.name == selectedName }
                            ?.let { reportViewModel.fetchVillages(it.id) }
                    },
                    enabled = selectedRegency != null && regencies.any { it.name == selectedRegency }
                )

                // Dropdown Kelurahan/Desa (Aktif hanya jika Kecamatan valid)
                EditableDropdownSelectorFL(
                    label = "Kelurahan/Desa",
                    options = if (selectedDistrict != null && districts.any { it.name == selectedDistrict }) villages.map { it.name } else emptyList(),
                    selectedOption = selectedVillage,
                    onOptionSelected = { selectedName ->
                        selectedVillage = selectedName?.ifBlank { null }
                    },
                    enabled = selectedDistrict != null && districts.any { it.name == selectedDistrict }
                )

                Button(
                    onClick = {
                        if (
                            name.isEmpty() ||
                            email.isEmpty() ||
                            password.isEmpty() ||
                            selectedProvince.isNullOrEmpty() ||
                            selectedRegency.isNullOrEmpty() ||
                            selectedDistrict.isNullOrEmpty() ||
                            selectedVillage.isNullOrEmpty()
                        ) {
                            Toast.makeText(
                                context,
                                "Pastikan semua form diisi dengan benar",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else if (!isEmailValid) {
                            Toast.makeText(context, "Email tidak valid", Toast.LENGTH_SHORT)
                                .show()
                        } else if (passwordError) {
                            Toast.makeText(context, "Kata sandi tidak valid", Toast.LENGTH_SHORT)
                                .show()
                        } else if (!provinces.any { it.name == selectedProvince } ||
                            !regencies.any { it.name == selectedRegency } ||
                            !districts.any { it.name == selectedDistrict } ||
                            !villages.any { it.name == selectedVillage }) {

                            val errorMessage = when {
                                !provinces.any { it.name == selectedProvince } -> "Nama Provinsi tidak valid!"
                                !regencies.any { it.name == selectedRegency } -> "Nama Kabupaten tidak valid!"
                                !districts.any { it.name == selectedDistrict } -> "Nama Kecamatan tidak valid!"
                                !villages.any { it.name == selectedVillage } -> "Nama Desa tidak valid!"
                                else -> null
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        } else {
                            Log.d(
                                "RegisterInput",
                                "Name: $name, Email: $email, Password: $password, Province: $selectedProvince, City: $selectedRegency, District: $selectedDistrict, Village: $selectedVillage"
                            )
                            authViewModel.register(
                                name = name,
                                email = email,
                                password = password,
                                province = selectedProvince!!,
                                district = selectedRegency!!,
                                subdistrict = selectedDistrict!!,
                                village = selectedVillage!!
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF276561)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Daftar", color = Color.White, fontSize = 18.sp)
                }

                TextButton(onClick = { navController.navigate("login") }) {
                    Text("Sudah punya akun? Login", color = Color(0xFF276561))
                }
            }
        }
        if (showSuccessDialog) {
            RegisterSuccessDialog(onDismiss = { showSuccessDialog = false }, "Silahkan login")
        }
        if (showErrorDialog) {
            RegisterFailedDialog(onDismiss = { showSuccessDialog = false }, "Coba lagi")
        }
        if (showLoadingDialog) {
            LoadingAuthDialog()
            LaunchedEffect(Unit) {
                delay(3000)
                showLoadingDialog = false
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A_XL)
@Composable
fun PreviewRegisterScreen() {
//    RegisterScreen(navController = rememberNavController())
}

