package com.task.sunena.weather.domain.mapper

import com.task.sunena.weather.data.models.WeatherForecastResponse
import com.task.sunena.weather.presentation.state.DailyForecastUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class WeatherDataMapperImpl @Inject constructor() : WeatherDataMapper {
    override fun mapToUiModel(response: WeatherForecastResponse): List<DailyForecastUiModel> {
        // Group the 3-hour forecasts by date and take the next 5 days
        return response.forecastItemList
            .groupBy { it.dtTxt.substringBefore(" ") }
            .mapNotNull { (date, forecastList) ->
                // Ensure the list for the day is not empty
                if (forecastList.isEmpty()) return@mapNotNull null

                // Calculate the highest temperature for the day
                val maxTemp = forecastList.maxOf { it.main.tempMax }

                // Prioritize the forecast closest to midday (e.g., 14:00)
                var representativeForecast = forecastList.find {
                    it.dtTxt.substringAfter(" ").startsWith("14:00")
                }

                //If no midday forecast, find the most frequent weather condition during the day
                if (representativeForecast == null) {
                    val dayForecasts = forecastList.filter { it.sys.pod == "d" }
                    if (dayForecasts.isNotEmpty()) {
                        representativeForecast = dayForecasts
                            .groupingBy { it.weatherCondition.firstOrNull()?.main }
                            .eachCount()
                            .maxByOrNull { it.value }
                            ?.key?.let { mostCommonWeather ->
                                dayForecasts.firstOrNull { it.weatherCondition.firstOrNull()?.main == mostCommonWeather }
                            }
                    }
                }

                if (representativeForecast == null) {
                    representativeForecast = forecastList.first()
                }

                val representativeWeather = representativeForecast.weatherCondition.firstOrNull()
               val weatherIconUrl =  representativeWeather?.icon?.let {
                    "https://openweathermap.org/img/wn/$it@2x.png"
                }
                DailyForecastUiModel(
                    day = formatDayOfWeek(date),
                    icon = weatherIconUrl.toString(),
                    temperature = kelvinToCelsius(maxTemp),
                    weatherType = representativeWeather?.main?.toWeatherType()!!
                )
            }
            .take(5) //  show a 5-day forecast

    }


    /**
     * Converts a date string (e.g., "2026-01-14") to a day name (e.g., "Wednesday").
     */
    private fun formatDayOfWeek(dateString: String): String {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) // <-- Corrected line
        if (dateString == today) {
            return "Today"
        }

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return date?.let { outputFormat.format(it) } ?: "Unknown"
    }
    /**
     * Converts temperature from Kelvin to Celsius and formats it as a string.
     */
    private fun kelvinToCelsius(kelvin: Double): String {
        val celsius = kelvin - 273.15
        return "${celsius.toInt()}°C"
    }
}