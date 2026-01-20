package com.task.sunena.weather.data.models


import com.google.gson.annotations.SerializedName

data class WeatherCondition(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String
)