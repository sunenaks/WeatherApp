package com.task.sunena.weather.domain.mapper

import com.task.sunena.weather.presentation.state.WeatherType


    fun String?.toWeatherType(): WeatherType {
        return when (this?.lowercase()) {
            "clear" -> WeatherType.SUNNY
            "clouds" -> WeatherType.CLOUDY
            "rain", "drizzle", "thunderstorm" ->
                WeatherType.RAINY
            else -> WeatherType.CLOUDY
        }
    }
