package com.task.sunena.weather.data.models


import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName("3h")
    val last3Hours: Double
)