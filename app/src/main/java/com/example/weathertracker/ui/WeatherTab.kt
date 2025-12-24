package com.example.weathertracker.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.weathertracker.R
import com.example.weathertracker.WeatherDetailActivity
import com.example.weathertracker.data.City
import com.example.weathertracker.data.Weather
import com.example.weathertracker.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

// Function to determine weather type based on description
private fun getWeatherType(description: String): WeatherType {
    val desc = description.lowercase()
    return when {
        desc.contains("rain") || desc.contains("drizzle") || desc.contains("shower") -> WeatherType.RAINY
        desc.contains("sun") || desc.contains("clear") || desc.contains("sunny") -> WeatherType.SUNNY
        else -> WeatherType.CLOUDY // Default for cloudy, windy, overcast, etc.
    }
}

// Enum for weather types
private enum class WeatherType {
    RAINY, SUNNY, CLOUDY
}

// Function to get gradient colors based on weather type
@Composable
private fun getWeatherGradientColors(weatherType: WeatherType): Pair<Color, Color> {
    return when (weatherType) {
        WeatherType.RAINY -> Pair(
            colorResource(id = R.color.rainy_gradient_start),
            colorResource(id = R.color.rainy_gradient_end)
        )
        WeatherType.SUNNY -> Pair(
            colorResource(id = R.color.sunny_gradient_start),
            colorResource(id = R.color.sunny_gradient_end)
        )
        WeatherType.CLOUDY -> Pair(
            colorResource(id = R.color.cloudy_gradient_start),
            colorResource(id = R.color.cloudy_gradient_end)
        )
    }
}

// Function to get region name from country code
private fun getRegionName(countryCode: String): String {
    return when (countryCode.uppercase()) {
        "US" -> "United States"
        "GB" -> "United Kingdom"
        "JP" -> "Japan"
        "FR" -> "France"
        "DE" -> "Germany"
        "IT" -> "Italy"
        "ES" -> "Spain"
        "RU" -> "Russia"
        "CN" -> "China"
        "IN" -> "India"
        "BR" -> "Brazil"
        "AU" -> "Australia"
        "CA" -> "Canada"
        "TH" -> "Thailand"
        "KR" -> "South Korea"
        "MX" -> "Mexico"
        "NL" -> "Netherlands"
        "SE" -> "Sweden"
        "DK" -> "Denmark"
        "NO" -> "Norway"
        "FI" -> "Finland"
        else -> countryCode
    }
}

// Function to get current time
private fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date())
}

// Weather icon composable
@Composable
private fun WeatherIcon(
    weatherType: WeatherType,
    description: String,
    isSunny: Boolean
) {
    val iconColor = if (isSunny) Color.Black else Color.White
    
    when (weatherType) {
        WeatherType.RAINY -> {
            Icon(
                imageVector = Icons.Default.WaterDrop,
                contentDescription = "Rain",
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
        }
        WeatherType.SUNNY -> {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = "Sunny",
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
        }
        WeatherType.CLOUDY -> {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = "Cloudy",
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// City background illustration composable
@Composable
private fun CityBackgroundIllustration(
    cityName: String,
    modifier: Modifier = Modifier
) {
    // Simple text-based illustration for now
    // In a real app, you'd use actual vector graphics or images
    Text(
        text = when (cityName.lowercase()) {
            "washington" -> "🏛️"
            "sydney" -> "🏛️"
            "oslo" -> "🏔️"
            "london" -> "🏰"
            "paris" -> "🗼"
            "tokyo" -> "🗼"
            "new york" -> "🏢"
            else -> "🌆"
        },
        style = MaterialTheme.typography.displayLarge,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTab(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Header with refresh button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weather Tracker",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(
                onClick = { viewModel.refreshWeatherData() },
                enabled = !uiState.isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Error message
        uiState.error?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { viewModel.clearError() }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Weather list
            if (uiState.trackedCities.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No cities tracked yet.\nAdd some cities in the Add City tab!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.trackedCities) { city ->
                        val weather = uiState.weatherData["${city.name},${city.country}"]
                        WeatherCard(
                            city = city,
                            weather = weather,
                            onRemove = { viewModel.removeCity(city) }
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherCard(
    city: City,
    weather: Weather?,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
)
{
    // Determine weather type and get gradient colors

    val weatherType = weather?.let { getWeatherType(it.description) } ?: WeatherType.CLOUDY
    val (startColor, endColor) = getWeatherGradientColors(weatherType)
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable{
                val intent = Intent(context, WeatherDetailActivity::class.java).apply {
                    putExtra("CITY_NAME", city.name)
                    putExtra("COUNTRY", getRegionName(city.country))
                    putExtra("TEMP", weather?.temperature?.toInt() ?: 0)
                    putExtra("HUMIDITY", weather?.humidity ?: 0)

                    //Weather Details

                    putExtra("DESCRIPTION", weather?.description ?: "Cloudy")
                    putExtra("WIND_SPEED", weather?.windSpeed ?: 0.0)
                    putExtra("PRESSURE", weather?.pressure ?: 0)


//                    putExtra("VISIBILITY", weather?.visibility ?: 0)
//                    putExtra("UV_INDEX", weather?.uvIndex ?: 0)
//                    putExtra("SUNRISE", weather?.sunrise ?: "")
//                    putExtra("SUNSET", weather?.sunset ?: "")
                }
                context.startActivity(intent)
            },


        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(startColor, endColor),
                        start = Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                    )
                )
        ) {
            // Background city illustration
            CityBackgroundIllustration(
                cityName = city.name,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .alpha(0.3f)
            )

            // Main content
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - City info and temperature
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = city.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (weatherType == WeatherType.SUNNY) Color.Black else Color.White
                    )
                    Text(
                        text = getRegionName(city.country),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    weather?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${it.temperature.toInt()}°",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (weatherType == WeatherType.SUNNY) Color.Black else Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "→",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (weatherType == WeatherType.SUNNY) Color.Black else Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "☂",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (weatherType == WeatherType.SUNNY) Color.Black else Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${it.humidity}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (weatherType == WeatherType.SUNNY) Color.Black else Color.White
                            )
                        }
                    } ?: run {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (weatherType == WeatherType.SUNNY) Color.Black else Color.White
                        )
                    }
                }

                // Right side - Weather icon and time

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.height(160.dp).padding(end = 36.dp)

                ) {
                    Spacer(modifier = Modifier.height(40.dp))
                    WeatherIcon(
                        weatherType = weatherType,
                        description = weather?.description ?: "",
                        isSunny = weatherType == WeatherType.SUNNY

                    )
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = getCurrentTime(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (weatherType == WeatherType.SUNNY) Color.Black else Color.White
                    )
                }
            }

            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove city",
                    tint = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}