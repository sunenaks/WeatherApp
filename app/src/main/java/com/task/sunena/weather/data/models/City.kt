package com.task.sunena.weather.data.models


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("coord")
    val coordinates: Coordinates,
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("population")
    val population: Int,
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int,
    @SerializedName("timezone")
    val timezone: Int
)