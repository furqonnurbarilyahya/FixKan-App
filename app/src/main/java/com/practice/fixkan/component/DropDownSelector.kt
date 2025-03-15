package com.practice.fixkan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController

//@Composable
//fun DropdownSelector(
//    label: String,
//    options: List<String>,
//    selectedOption: String,
//    onOptionSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf(selectedOption) }
//
//    Column {
//        Text(text = label, modifier = Modifier.padding(4.dp))
//        Box(
//            modifier = Modifier
//                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
//                .clickable { expanded = true }
//                .padding(8.dp)
//        ) {
//            Text(text = selectedText.ifEmpty { "Pilih $label" })
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.background(Color.White)
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option) },  // ✅ Perbaikan di sini
//                    onClick = {
//                        selectedText = option
//                        onOptionSelected(option)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}

//@Composable
//fun EditableDropdownSelector(
//    label: String,
//    options: List<String>,
//    selectedOption: String,
//    onOptionSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var textValue by remember { mutableStateOf(selectedOption) }
//    var filteredOptions by remember { mutableStateOf(options) }
//
//    Column {
//        OutlinedTextField(
//            value = textValue,
//            onValueChange = { input ->
//                textValue = input
//                filteredOptions = options.filter { it.contains(input, ignoreCase = true) }
//                expanded = true // Tetap buka dropdown saat mengetik
//            },
//            label = { Text(label) },
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.ArrowDropDown,
//                    contentDescription = "Dropdown Icon",
//                    modifier = Modifier.clickable { expanded = !expanded }
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.background(Color.White)
//        ) {
//            filteredOptions.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option) },
//                    onClick = {
//                        textValue = option
//                        onOptionSelected(option)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}

//@Composable
//fun EditableDropdownSelector(
//    label: String,
//    options: List<String>,
//    selectedOption: String,
//    onOptionSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var textValue by remember { mutableStateOf(selectedOption) }
//    var filteredOptions by remember { mutableStateOf(options) }
//
//    Column {
//        OutlinedTextField(
//            value = textValue,
//            onValueChange = { input ->
//                textValue = input
//                filteredOptions = if (input.isEmpty()) options else options.filter {
//                    it.contains(input, ignoreCase = true)
//                }
//                expanded = input.isNotEmpty() // Tetap buka dropdown saat mengetik, tapi bisa dihapus langsung
//            },
//            label = { Text(label) },
//            trailingIcon = {
//                Row {
//                    if (textValue.isNotEmpty()) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "Clear Text",
//                            modifier = Modifier
//                                .clickable {
//                                    textValue = "" // Mengosongkan teks sekaligus
//                                    filteredOptions = options // Kembalikan opsi awal
//                                    expanded = false
//                                }
//                        )
//                    }
//                    Icon(
//                        imageVector = Icons.Default.ArrowDropDown,
//                        contentDescription = "Dropdown Icon",
//                        modifier = Modifier.clickable { expanded = !expanded }
//                    )
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.background(Color.White)
//        ) {
//            filteredOptions.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option) },
//                    onClick = {
//                        textValue = option
//                        onOptionSelected(option)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}

@Composable
fun EditableDropdownSelector(
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
            modifier = Modifier.fillMaxWidth()
        )

        // **Tunggu sampai user berhenti mengetik selama 2s baru buka dropdown**
        LaunchedEffect(textValue) {
            kotlinx.coroutines.delay(2000) // Jeda 2s
            if (System.currentTimeMillis() - lastInputTime >= 2000) {
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


