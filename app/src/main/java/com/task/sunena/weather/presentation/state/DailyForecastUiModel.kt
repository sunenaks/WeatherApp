package com.task.sunena.weather.presentation.state

data class DailyForecastUiModel(
    val day: String,
    val icon: String,
    val temperature: String,
    val weatherType: WeatherType
)
