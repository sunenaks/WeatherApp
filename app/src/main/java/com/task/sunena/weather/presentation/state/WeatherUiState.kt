package com.task.sunena.weather.presentation.state


sealed interface WeatherUiState {
    data object Loading : WeatherUiState

    data class Success(
        val forecasts: List<DailyForecastUiModel>
    ) : WeatherUiState

    data class Error(
        val message: String
    ) : WeatherUiState

}