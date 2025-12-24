package com.example.weathertracker

// WeatherDetailActivity.kt

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathertracker.ui.theme.WeatherTrackerTheme

// Line 61 ke BAAD add karo (DailyForecast data class ke baad)

// ============================================
// Helper Functions for Weather Icons
// ============================================

private enum class WeatherType {
    RAINY, SUNNY, CLOUDY
}
@Composable
private fun getWeatherGradientColors(description: String): Pair<Color, Color> {
    val weatherType = getWeatherType(description)
    return when (weatherType) {

        WeatherType.RAINY -> Pair(
           colorResource(id = R.color.rainy_gradient_start),  // Rainy gradient start
            colorResource(id = R.color.rainy_gradient_end)   // Rainy gradient end
        )
        WeatherType.SUNNY -> Pair(
            colorResource(id = R.color.sunny_gradient_start),  // Sunny gradient start
            colorResource(id = R.color.sunny_gradient_end)   // Sunny gradient end
        )
        WeatherType.CLOUDY -> Pair(
            colorResource(id = R.color.cloudy_gradient_start),  // Cloudy gradient start
            colorResource(id = R.color.cloudy_gradient_end)   // Cloudy gradient end
        )
    }
}


private fun getWeatherType(description: String): WeatherType {
    val desc = description.lowercase()
    return when {
        desc.contains("rain") ||
        desc.contains("drizzle") ||
        desc.contains("shower") -> WeatherType.RAINY
        desc.contains("sun") ||
        desc.contains("clear") ||
        desc.contains("sunny") -> WeatherType.SUNNY
        else -> WeatherType.CLOUDY
    }
}

private fun getWeatherIcon(description: String): ImageVector {
    val weatherType = getWeatherType(description)
    return when (weatherType) {
        WeatherType.RAINY -> Icons.Default.WaterDrop
        WeatherType.SUNNY -> Icons.Default.WbSunny
        WeatherType.CLOUDY -> Icons.Default.Cloud
    }
}


// ============================================
// Data Classes
// ============================================

data class WeatherData(
    val cityName: String,
    val country: String,
    val currentTemp: Int,
    val feelsLike: Int,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val visibility: Int,
    val uvIndex: Int,
    val sunrise: String,
    val sunset: String
)

data class HourlyForecast(
    val time: String,
    val temp: Int,
    val icon: ImageVector,
//    val cardColor: String
)

data class DailyForecast(
    val day: String,
    val high: Int,
    val low: Int,
    val condition: String,
    val icon: ImageVector
)

// ============================================
// Activity
// ============================================

class WeatherDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Intent se data receive karo
        val cityName = intent.getStringExtra("CITY_NAME") ?: "Unknown"
        val country = intent.getStringExtra("COUNTRY") ?: "Unknown"
        val temp = intent.getIntExtra("TEMP", 0)
        val humidity = intent.getIntExtra("HUMIDITY", 0)

        val description = intent.getStringExtra("DESCRIPTION") ?: "Unknown"
        val windSpeed = intent.getDoubleExtra("WIND_SPEED", 0.0)
        val pressure = intent.getIntExtra("PRESSURE", 0)


        // Weather data prepare karo
        val weatherData = WeatherData(
            cityName = cityName,
            country = country,
            currentTemp = temp,
            feelsLike = temp - 2,
            condition = description,
            humidity = humidity,
            windSpeed = windSpeed,
            pressure = pressure,
            visibility = 10,
            uvIndex = 2,
            sunrise = "06:45",
            sunset = "16:30"
        )

        setContent {
            WeatherTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherDetailScreen(
                        weatherData = weatherData,
                        onBackClick = {

                            finish()
                        }
                    )
                }
            }
        }
    }
}

