package com.financialauthority.database.data

import com.financialauthority.database.domain.AppCatalog
import com.financialauthority.database.domain.SearchResult
import com.financialauthority.database.navigation.Routes

object SearchEngine {
    fun search(catalog: AppCatalog, query: String, locale: String): List<SearchResult> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return emptyList()

        val countryResults = catalog.countries.filter { country ->
            listOf(
                country.countryName,
                country.authority.name,
                country.authority.abbreviation.orEmpty(),
                country.protectionLevel,
                country.notes.orEmpty()
            ).any { it.lowercase().contains(q) }
        }.map { country ->
            SearchResult(
                id = "country:${country.countryKey}",
                title = "${country.flag} ${country.countryName}",
                subtitle = country.authority.name,
                route = "${Routes.COUNTRIES}/${country.countryKey}",
                favoriteId = "country:${country.countryKey}"
            )
        }

        val authorityResults = catalog.countries.filter { country ->
            listOf(
                country.authority.name,
                country.authority.abbreviation.orEmpty(),
                country.countryName
            ).any { it.lowercase().contains(q) }
        }.map { country ->
            SearchResult(
                id = "authority:${country.authority.authorityId}",
                title = country.authority.name,
                subtitle = country.countryName,
                route = "${Routes.COUNTRIES}/${country.countryKey}",
                favoriteId = "authority:${country.authority.authorityId}"
            )
        }

        val institutionResults = catalog.institutions.filter { institution ->
            val desc = institution.descriptionKey?.let {
                catalog.i18nStrings.resolve(it, locale, it)
            }.orEmpty()
            listOf(
                institution.name,
                institution.shortName.orEmpty(),
                institution.abbreviation.orEmpty(),
                institution.italianName.orEmpty(),
                institution.category,
                institution.institutionType,
                institution.region,
                institution.level,
                institution.country.orEmpty(),
                institution.scope.joinToString(" "),
                desc
            ).any { it.lowercase().contains(q) }
        }.map { institution ->
            SearchResult(
                id = "institution:${institution.id}",
                title = institution.shortName ?: institution.name,
                subtitle = institution.name,
                route = "${Routes.INTERNATIONAL}/${institution.id}",
                favoriteId = "institution:${institution.id}"
            )
        }

        return (countryResults + authorityResults + institutionResults)
            .distinctBy { it.id }
            .take(200)
    }
}
