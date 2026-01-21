package com.task.sunena.weather.presentation.navigation

import WeatherForecastScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.sunena.weather.presentation.ui.ErrorScreen
import com.task.sunena.weather.presentation.viewmodel.WeatherViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: WeatherViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = AppNavRoute.Weather.route
    ) {

        composable(AppNavRoute.Weather.route) {
            WeatherForecastScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable(route = AppNavRoute.Error.route){
            ErrorScreen(viewModel = viewModel, navController = navController)
        }
    }
}