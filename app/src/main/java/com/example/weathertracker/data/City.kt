package com.example.weathertracker.data

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val name: String,
    val country: String,
    val isTracked: Boolean = true
)

// Popular cities to initialize the app with
val POPULAR_CITIES = listOf(
    City("New York", "US"),
    City("London", "GB"),
    City("Tokyo", "JP"),
    City("Bangkok", "TH")
)
