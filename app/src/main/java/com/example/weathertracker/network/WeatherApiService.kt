package com.example.weathertracker.network

import com.example.weathertracker.data.Main
import com.example.weathertracker.data.Sys
import com.example.weathertracker.data.WeatherInfo
import com.example.weathertracker.data.WeatherResponse
import com.example.weathertracker.data.Wind
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}

object WeatherApi {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private const val API_KEY = "deec093cb5c7634e023337f84554adc5"
    // Replace with actual API key
    
    val service: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}

// Mock weather service for testing without API key
//class MockWeatherService : WeatherApiService {
//    override suspend fun getWeather(cityName: String, apiKey: String, units: String): WeatherResponse {
//        // Simulate network delay
//        kotlinx.coroutines.delay(1000)
//
//        // Return mock data based on city name
//        val mockData = when {
//            cityName.contains("New York", ignoreCase = true) -> WeatherResponse(
//                name = "New York",
//                sys = Sys("US"),
//                main = Main(22.0, 65, 1000),
//                weather = listOf(WeatherInfo("Clear sky", "01d")),
//                wind = Wind(3.5),
//                pressure = Main(1000, 80, 1000)
//            )
//            cityName.contains("London", ignoreCase = true) -> WeatherResponse(
//                name = "London",
//                sys = Sys("GB"),
//                main = Main(15.0, 80),
//                weather = listOf(WeatherInfo("Cloudy", "04d")),
//                wind = Wind(2.1)
//            )
//            cityName.contains("Tokyo", ignoreCase = true) -> WeatherResponse(
//                name = "Tokyo",
//                sys = Sys("JP"),
//                main = Main(28.0, 70),
//                weather = listOf(WeatherInfo("Partly cloudy", "03d")),
//                wind = Wind(1.8)
//            )
//            cityName.contains("Bangkok", ignoreCase = true) -> WeatherResponse(
//                name = "Bangkok",
//                sys = Sys("TH"),
//                main = Main(32.0, 85),
//                weather = listOf(WeatherInfo("Hot and humid", "01d")),
//                wind = Wind(1.2)
//            )
//            else -> WeatherResponse(
//                name = cityName.split(",")[0],
//                sys = Sys("XX"),
//                main = Main(20.0, 60),
//                weather = listOf(WeatherInfo("Unknown", "01d")),
//                wind = Wind(2.0)
//            )
//        }
//
//        return mockData
//    }
//}
