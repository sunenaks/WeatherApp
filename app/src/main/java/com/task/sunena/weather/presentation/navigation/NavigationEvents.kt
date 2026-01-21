package com.task.sunena.weather.presentation.navigation

sealed interface NavigationEvent {
    data class NavigateToError(val errorMessage: String) : NavigationEvent
}