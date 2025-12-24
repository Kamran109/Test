package com.example.weathertracker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_tracker")

class CityDataStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    
    private val trackedCitiesKey = stringPreferencesKey("tracked_cities")
    
    val trackedCities: Flow<List<City>> = context.dataStore.data.map { preferences ->
        val citiesJson = preferences[trackedCitiesKey] ?: "[]"
        try {
            json.decodeFromString<List<City>>(citiesJson)
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun saveTrackedCities(cities: List<City>) {
        context.dataStore.edit { preferences ->
            val citiesJson = json.encodeToString(cities)
            preferences[trackedCitiesKey] = citiesJson
        }
    }
}
