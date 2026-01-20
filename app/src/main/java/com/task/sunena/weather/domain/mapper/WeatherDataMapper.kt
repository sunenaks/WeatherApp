package com.task.sunena.weather.domain.mapper

import com.task.sunena.weather.data.models.WeatherForecastResponse
import com.task.sunena.weather.presentation.state.DailyForecastUiModel

interface WeatherDataMapper {
    fun mapToUiModel(
        response: WeatherForecastResponse
    ): List<DailyForecastUiModel>
}