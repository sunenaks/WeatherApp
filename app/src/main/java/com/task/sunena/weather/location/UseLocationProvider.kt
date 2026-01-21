package com.task.sunena.weather.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import javax.inject.Inject

class UseLocationProvider @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {


    @SuppressLint("MissingPermission") // Permissions should be handled at the call site
    fun getCurrentLocation(onSuccess: (Location) -> Unit, onFailure: () -> Unit) {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onSuccess(location)
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }
}