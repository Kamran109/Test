# Weather Tracker Android App

A modern Android weather tracking app built with Jetpack Compose that allows users to track weather conditions in multiple cities.

## Features

- **Two-tab interface:**
  - **Weather Tab**: Displays a list of tracked cities with current weather information
  - **Add City Tab**: Allows users to add new cities to track

- **Popular cities pre-loaded**: New York, London, Tokyo, and Bangkok are added automatically when the app is first run

- **Weather information includes:**
  - Temperature in Celsius
  - Weather description
  - Humidity percentage
  - Wind speed

- **Data persistence**: Tracked cities are saved locally and restored when the app is restarted

- **Modern UI**: Built with Material Design 3 and Jetpack Compose

## Setup Instructions

### 1. Get a Weather API Key

To use real weather data, you'll need to get a free API key from OpenWeatherMap:

1. Go to [OpenWeatherMap](https://openweathermap.org/api)
2. Sign up for a free account
3. Get your API key from the dashboard

### 2. Configure the API Key

1. Open `app/src/main/java/com/example/weathertracker/network/WeatherApiService.kt`
2. Replace `"your_api_key_here"` with your actual API key in two places:
   - Line 20: `private const val API_KEY = "your_actual_api_key_here"`
   - Line 18 in `WeatherRepository.kt`: `apiKey = "your_actual_api_key_here"`

### 3. Switch to Real API (Optional)

Currently, the app uses a mock weather service for demonstration. To use real weather data:

1. In `app/src/main/java/com/example/weathertracker/repository/WeatherRepository.kt`
2. Change line 11 from:
   ```kotlin
   private val weatherApi = MockWeatherService() // Using mock service for demo
   ```
   to:
   ```kotlin
   private val weatherApi = WeatherApi.service
   ```

## How to Use

### Adding Cities
1. Open the "Add City" tab
2. Enter the city name (e.g., "Paris")
3. Enter the country code (e.g., "FR")
4. Tap "Add City"

### Viewing Weather
1. Open the "Weather" tab
2. View weather information for all tracked cities
3. Use the refresh button to update weather data
4. Tap the delete icon to remove a city from tracking

### Removing Cities
- In the Weather tab: Tap the delete icon next to any city
- In the Add City tab: View the list of currently tracked cities

## Technical Details

### Architecture
- **MVVM Pattern**: Clean separation of concerns with ViewModels managing UI state
- **Repository Pattern**: Centralized data access layer
- **Jetpack Compose**: Modern declarative UI framework
- **DataStore**: For local data persistence
- **Retrofit**: For network requests (when using real API)

### Dependencies
- Jetpack Compose for UI
- Navigation Compose for tab navigation
- ViewModel and LiveData for state management
- Retrofit for networking
- DataStore for local storage
- Kotlinx Serialization for data serialization

## Project Structure

```
app/src/main/java/com/example/weathertracker/
├── data/
│   ├── City.kt                 # City data model
│   ├── Weather.kt              # Weather data models
│   └── CityDataStore.kt        # Data persistence
├── network/
│   └── WeatherApiService.kt    # API service and mock data
├── repository/
│   └── WeatherRepository.kt    # Data access layer
├── ui/
│   ├── WeatherTab.kt           # Weather display tab
│   ├── AddCityTab.kt           # Add city tab
│   └── WeatherTrackerApp.kt    # Main app navigation
├── viewmodel/
│   └── WeatherViewModel.kt     # UI state management
└── MainActivity.kt             # Main activity
```

## Requirements

- Android API 24+ (Android 7.0)
- Kotlin 2.0.21
- Jetpack Compose
- Internet permission for weather data

## Future Enhancements

- Weather forecasts (5-day, hourly)
- Weather maps
- Location-based weather
- Weather alerts and notifications
- Dark/light theme toggle
- Weather history
- Multiple temperature units (Fahrenheit, Kelvin)
