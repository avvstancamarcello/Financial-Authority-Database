package com.financialauthority.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.financialauthority.database.data.AppRepository
import com.financialauthority.database.data.SearchEngine
import com.financialauthority.database.data.UserPrefs
import com.financialauthority.database.data.resolve
import com.financialauthority.database.domain.AppCatalog
import com.financialauthority.database.domain.Country
import com.financialauthority.database.domain.Institution
import com.financialauthority.database.domain.SearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface CatalogUiState {
    data object Loading : CatalogUiState
    data class Error(val message: String) : CatalogUiState
    data class Ready(val catalog: AppCatalog) : CatalogUiState
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AppRepository(application)
    private val prefs = UserPrefs(application)

    private val _catalogState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val catalogState: StateFlow<CatalogUiState> = _catalogState

    val favorites = prefs.favorites.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())
    val language = prefs.language.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "it")

    val aboutSections: StateFlow<List<Pair<String, String>>> = language.combine(_catalogState) { locale, _ ->
        listOf(
            "privacy" to if (locale == "it") "Privacy" else "Privacy",
            "terms" to if (locale == "it") "Termini di Servizio" else "Terms of Service",
            "disclaimer" to "Disclaimer",
            "sources" to if (locale == "it") "Fonti e Metodologia" else "Sources and Methodology",
            "licenses" to if (locale == "it") "Licenze Open Source" else "Open Source Licenses",
            "contact" to if (locale == "it") "Contatti" else "Contact"
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _catalogState.value = CatalogUiState.Loading
            val result = repository.loadCatalog()
            _catalogState.value = result.catalog?.let { CatalogUiState.Ready(it) }
                ?: CatalogUiState.Error(result.error ?: "Errore sconosciuto")
        }
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch { prefs.toggleFavorite(id) }
    }

    fun setLanguage(locale: String) {
        viewModelScope.launch { prefs.setLanguage(locale) }
    }

    suspend fun loadLegal(doc: String, locale: String): String = repository.loadLegalDocument(doc, locale)

    fun findCountry(catalog: AppCatalog, countryKey: String): Country? =
        catalog.countries.firstOrNull { it.countryKey == countryKey }

    fun findInstitution(catalog: AppCatalog, institutionId: String): Institution? =
        catalog.institutions.firstOrNull { it.id == institutionId }

    fun search(catalog: AppCatalog, query: String, locale: String): List<SearchResult> {
        return SearchEngine.search(catalog, query, locale)
    }
}
