package com.task.sunena.weather.domain

import android.util.Log
import com.task.sunena.weather.data.repository.WeatherForecastRepository
import com.task.sunena.weather.di.DefaultDispatcher
import com.task.sunena.weather.domain.mapper.WeatherDataMapper
import com.task.sunena.weather.presentation.state.DailyForecastUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * This use case fetches the weather forecast data
 */
class WeatherForecastUseCase @Inject constructor(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val weatherDataMapper: WeatherDataMapper,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    operator fun invoke(
        latitude: String,
        longitude: String,
        apiKey: String
    ): Flow<List<DailyForecastUiModel>> =
        weatherForecastRepository.fetchWeatherForecast(latitude, longitude, apiKey)
            .map { response ->
                Log.d("WeatherForecast", "Fetched weather data: $response")
                weatherDataMapper.mapToUiModel(response)
            }
            .flowOn(defaultDispatcher)


}