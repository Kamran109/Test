package com.example.weathertracker.data

data class Weather(
    val city: String,
    val country: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String,
    val pressure: Int
)

data class WeatherResponse(
    val name: String,
    val sys: Sys,
    val main: Main,
    val weather: List<WeatherInfo>,
    val wind: Wind,
    val pressure: Main
)

data class Sys(
    val country: String
)

data class Main(
    val temp: Double,
    val humidity: Int,
    val pressure: Int  // YE LINE ADD KARO
)

data class WeatherInfo(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)
