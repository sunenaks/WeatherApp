package com.task.sunena.weather.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.sunena.weather.BuildConfig
import com.task.sunena.weather.domain.WeatherForecastUseCase
import com.task.sunena.weather.location.GpsStatusListener
import com.task.sunena.weather.location.UseLocationProvider
import com.task.sunena.weather.presentation.navigation.NavigationEvent
import com.task.sunena.weather.presentation.state.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val useLocationProvider: UseLocationProvider,
    private val weatherForecastUseCase: WeatherForecastUseCase,
    private val gpsStatusListener: GpsStatusListener
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _navEvent = Channel<NavigationEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    init {
        observeGpsStatus()
    }

    fun observeGpsStatus() {
        viewModelScope.launch {
            gpsStatusListener.gpsStatus.collect { isGpsEnabled ->
                if (isGpsEnabled) {
                    getWeatherForecasts(BuildConfig.API_KEY)

                } else {
                    val currentState = _uiState.value
                    when (currentState) {
                        is WeatherUiState.Success -> {
                           // no implementation
                        }

                        else -> {
                            _uiState.value =
                                WeatherUiState.Error("Please enable GPS to get weather data.")
                        }
                    }
                }
            }
        }
    }

    fun getWeatherForecasts(apiKey: String) {

        viewModelScope.launch {
            useLocationProvider.getCurrentLocation(onSuccess = {
                fetchWeatherForecasts(apiKey, it)
            }, onFailure = {
                _uiState.value =
                    WeatherUiState.Error("Could not retrieve location. Please enable GPS and grant permission.")
                _navEvent.trySend(NavigationEvent.NavigateToError("Could not retrieve location. Please enable GPS and grant permission."))

            })
        }
    }

    fun fetchWeatherForecasts(apiKey: String, location: Location) {
        viewModelScope.launch {
            weatherForecastUseCase(
                location.latitude.toString(),
                location.longitude.toString(),
                apiKey
            )
                .onStart {
                    _uiState.value = WeatherUiState.Loading
                }
                .catch { exception ->
                    _uiState.value =
                        WeatherUiState.Error("Failed to fetch weather: ${exception.message}")
                    _navEvent.trySend(NavigationEvent.NavigateToError("Failed to fetch weather: ${exception.message}"))
                }
                .collect { forecastList ->
                    _uiState.value = WeatherUiState.Success(forecastList)
                }
        }
    }
}