package com.financialauthority.database.data

import android.content.Context
import com.financialauthority.database.domain.AppCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class LoadResult(
    val catalog: AppCatalog? = null,
    val error: String? = null
)

class AppRepository(private val context: Context) {
    suspend fun loadCatalog(): LoadResult = withContext(Dispatchers.IO) {
        try {
            val countries = loadAsset("financial_authorities_database.json")
            val institutions = loadAsset("data/international_financial_institutions.json")
            val categories = loadAsset("data/international_institutions_categories.json")
            val types = loadAsset("data/international_institutions_types.json")
            val i18n = loadAsset("data/international_institutions_i18n.json")

            val result = JsonParsers.parseCatalog(countries, institutions, categories, types, i18n)
            result.fold(
                onSuccess = { LoadResult(catalog = it) },
                onFailure = { LoadResult(error = "JSON non valido: ${it.message}") }
            )
        } catch (e: Exception) {
            LoadResult(error = "Impossibile caricare i dataset locali: ${e.message}")
        }
    }

    suspend fun loadLegalDocument(docId: String, locale: String): String = withContext(Dispatchers.IO) {
        val preferred = "legal/${docId}_${locale}.md"
        val en = "legal/${docId}_en.md"
        val it = "legal/${docId}_it.md"
        runCatching { loadAsset(preferred) }
            .recoverCatching { loadAsset(en) }
            .recoverCatching { loadAsset(it) }
            .getOrElse { "Contenuto non disponibile." }
    }

    private fun loadAsset(path: String): String =
        context.assets.open(path).bufferedReader().use { it.readText() }
}
