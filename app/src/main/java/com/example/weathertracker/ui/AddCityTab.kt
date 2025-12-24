package com.example.weathertracker.ui

//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.weathertracker.data.City
import com.example.weathertracker.data.CitySuggestion
import com.example.weathertracker.data.filterCitySuggestions
import com.example.weathertracker.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCityTab(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    var cityName by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    
    // Filter suggestions based on input
    val suggestions = remember(cityName) {
        if (cityName.isNotBlank()) {
            filterCitySuggestions(cityName, 8)
        } else {
            emptyList()
        }
    }
    
    // Close suggestions when clicking outside
    LaunchedEffect(Unit) {
        // This will close suggestions when the composable loses focus
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                showSuggestions = false
            }
    ) {
        Text(
            text = "Add New City",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Add city form
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "City Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // City name input with suggestions
                Box {
                    OutlinedTextField(
                        value = cityName,
                        onValueChange = { newValue ->
                            cityName = newValue
                            showSuggestions = newValue.isNotBlank()
                        },
                        label = { Text("City Name") },
                        placeholder = { Text("e.g., Paris, London, Tokyo") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                showSuggestions = focusState.isFocused && cityName.isNotBlank()
                            },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                showSuggestions = false
                            }
                        ),
                        trailingIcon = {
                            if (cityName.isNotBlank()) {
                                IconButton(
                                    onClick = { 
                                        cityName = ""
                                        showSuggestions = false
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        }
                    )
                    
                    // Suggestions dropdown
                    if (showSuggestions && suggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    // Prevent closing when clicking on the card itself
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.heightIn(max = 200.dp)
                            ) {
                                items(suggestions) { suggestion ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                cityName = suggestion.name
                                                showSuggestions = false
                                                // Add city directly when suggestion is selected
                                                val cityExists = uiState.trackedCities.any { 
                                                    it.name.equals(suggestion.name, ignoreCase = true) && 
                                                    it.country.equals(suggestion.country, ignoreCase = true) 
                                                }
                                                
                                                if (cityExists) {
                                                    errorMessage = "City is already being tracked"
                                                    showErrorMessage = true
                                                } else {
                                                    viewModel.addCity(suggestion)
                                                    cityName = ""
                                                    showSuccessMessage = true
                                                }
                                            }
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = suggestion.name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = suggestion.countryName,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        if (cityName.isNotBlank()) {
                            // Find matching suggestion
                            val matchingSuggestion = suggestions.firstOrNull { 
                                it.name.equals(cityName, ignoreCase = true) 
                            }
                            
                            if (matchingSuggestion != null) {
                                // Check if city already exists
                                val cityExists = uiState.trackedCities.any { 
                                    it.name.equals(matchingSuggestion.name, ignoreCase = true) && 
                                    it.country.equals(matchingSuggestion.country, ignoreCase = true) 
                                }
                                
                                if (cityExists) {
                                    errorMessage = "City is already being tracked"
                                    showErrorMessage = true
                                } else {
                                    viewModel.addCity(matchingSuggestion)
                                    cityName = ""
                                    showSuggestions = false
                                    showSuccessMessage = true
                                }
                            } else {
                                errorMessage = "City not found in suggestions. Please select from the dropdown."
                                showErrorMessage = true
                            }
                        } else {
                            errorMessage = "Please enter a city name"
                            showErrorMessage = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cityName.isNotBlank() && suggestions.any { 
                        it.name.equals(cityName, ignoreCase = true) 
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add City")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Success message
        if (showSuccessMessage) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "City added successfully!",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { showSuccessMessage = false }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Error message
        if (showErrorMessage) {
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
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { showErrorMessage = false }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Currently tracked cities
        Text(
            text = "Currently Tracked Cities",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (uiState.trackedCities.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No cities tracked yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(uiState.trackedCities) { city ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "${city.name}, ${city.country}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