// ============================================
// Main Screen Composable
// ============================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailScreen(
    weatherData: WeatherData,
    onBackClick: () -> Unit
) {
    val currentWeatherIcon = getWeatherIcon(weatherData.condition)

    val (gradientStart, gradientEnd) = getWeatherGradientColors(weatherData.condition)

    // Sample hourly forecast data
    val hourlyForecast = listOf(
//        HourlyForecast("Now", weatherData.currentTemp, Icons.Default.Cloud),

        HourlyForecast("Now", weatherData.currentTemp, currentWeatherIcon),

//        HourlyForecast("15:00", weatherData.currentTemp + 1, Icons.Default.Cloud),
//        HourlyForecast("16:00", weatherData.currentTemp, Icons.Default.Cloud),

        HourlyForecast("15:00", weatherData.currentTemp + 1, currentWeatherIcon),
        HourlyForecast("16:00", weatherData.currentTemp, currentWeatherIcon),

        HourlyForecast("17:00", weatherData.currentTemp - 1, currentWeatherIcon),
        HourlyForecast("18:00", weatherData.currentTemp - 2, currentWeatherIcon),
        HourlyForecast("19:00", weatherData.currentTemp - 3, currentWeatherIcon),
        HourlyForecast("20:00", weatherData.currentTemp - 4, currentWeatherIcon),
        HourlyForecast("21:00", weatherData.currentTemp - 5, currentWeatherIcon)
    )

    // Sample weekly forecast data
    val weeklyForecast = listOf(
//        DailyForecast("Today", weatherData.currentTemp + 1, weatherData.currentTemp - 3, "Cloudy", Icons.Default.Cloud),

        DailyForecast("Today", weatherData.currentTemp + 1, weatherData.currentTemp - 3, weatherData.condition, currentWeatherIcon),

        DailyForecast("Tomorrow", weatherData.currentTemp + 2, weatherData.currentTemp - 2, "Rainy", Icons.Default.Umbrella),
        DailyForecast("Wed", weatherData.currentTemp + 3, weatherData.currentTemp - 1, "Partly Cloudy", Icons.Default.Cloud),
        DailyForecast("Thu", weatherData.currentTemp + 4, weatherData.currentTemp, "Sunny", Icons.Default.WbSunny),
        DailyForecast("Fri", weatherData.currentTemp + 2, weatherData.currentTemp - 2, "Cloudy", Icons.Default.Cloud),
        DailyForecast("Sat", weatherData.currentTemp + 1, weatherData.currentTemp - 3, "Rainy", Icons.Default.Umbrella),
        DailyForecast("Sun", weatherData.currentTemp + 3, weatherData.currentTemp - 1, "Sunny", Icons.Default.WbSunny)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Weather Details",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF212121),
                    navigationIconContentColor = Color(0xFF212121)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Weather Card
            item {
//                CurrentWeatherCard(weatherData)

                CurrentWeatherCard(weatherData, currentWeatherIcon, gradientStart, gradientEnd)
            }

            // Hourly Forecast Section
            item {
                Text(
                    text = "Hourly Forecast",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121)
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(hourlyForecast) { hour ->
                        HourlyForecastCard(hour)
                    }
                }
            }

            // 7-Day Forecast Section
            item {
                Text(
                    text = "7-Day Forecast",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        weeklyForecast.forEachIndexed { index, day ->
                            DailyForecastRow(day)
                            if (index < weeklyForecast.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = Color(0xFFEEEEEE),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }

            // Additional Information Section
            item {
                Text(
                    text = "Additional Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Row 1: Humidity & Wind Speed
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.WaterDrop,
                        label = "Humidity",
                        value = "${weatherData.humidity}%",
                        iconTint = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = Icons.Default.Air,
                        label = "Wind Speed",
                        value = "${weatherData.windSpeed} km/h",
                        iconTint = Color(0xFF757575),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 2: Pressure & Visibility
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.Speed,
                        label = "Pressure",
                        value = "${weatherData.pressure} mb",
                        iconTint = Color(0xFF9C27B0),
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = Icons.Default.Visibility,
                        label = "Visibility",
                        value = "${weatherData.visibility} km",
                        iconTint = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Row 3: Sunrise & Sunset
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Default.WbTwilight,
                        label = "Sunrise",
                        value = weatherData.sunrise,
                        iconTint = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = Icons.Default.NightsStay,
                        label = "Sunset",
                        value = weatherData.sunset,
                        iconTint = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ============================================
// Composable Components
// ============================================

@Composable
fun CurrentWeatherCard(
    weatherData: WeatherData,
    currentWeatherIcon:ImageVector,
    gradientStart: Color,
    gradientEnd: Color
) {
    val weatherType = weatherData?.let { getWeatherType(it.condition) } ?: WeatherType.CLOUDY

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(gradientStart, gradientEnd),
                        start = Offset(0f, 0f),
                        end = Offset(1000f, 1000f)
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Left side - City info and temperature
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = weatherData.cityName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
//                            color = Color.White
                            color = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f)

                        )
                    }
                    Text(
                        text = weatherData.country,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 28.dp),
                        color = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "${weatherData.currentTemp}°",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f)
                    )

                    Text(
                        text = weatherData.condition,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp),
                        color = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f)
                    )

                    Text(
                        text = "Feels like ${weatherData.feelsLike}°",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        color = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f)
                    )
                }

                // Right side - Weather icon
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(
//                        Icons.Default.Cloud,
                        currentWeatherIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(16.dp),
                        tint = if (weatherType == WeatherType.SUNNY) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyForecastCard(hourlyForecast: HourlyForecast) {

//    val (cardColor, textColor) = when (getWeatherType(hourlyForecast.cardColor)) {
//        WeatherType.RAINY -> Pair(colorResource(id = R.color.rainy_gradient_start), colorResource(id = R.color.rainy_gradient_end))
//        WeatherType.SUNNY -> Pair(colorResource(id = R.color.sunny_gradient_start), colorResource(id = R.color.sunny_gradient_end))
//        WeatherType.CLOUDY -> Pair(colorResource(id = R.color.cloudy_gradient_start), colorResource(id = R.color.cloudy_gradient_end))
//    }

    Card(
        modifier = Modifier.width(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = hourlyForecast.time,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.7f)

            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                hourlyForecast.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Black.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${hourlyForecast.temp}°",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun DailyForecastRow(forecast: DailyForecast) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                forecast.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF616161)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = forecast.day,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121),
                modifier = Modifier.width(80.dp)
            )
        }

        Text(
            text = forecast.condition,
            fontSize = 14.sp,
            color = Color(0xFF757575),
            modifier = Modifier.weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${forecast.high}°",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF212121)
            )
            Text(
                text = "${forecast.low}°",
                fontSize = 16.sp,
                color = Color(0xFFBDBDBD)
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = iconTint
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            }
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
        }
    }
}