package com.practice.fixkan.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.IOException
import java.util.Locale

fun getAddressFromLocation(
    latitude: Double,
    longitude: Double,
    context: Context,
    onAddressFound: (String?, String?, String?, String?) -> Unit // Mengembalikan 4 nilai
) {
    val geocoder = Geocoder(context, Locale("id"))

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
            override fun onGeocode(addresses: MutableList<Address>) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val adminArea = address.adminArea // Provinsi
                    val subAdminArea = address.subAdminArea // Kabupaten/Kota
                    val locality = address.locality // Kota atau Kecamatan
                    val subLocality = address.subLocality // Kelurahan atau Desa

                    Log.d("LocationDebug", """
                        Lat: $latitude, Lon: $longitude
                        Admin Area: $adminArea
                        Sub Admin Area: $subAdminArea
                        Locality: $locality
                        Sub Locality: $subLocality
                    """.trimIndent())

                    onAddressFound(adminArea, subAdminArea, locality, subLocality)
                } else {
                    Log.d("LocationDebug", "Lat: $latitude, Lon: $longitude, Address: Not Found")
                    onAddressFound(null, null, null, null)
                }
            }

            override fun onError(errorMessage: String?) {
                Log.e("LocationDebug", "Geocoder error: $errorMessage")
                onAddressFound(null, null, null, null)
            }
        })
    } else {
        try {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val adminArea = address.adminArea
                val subAdminArea = address.subAdminArea
                val locality = address.locality
                val subLocality = address.subLocality

                Log.d("LocationDebug", """
                    Lat: $latitude, Lon: $longitude
                    Admin Area: $adminArea
                    Sub Admin Area: $subAdminArea
                    Locality: $locality
                    Sub Locality: $subLocality
                """.trimIndent())

                onAddressFound(adminArea, subAdminArea, locality, subLocality)
            } else {
                Log.d("LocationDebug", "Lat: $latitude, Lon: $longitude, Address: Not Found")
                onAddressFound(null, null, null, null)
            }
        } catch (e: IOException) {
            Log.e("LocationDebug", "Gagal mendapatkan alamat: ${e.message}")
            onAddressFound(null, null, null, null)
        }
    }
}


fun getCurrentLocation(context: Context, onLocationRetrieved: (Double?, Double?, String?, String?, String?, String?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        Log.e("LocationDebug", "Izin lokasi tidak diberikan")
        onLocationRetrieved(null, null, null, null, null, null)
        return
    }

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setWaitForAccurateLocation(true)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                getAddressFromLocation(it.latitude, it.longitude, context) { adminArea, subAdminArea, locality, subLocality ->
                    onLocationRetrieved(it.latitude, it.longitude, adminArea, subAdminArea, locality, subLocality)
                }
                fusedLocationClient.removeLocationUpdates(this)
            } ?: run {
                Log.e("LocationDebug", "Gagal mendapatkan lokasi dari request update")
                onLocationRetrieved(null, null, null, null, null, null)
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
}


//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        Log.e("LocationDebug", "Izin lokasi tidak diberikan")
//        onLocationRetrieved(null, null, null)
//        return
//    }
//
//    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
//        .setWaitForAccurateLocation(true)
//        .build()
//
//    val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            locationResult.lastLocation?.let {
//                Log.d("LocationDebug", "Lokasi ditemukan: Lat ${it.latitude}, Lon ${it.longitude}")
//                getAddressFromLocation(it.latitude, it.longitude, context) { address ->
//                    onLocationRetrieved(it.latitude, it.longitude, address)
//                }
//
//                fusedLocationClient.removeLocationUpdates(this)
//
//            } ?: run {
//                Log.e("LocationDebug", "Gagal mendapatkan lokasi dari request update")
//                onLocationRetrieved(null, null, null)
//            }
//        }
//    }
//
//    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//}


//Ini baruu
//fun getCurrentLocation(
//    context: Context,
//    onLocationRetrieved: (
//        Double?, Double?, String?, String?, String?, String?
//    ) -> Unit
//) {
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//        != PackageManager.PERMISSION_GRANTED
//    ) {
//        Log.e("LocationDebug", "Izin lokasi tidak diberikan")
//        onLocationRetrieved(null, null, null, null, null, null)
//        return
//    }
//
//    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
//        .setWaitForAccurateLocation(true)
//        .build()
//
//    val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            locationResult.lastLocation?.let { location ->
//                Log.d("LocationDebug", "Lokasi ditemukan: Lat ${location.latitude}, Lon ${location.longitude}")
//
//                // Panggil geocoder untuk mendapatkan alamat berdasarkan lokasi
//                getAddressFromLocation(location.latitude, location.longitude, context) { address ->
//                    onLocationRetrieved(
//                        location.latitude,
//                        location.longitude,
//                        address?.adminArea ?: "Alamat tidak ditemukan",
//                        address?.subAdminArea ?: "Alamat tidak ditemukan",
//                        address?.locality ?: "Alamat tidak ditemukan",
//                        address?.subLocality ?: "Alamat tidak ditemukan"
//                    )
//                }
//
//                // Hentikan update lokasi setelah mendapatkan data
//                fusedLocationClient.removeLocationUpdates(this)
//            } ?: run {
//                Log.e("LocationDebug", "Gagal mendapatkan lokasi dari request update")
//                onLocationRetrieved(null, null, null, null, null, null)
//            }
//        }
//    }
//
//    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//}


//fun getCurrentLocation(context: Context, onLocationRetrieved: (Double?, Double?, String?, String?, String?, String?) -> Unit) {
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        Log.e("LocationDebug", "Izin lokasi tidak diberikan")
//        onLocationRetrieved(null, null, null, null, null, null)
//        return
//    }
//
//    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
//        .setWaitForAccurateLocation(true)
//        .build()
//
//    val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            locationResult.lastLocation?.let {
//                getAddressFromLocation(it.latitude, it.longitude, context) { adminArea, subAdminArea, locality, subLocality ->
//                    onLocationRetrieved(it.latitude, it.longitude, adminArea, subAdminArea, locality, subLocality)
//                }
//                fusedLocationClient.removeLocationUpdates(this)
//            } ?: run {
//                Log.e("LocationDebug", "Gagal mendapatkan lokasi dari request update")
//                onLocationRetrieved(null, null, null, null, null, null)
//            }
//        }
//    }
//
//    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
//}
//
//
