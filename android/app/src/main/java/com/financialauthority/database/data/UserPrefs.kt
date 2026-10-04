package com.financialauthority.database.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "financial_authority_prefs")

class UserPrefs(private val context: Context) {
    private val favoritesKey = stringSetPreferencesKey("favorites")
    private val languageKey = stringPreferencesKey("language")
    private val totalConnectionsKey = intPreferencesKey("total_connections")
    private val starVotesKey = stringPreferencesKey("star_votes") // Stored as "1:count,2:count,3:count,4:count,5:count"
    private val visitedCardsKey = stringSetPreferencesKey("visited_cards")

    val favorites: Flow<Set<String>> = context.dataStore.data.map { prefs -> prefs[favoritesKey] ?: emptySet() }
    val language: Flow<String> = context.dataStore.data.map { prefs -> prefs[languageKey] ?: "it" }
    val totalConnections: Flow<Int> = context.dataStore.data.map { prefs -> prefs[totalConnectionsKey] ?: 0 }
    val starVotes: Flow<Map<Int, Int>> = context.dataStore.data.map { prefs ->
        val raw = prefs[starVotesKey] ?: "1:0,2:0,3:0,4:0,5:0"
        raw.split(",").associate { entry ->
            val parts = entry.split(":")
            (parts.getOrNull(0)?.toIntOrNull() ?: 1) to (parts.getOrNull(1)?.toIntOrNull() ?: 0)
        }
    }
    val visitedCards: Flow<Set<String>> = context.dataStore.data.map { prefs -> prefs[visitedCardsKey] ?: emptySet() }

    suspend fun toggleFavorite(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[favoritesKey]?.toMutableSet() ?: mutableSetOf()
            if (!current.add(id)) current.remove(id)
            prefs[favoritesKey] = current
        }
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { prefs -> prefs[languageKey] = language }
    }

    suspend fun incrementConnection() {
        context.dataStore.edit { prefs ->
            val current = prefs[totalConnectionsKey] ?: 0
            prefs[totalConnectionsKey] = current + 1
        }
    }

    suspend fun voteStar(star: Int) {
        context.dataStore.edit { prefs ->
            val raw = prefs[starVotesKey] ?: "1:0,2:0,3:0,4:0,5:0"
            val map = raw.split(",").associate { entry ->
                val parts = entry.split(":")
                (parts.getOrNull(0)?.toIntOrNull() ?: 1) to (parts.getOrNull(1)?.toIntOrNull() ?: 0)
            }.toMutableMap()
            map[star] = (map[star] ?: 0) + 1
            prefs[starVotesKey] = map.entries.joinToString(",") { "${it.key}:${it.value}" }
        }
    }

    suspend fun recordVisit(cardId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[visitedCardsKey]?.toMutableSet() ?: mutableSetOf()
            current.add(cardId)
            prefs[visitedCardsKey] = current
        }
    }

    suspend fun clearFavorites() {
        context.dataStore.edit { prefs -> prefs.remove(favoritesKey) }
    }
}
