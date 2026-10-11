package com.financialauthority.database.data

import com.financialauthority.database.domain.Country

internal fun isUsCountryQuery(query: String): Boolean =
    query.trim().lowercase() in setOf("us", "usa", "united states")

internal fun matchesUsCountryAlias(country: Country, query: String): Boolean {
    if (!isUsCountryQuery(query)) return false
    val key = country.countryKey.lowercase()
    return key.startsWith("usa_") || key == "united_states_of_america"
}
