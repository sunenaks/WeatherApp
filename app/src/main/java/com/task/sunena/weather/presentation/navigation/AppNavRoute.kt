package com.task.sunena.weather.presentation.navigation


sealed class AppNavRoute(val route: String) {
    data object Weather : AppNavRoute("weather")
    data object Error : AppNavRoute("error")
}