package com.financialauthority.database.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "financial_authority_prefs")

class UserPrefs(private val context: Context) {
    private val favoritesKey = stringSetPreferencesKey("favorites")
    private val languageKey = stringPreferencesKey("language")

    val favorites: Flow<Set<String>> = context.dataStore.data.map { prefs -> prefs[favoritesKey] ?: emptySet() }
    val language: Flow<String> = context.dataStore.data.map { prefs -> prefs[languageKey] ?: "it" }

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

    suspend fun clearFavorites() {
        context.dataStore.edit { prefs -> prefs.remove(favoritesKey) }
    }
}
