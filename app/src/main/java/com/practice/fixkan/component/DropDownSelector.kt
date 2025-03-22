package com.practice.fixkan.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EditableDropdownSelectorFL(
    label: String,
//    previousForm: String,
    enabled: Boolean,
    options: List<String>,  // Data provinsi dari API-Wilayah Indonesia
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(selectedOption ?: "") }
    var filteredOptions by remember { mutableStateOf(options) }
    var lastInputTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isValidInput by remember { mutableStateOf(true) }

    Column {
        OutlinedTextField(
            value = textValue,
            onValueChange = { input ->
                textValue = input.uppercase()
                lastInputTime = System.currentTimeMillis()
                expanded = false // Sembunyikan dropdown saat user mengetik
            },
            label = { Text(label) },
            isError = !isValidInput,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            placeholder = {
                Text(
                    text = "Ketikkan nama $label dengan benar",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )

        // Tampilkan pesan error jika input tidak valid
        if (!isValidInput) {
            Text(
                text = "Nama $label salah atau tidak ditemukan!",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        // Efek debounce untuk validasi input setelah berhenti mengetik
        LaunchedEffect(textValue) {
            kotlinx.coroutines.delay(1000) // Tunggu 1.5 detik sebelum validasi
            if (System.currentTimeMillis() - lastInputTime >= 1000) {
                filteredOptions = if (textValue.isNotEmpty()) {
                    options.filter { it.contains(textValue, ignoreCase = true) }
                } else {
                    options
                }

                // **Perbaikan utama: Null atau input kosong dianggap valid**
                isValidInput = textValue.isBlank() || options.contains(textValue)

                // Panggil onOptionSelected agar tetap bisa diproses
                onOptionSelected(textValue.ifBlank { null })

                expanded = filteredOptions.isNotEmpty()
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        textValue = option
                        isValidInput = true // Set valid jika dipilih dari dropdown
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
fun EditableDropdownSelectorProvinceFL(
    label: String,
    options: List<String>,  // Data provinsi dari API-Wilayah Indonesia
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    onUserStartsTyping: () -> Unit // Tambahkan callback untuk trigger fetchProvinces()
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(selectedOption ?: "") }
    var filteredOptions by remember { mutableStateOf(options) }
    var lastInputTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isValidInput by remember { mutableStateOf(true) }
    var hasUserStartedTyping by remember { mutableStateOf(false) } // Deteksi awal input

    LaunchedEffect(selectedOption) {
        textValue = selectedOption ?: ""
        isValidInput = textValue.isBlank() || options.contains(textValue)
    }

    Column {
        OutlinedTextField(
            value = textValue,
            onValueChange = { input ->
                textValue = input.uppercase()
                lastInputTime = System.currentTimeMillis()
                expanded = false // Sembunyikan dropdown saat user mengetik

                // Jika user baru pertama kali mengetik setelah kosong, panggil fetchProvinces()
                if (!hasUserStartedTyping && input.isNotBlank()) {
                    hasUserStartedTyping = true
                    onUserStartsTyping() // Trigger API fetch
                }
            },
            label = { Text(label) },
            isError = !isValidInput,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            placeholder = {
                Text(
                    text = "Ketikkan nama $label dengan benar",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Tampilkan pesan error jika input tidak valid
        if (!isValidInput) {
            Text(
                text = "Nama $label salah atau tidak ditemukan!",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        // Efek debounce untuk validasi input setelah berhenti mengetik
        LaunchedEffect(textValue) {
            kotlinx.coroutines.delay(1000) // Tunggu 1.5 detik sebelum validasi
            if (System.currentTimeMillis() - lastInputTime >= 1000) {
                filteredOptions = if (textValue.isNotEmpty()) {
                    options.filter { it.contains(textValue, ignoreCase = true) }
                } else {
                    options
                }

                // **Perbaikan utama: Null atau input kosong dianggap valid**
                isValidInput = textValue.isBlank() || options.contains(textValue)

                // Panggil onOptionSelected agar tetap bisa diproses
                onOptionSelected(textValue.ifBlank { null })

                expanded = filteredOptions.isNotEmpty()
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        textValue = option
                        isValidInput = true // Set valid jika dipilih dari dropdown
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
fun EditableDropdownSelector(
    label: String,
    previousForm: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(selectedOption) }
    var filteredOptions by remember { mutableStateOf(options) }
    var lastInputTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    Column {
        OutlinedTextField(
            value = textValue,
            onValueChange = { input ->
                textValue = input
                lastInputTime = System.currentTimeMillis()

                // **Jangan langsung update dropdown saat user mengetik**
                if (input.isNotEmpty()) {
                    filteredOptions = options.filter { it.contains(input, ignoreCase = true) }
                } else {
                    filteredOptions = options
                    expanded = false // **Tutup dropdown jika input kosong**
                }
            },
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            placeholder = { Text(
                text = "Pilih $previousForm dengan benar agar opsi muncul.",
                fontSize = 14.sp,
                color = Color.Red
            ) },
            modifier = Modifier.fillMaxWidth()
        )

        // **Tunggu sampai user berhenti mengetik selama 2s baru buka dropdown**
        LaunchedEffect(textValue) {
            kotlinx.coroutines.delay(500) // Jeda 2s
            if (System.currentTimeMillis() - lastInputTime >= 500) {
                expanded = textValue.isNotEmpty() && filteredOptions.isNotEmpty()
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        textValue = option
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
fun EditableDropdownSelectorProvince(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(selectedOption) }
    var filteredOptions by remember { mutableStateOf(options) }
    var lastInputTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    Column {
        OutlinedTextField(
            value = textValue,
            onValueChange = { input ->
                textValue = input
                lastInputTime = System.currentTimeMillis()

                // **Jangan langsung update dropdown saat user mengetik**
                if (input.isNotEmpty()) {
                    filteredOptions = options.filter { it.contains(input, ignoreCase = true) }
                } else {
                    filteredOptions = options
                    expanded = false // **Tutup dropdown jika input kosong**
                }
            },
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            placeholder = { Text(
                text = "Isi $label dengan benar agar opsi muncul.",
                fontSize = 14.sp,
                color = Color.Red
            ) },
            modifier = Modifier.fillMaxWidth()
        )

        // **Tunggu sampai user berhenti mengetik selama 2s baru buka dropdown**
        LaunchedEffect(textValue) {
            kotlinx.coroutines.delay(500) // Jeda 2s
            if (System.currentTimeMillis() - lastInputTime >= 500) {
                expanded = textValue.isNotEmpty() && filteredOptions.isNotEmpty()
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        textValue = option
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
fun ReportTypeDropdown(typeReport: String, onTypeSelected: (String) -> Unit) {
    val reportOptions = listOf(
        "",
        "Jalan Rusak",
        "Sampah Berserakan",
        "Bangunan Retak",
        "Bangunan Roboh",
        "Jembatan Rusak"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(typeReport) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Dropdown Button dengan Border
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text("Pilih Jenis Laporan") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true, // Agar tidak bisa diketik manual
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            reportOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        onTypeSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun OrderByDropdown(orderBy: String, onTypeSelected: (String) -> Unit) {
    val reportOptions = listOf("Terlama" to "ASC", "Terbaru" to "DESC")
    var expanded by remember { mutableStateOf(false) }

    // Mencari label UI berdasarkan nilai orderBy yang diberikan
    var selectedOption by remember {
        mutableStateOf(reportOptions.firstOrNull { it.second == orderBy }?.first ?: "Pilih Urutan")
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Dropdown Button dengan Border
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { },
            label = { Text("Urut Berdasarkan") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true, // Agar tidak bisa diketik manual
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            reportOptions.forEach { (uiLabel, label) ->
                DropdownMenuItem(
                    text = { Text(uiLabel) },
                    onClick = {
                        selectedOption = uiLabel
                        onTypeSelected(label)
                        expanded = false
                    }
                )
            }
        }
    }
}


