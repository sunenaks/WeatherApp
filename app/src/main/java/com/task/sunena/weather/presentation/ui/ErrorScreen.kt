package com.task.sunena.weather.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.task.sunena.weather.presentation.navigation.AppNavRoute
import com.task.sunena.weather.presentation.state.WeatherUiState
import com.task.sunena.weather.presentation.viewmodel.WeatherViewModel

@Composable
fun ErrorScreen(
    viewModel: WeatherViewModel,
    navController: NavController
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val buttonText = remember { mutableStateOf("Enable Location") }

    // Launcher to open system settings and get a callback when user returns
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val success = checkAndRetry(context)
        if (success) {
            buttonText.value = "Retry"
        }
    }

    when (val state = uiState) {
        is WeatherUiState.Error -> {
            val errorMessage = state.message
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (buttonText.value == "Enable Location") {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            settingsLauncher.launch(intent)
                        } else {
                            navController.navigate(AppNavRoute.Weather.route) {
                                launchSingleTop = true
                                popUpTo(AppNavRoute.Weather.route)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(buttonText.value)
                }
            }
        }

        WeatherUiState.Loading -> {
            CircularProgressIndicator()
        }

        is WeatherUiState.Success -> {
            navController.popBackStack(AppNavRoute.Weather.route, inclusive = false)
        }
    }
}

private fun checkAndRetry(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val gpsEnabled = try {
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (_: Exception) {
        false
    }

    val permissionGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED

    if (!permissionGranted) {
        Toast.makeText(
            context,
            "Location permission is not granted. Please grant location access.",
            Toast.LENGTH_LONG
        ).show()
        return false
    }

    if (!gpsEnabled) {
        Toast.makeText(
            context,
            "GPS is disabled. Please enable location services.",
            Toast.LENGTH_LONG
        ).show()
        return false
    }
    return true
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load weather data. Please try again.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            onClick = { }) {
            Text(
                "Enable Location",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}