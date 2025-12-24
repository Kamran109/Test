package com.example.weathertracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertracker.data.City
import com.example.weathertracker.data.CityDataStore
import com.example.weathertracker.data.Weather
import com.example.weathertracker.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class WeatherUiState(
    val trackedCities: List<City> = emptyList(),
    val weatherData: Map<String, Weather> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class WeatherViewModel(private val context: Context) : ViewModel() {
    private val repository = WeatherRepository()
    private val cityDataStore = CityDataStore(context)
    
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    init {
        // Load cities from data store and initialize with popular cities if empty
        viewModelScope.launch {
            cityDataStore.trackedCities.collect { cities ->
                if (cities.isEmpty()) {
                    // Initialize with popular cities if no cities are stored
                    cityDataStore.saveTrackedCities(com.example.weathertracker.data.POPULAR_CITIES)
                } else {
                    _uiState.value = _uiState.value.copy(trackedCities = cities)
                    refreshWeatherData()
                }
            }
        }
    }
    
    fun addCity(cityName: String, countryCode: String) {
        viewModelScope.launch {
            val newCity = City(cityName, countryCode)
            val currentCities = _uiState.value.trackedCities.toMutableList()
            
            // Check if city already exists
            if (currentCities.none { it.name == cityName && it.country == countryCode }) {
                currentCities.add(newCity)
                _uiState.value = _uiState.value.copy(trackedCities = currentCities)
                cityDataStore.saveTrackedCities(currentCities)
                refreshWeatherData()
            }
        }
    }
    
    fun addCity(citySuggestion: com.example.weathertracker.data.CitySuggestion) {
        viewModelScope.launch {
            val newCity = City(citySuggestion.name, citySuggestion.country)
            val currentCities = _uiState.value.trackedCities.toMutableList()
            
            // Check if city already exists
            if (currentCities.none { it.name == citySuggestion.name && it.country == citySuggestion.country }) {
                currentCities.add(newCity)
                _uiState.value = _uiState.value.copy(trackedCities = currentCities)
                cityDataStore.saveTrackedCities(currentCities)
                refreshWeatherData()
            }
        }
    }
    
    fun removeCity(city: City) {
        viewModelScope.launch {
            val currentCities = _uiState.value.trackedCities.toMutableList()
            currentCities.remove(city)
            _uiState.value = _uiState.value.copy(trackedCities = currentCities)
            cityDataStore.saveTrackedCities(currentCities)
            
            // Remove weather data for this city
            val currentWeatherData = _uiState.value.weatherData.toMutableMap()
            currentWeatherData.remove("${city.name},${city.country}")
            _uiState.value = _uiState.value.copy(weatherData = currentWeatherData)
        }
    }
    
    fun refreshWeatherData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val weatherData = repository.getWeatherForCities(_uiState.value.trackedCities)
                val weatherMap = weatherData.associateBy { "${it.city},${it.country}" }
                
                _uiState.value = _uiState.value.copy(
                    weatherData = weatherMap,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to fetch weather data"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

}
