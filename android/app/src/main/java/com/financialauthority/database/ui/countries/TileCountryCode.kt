package com.financialauthority.database.ui.countries

import com.financialauthority.database.domain.Country

private val usStateCodes = mapOf(
    "alabama" to "AL",
    "alaska" to "AK",
    "arizona" to "AZ",
    "arkansas" to "AR",
    "california" to "CA",
    "colorado" to "CO",
    "connecticut" to "CT",
    "delaware" to "DE",
    "florida" to "FL",
    "georgia" to "GA",
    "hawaii" to "HI",
    "idaho" to "ID",
    "illinois" to "IL",
    "indiana" to "IN",
    "iowa" to "IA",
    "kansas" to "KS",
    "kentucky" to "KY",
    "louisiana" to "LA",
    "maine" to "ME",
    "maryland" to "MD",
    "massachusetts" to "MA",
    "michigan" to "MI",
    "minnesota" to "MN",
    "mississippi" to "MS",
    "missouri" to "MO",
    "montana" to "MT",
    "nebraska" to "NE",
    "nevada" to "NV",
    "new_hampshire" to "NH",
    "new_jersey" to "NJ",
    "new_mexico" to "NM",
    "new_york" to "NY",
    "north_carolina" to "NC",
    "north_dakota" to "ND",
    "ohio" to "OH",
    "oklahoma" to "OK",
    "oregon" to "OR",
    "pennsylvania" to "PA",
    "rhode_island" to "RI",
    "south_carolina" to "SC",
    "south_dakota" to "SD",
    "tennessee" to "TN",
    "texas" to "TX",
    "utah" to "UT",
    "vermont" to "VT",
    "virginia" to "VA",
    "washington" to "WA",
    "west_virginia" to "WV",
    "wisconsin" to "WI",
    "wyoming" to "WY"
)

internal fun deriveTileCountryCode(country: Country): String {
    val key = country.countryKey
    if (key == "united_states_of_america") return "USA"
    if (key.startsWith("usa_")) {
        usStateCodes[key.removePrefix("usa_").trimStart('_')]?.let { return it }
    }
    return when {
        key.contains("united_kingdom") -> "UK"
        key.contains("united_states") || key.startsWith("usa") -> "USA"
        key.contains("canada") -> "CAN"
        else -> key.take(3).uppercase().replace("_", "")
    }
}
