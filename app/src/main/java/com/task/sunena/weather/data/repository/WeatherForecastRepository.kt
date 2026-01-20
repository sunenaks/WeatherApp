package com.task.sunena.weather.data.repository

import com.task.sunena.weather.data.models.WeatherForecastResponse
import com.task.sunena.weather.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherForecastRepository @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {
    fun fetchWeatherForecast(
        latitude: String,
        longitude: String,
        apiKey: String
    ): Flow<WeatherForecastResponse> =
        weatherRemoteDataSource.fetchWeatherForecast(latitude, longitude, apiKey)
}