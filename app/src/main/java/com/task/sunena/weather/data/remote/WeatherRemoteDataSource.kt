package com.task.sunena.weather.data.remote

import com.task.sunena.weather.data.models.WeatherForecastResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApiService: WeatherApiService
) {

    fun fetchWeatherForecast(latitude:String, longitude:String, apiKey:String): Flow<WeatherForecastResponse> = flow {
        val weatherData = weatherApiService.fetchWeatherForecast(latitude,longitude,apiKey)
        emit(weatherData)

    }
}