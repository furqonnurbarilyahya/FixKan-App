package com.practice.fixkan.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
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
    onAddressFound: (String) -> Unit
) {
    val geocoder = Geocoder(context, Locale.getDefault())

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
            override fun onGeocode(addresses: MutableList<Address>) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val result = "${address.adminArea}, ${address.subAdminArea}, ${address.locality}, ${address.subLocality}"
                    Log.d("LocationDebug", "Lat: $latitude, Lon: $longitude, Address: $result")
                    onAddressFound(result)
                } else {
                    Log.d("LocationDebug", "Lat: $latitude, Lon: $longitude, Address: Not Found")
                    onAddressFound("Alamat tidak ditemukan")
                }
            }

            override fun onError(errorMessage: String?) {
                Log.e("LocationDebug", "Geocoder error: $errorMessage")
                onAddressFound("Gagal mendapatkan alamat")
            }
        })
    } else {
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val result = "${address.adminArea}, ${address.subAdminArea}, ${address.locality}, ${address.subLocality}"
                Log.d("LocationDebug", "Lat: $latitude, Lon: $longitude, Address: $result")
                onAddressFound(result)
            } else {
                Log.d("LocationDebug", "Lat: $latitude, Lon: $longitude, Address: Not Found")
                onAddressFound("Alamat tidak ditemukan")
            }
        } catch (e: IOException) {
            Log.e("LocationDebug", "Gagal mendapatkan alamat: ${e.message}")
            onAddressFound("Gagal mendapatkan alamat")
        }
    }
}

fun getCurrentLocation(context: Context, onLocationRetrieved: (Double?, Double?, String?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        Log.e("LocationDebug", "Izin lokasi tidak diberikan")
        onLocationRetrieved(null, null, null)
        return
    }

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setWaitForAccurateLocation(true)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                Log.d("LocationDebug", "Lokasi ditemukan: Lat ${it.latitude}, Lon ${it.longitude}")
                getAddressFromLocation(it.latitude, it.longitude, context) { address ->
                    onLocationRetrieved(it.latitude, it.longitude, address)
                }

                fusedLocationClient.removeLocationUpdates(this)

            } ?: run {
                Log.e("LocationDebug", "Gagal mendapatkan lokasi dari request update")
                onLocationRetrieved(null, null, null)
            }
        }
    }

    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
}
