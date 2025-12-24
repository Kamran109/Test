package com.example.weathertracker.repository

import com.example.weathertracker.data.City
import com.example.weathertracker.data.Weather
//import com.example.weathertracker.data.WeatherResponse
//import com.example.weathertracker.network.MockWeatherService
import com.example.weathertracker.network.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {

//    private val weatherApi = MockWeatherService() // Using mock service for demo

    private val weatherApi = WeatherApi.service
    
    suspend fun getWeatherForCity(city: City): Result<Weather> = withContext(Dispatchers.IO) {
        try {
            val response = weatherApi.getWeather(
                cityName = "${city.name},${city.country}",
                apiKey = "deec093cb5c7634e023337f84554adc5" // Replace with actual API key
            )
            
            val weather = Weather(
                city = response.name,
                country = response.sys.country,
                temperature = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "Unknown",
                humidity = response.main.humidity,
                windSpeed = response.wind.speed,
                icon = response.weather.firstOrNull()?.icon ?: "01d",
                pressure = response.main.pressure
            )
            
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getWeatherForCities(cities: List<City>): List<Weather> = withContext(Dispatchers.IO) {
        cities.mapNotNull { city ->
            getWeatherForCity(city).getOrNull()
        }
    }
}
