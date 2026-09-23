package com.financialauthority.database.domain

data class FinancialAuthority(
    val authorityId: String,
    val name: String,
    val abbreviation: String?,
    val homepage: String?,
    val fraudReportLink: String?,
    val authorityEmail: String?,
    val relatedInternationalInstitutionIds: Set<String>
)

data class Country(
    val countryKey: String,
    val countryName: String,
    val flag: String,
    val isEU: Boolean,
    val protectionLevel: String,
    val notes: String?,
    val authority: FinancialAuthority
)

data class Institution(
    val id: String,
    val name: String,
    val shortName: String?,
    val abbreviation: String?,
    val italianName: String?,
    val category: String,
    val institutionType: String,
    val level: String,
    val region: String,
    val country: String?,
    val homepage: String?,
    val mandateUrl: String?,
    val recommendationsUrl: String?,
    val listsUrl: String?,
    val reportsUrl: String?,
    val alertsUrl: String?,
    val aboutUrl: String?,
    val contactsUrl: String?,
    val membersUrl: String?,
    val descriptionKey: String?,
    val relatedInstitutionIds: Set<String>,
    val relatedAuthorityIds: Set<String>,
    val scope: List<String>,
    val canPowers: List<String>,
    val cannotPowers: List<String>
)

data class AppCatalog(
    val countries: List<Country>,
    val institutions: List<Institution>,
    val i18nStrings: Map<String, Map<String, String>>,
    val categories: Map<String, String>,
    val institutionTypes: Map<String, String>
)

data class SearchResult(
    val id: String,
    val title: String,
    val subtitle: String,
    val route: String,
    val favoriteId: String
)
