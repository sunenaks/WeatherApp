package com.task.sunena.weather.data.models


import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val forecastItemList: List<ForecastItem>,
    @SerializedName("message")
    val message: Int
)