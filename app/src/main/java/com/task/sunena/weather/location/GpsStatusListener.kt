package com.task.sunena.weather.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GpsStatusListener @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /**
     * Flow that emits the GPS status (true for enabled, false for disabled)
     * and continues to emit updates whenever the status changes.
     */
    val gpsStatus: Flow<Boolean> = callbackFlow {

        trySend(isGpsEnabled())

        // Create a BroadcastReceiver to listen for provider changes
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
                    // When the provider status changes, send the new status
                    trySend(isGpsEnabled())
                }
            }
        }

        // Register the receiver
        context.registerReceiver(receiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))

        // When the flow is cancelled, unregister the receiver
        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }

    private fun isGpsEnabled(): Boolean {
        return try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }
}
