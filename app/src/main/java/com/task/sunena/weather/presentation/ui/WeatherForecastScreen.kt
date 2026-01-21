import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.task.sunena.weather.presentation.viewmodel.WeatherViewModel
import coil.compose.AsyncImage
import com.task.sunena.weather.R
import com.task.sunena.weather.presentation.navigation.AppNavRoute
import com.task.sunena.weather.presentation.navigation.NavigationEvent
import com.task.sunena.weather.presentation.state.DailyForecastUiModel
import com.task.sunena.weather.presentation.state.WeatherType
import com.task.sunena.weather.presentation.state.WeatherUiState
import com.task.sunena.weather.ui.theme.WeatherAppTheme
import kotlinx.coroutines.flow.collectLatest


@Composable
fun WeatherForecastScreen(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.navEvent.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateToError -> {
                    navController.navigate(AppNavRoute.Error.route) {
                        launchSingleTop = true
                        popUpTo(AppNavRoute.Weather.route)
                    }
                }
            }
        }
    }



    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading -> CircularProgressIndicator(color = Color.Black)
                is WeatherUiState.Success -> {
                    val selectedDayWeatherType = remember {
                        mutableStateOf(
                            state.forecasts.firstOrNull()?.weatherType ?: WeatherType.SUNNY
                        )
                    }
                    WeatherBackground(selectedDayWeatherType.value)
                    Column {
                        Text(
                            "5 Day Forecast",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )

                        HorizontalDivider(color = Color.White, thickness = 1.dp)

                        ForecastListItems(
                            forecasts = state.forecasts,
                            onItemClick = {
                                selectedDayWeatherType.value = it
                            }
                        )
                    }
                }

                is WeatherUiState.Error -> {
                    viewModel.observeGpsStatus()
                }
            }
        }
    }
}

@Composable
fun ForecastListItems(
    forecasts: List<DailyForecastUiModel>,
    onItemClick: (WeatherType) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(forecasts) { forecast ->
            ForecastCardItem(forecast = forecast, onClick = { onItemClick(forecast.weatherType) })
        }
    }
}

@Composable
fun ForecastCardItem(forecast: DailyForecastUiModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = forecast.day, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
            Text(text = forecast.temperature, style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.secondary)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            // Use AsyncImage to load the image from the URL
            AsyncImage(
                model = forecast.icon,
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),
                placeholder = painterResource(R.drawable.sun),
                error = painterResource(R.drawable.sun)
            )
        }
    }
}

@Composable
fun WeatherBackground(weatherType: WeatherType) {
    val backgroundResource = when (weatherType) {
        WeatherType.SUNNY -> R.drawable.sunny
        WeatherType.CLOUDY -> R.drawable.cloudy
        WeatherType.RAINY -> R.drawable.rainy
    }

    Image(
        painter = painterResource(id = backgroundResource),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true)
@Composable
fun WeatherForecastScreenPreview() {
    WeatherAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            WeatherBackground(WeatherType.SUNNY)
            Column {
                Text(
                    "5 Day Forecast", color = Color.White, modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                HorizontalDivider(color = Color.White, thickness = 1.dp)
                ForecastListItems(
                    forecasts = listOf(
                        DailyForecastUiModel(
                            "Monday",
                            "https://openweathermap.org/img/wn/01d@2x.png",
                            "25°C",
                            WeatherType.SUNNY
                        ),
                        DailyForecastUiModel(
                            "Tuesday",
                            "https://openweathermap.org/img/wn/01d@2x.png",
                            "22°C",
                            WeatherType.CLOUDY
                        ),
                        DailyForecastUiModel(
                            "Wednesday",
                            "https://openweathermap.org/img/wn/01d@2x.png",
                            "20°C",
                            WeatherType.RAINY
                        ),
                        DailyForecastUiModel(
                            "Thursday",
                            "https://openweathermap.org/img/wn/01d@2x.png",
                            "27°C",
                            WeatherType.RAINY
                        ),
                        DailyForecastUiModel(
                            "Friday",
                            "https://openweathermap.org/img/wn/01d@2x.png",
                            "23°C",
                            WeatherType.SUNNY
                        )
                    ),
                    onItemClick = {}
                )
            }
        }

    }
}