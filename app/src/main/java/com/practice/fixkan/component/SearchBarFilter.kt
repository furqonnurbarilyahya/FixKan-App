package com.practice.fixkan.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practice.fixkan.MainViewModelFactory
import com.practice.fixkan.R
import com.practice.fixkan.data.remote.repository.MainRepository
import com.practice.fixkan.di.Injection
import com.practice.fixkan.screen.createReport.ReportViewModel
import com.practice.fixkan.screen.listReport.ListReportViewModel

@Composable
fun SearchWithFilter(
    context: Context,
    repository: MainRepository,
    listReportViewModel: ListReportViewModel = viewModel(
        factory = MainViewModelFactory(
            Injection.provideMainRepository(
                context
            )
        )
    ),
    onSearchQuery: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilter by remember { mutableStateOf(false) }


    var typeReport by remember { mutableStateOf<String?>(null) }
//    var sortBy by remember { mutableStateOf("") }
    var orderBy by remember { mutableStateOf("DESC") }

    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var selectedRegency by remember { mutableStateOf<String?>(null) }
    var selectedDistrict by remember { mutableStateOf<String?>(null) }
    var selectedVillage by remember { mutableStateOf<String?>(null) }

    val reportViewModel: ReportViewModel = viewModel(factory = MainViewModelFactory(repository))

    val provinces by reportViewModel.provinces.collectAsState()
    val regencies by reportViewModel.regencies.collectAsState()
    val districts by reportViewModel.districts.collectAsState()
    val villages by reportViewModel.villages.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search")
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQuery(it)
                },
                placeholder = { Text("Cari Tipe Laporan") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            IconButton(onClick = { showFilter = !showFilter }) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_filter_list_24),
                    contentDescription = "Filter"
                )
            }
        }

        // Dropdown Filter
        DropdownMenu(
            expanded = showFilter,
            onDismissRequest = { showFilter = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                ReportTypeDropdown(
                    typeReport = typeReport ?: "",
                    onTypeSelected = { typeReport = it }
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


                // Sorting
                OrderByDropdown(orderBy = orderBy, onTypeSelected = { orderBy = it })

                Spacer(Modifier.height(12.dp))
                // Tombol Aksi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            Log.d(
                                "FilterInput",
                                "typeReport: $typeReport, province: $selectedProvince, district: $selectedRegency, subdistrict: $selectedDistrict, village: $selectedVillage, orderBy: $orderBy"
                            )
                            listReportViewModel.getListReport(
                                typeReport = typeReport,
                                province = selectedProvince,
                                district = selectedRegency,
                                subdistrict = selectedDistrict,
                                village = selectedVillage,
                                orderBy = orderBy
                            )
                            showFilter = false
                        }
                    ) {
                        Text("Telusuri")
                    }
                }
            }
        }
    }
}