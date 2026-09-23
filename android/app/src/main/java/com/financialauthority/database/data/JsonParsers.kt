package com.financialauthority.database.data

import com.financialauthority.database.domain.AppCatalog
import com.financialauthority.database.domain.Country
import com.financialauthority.database.domain.FinancialAuthority
import com.financialauthority.database.domain.Institution
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement

object JsonParsers {
    private val json = Json { ignoreUnknownKeys = true }

    fun parseCatalog(
        countriesJson: String,
        institutionsJson: String,
        categoriesJson: String,
        typesJson: String,
        i18nJson: String
    ): Result<AppCatalog> = runCatching {
        val countries = parseCountries(countriesJson)
        val institutions = parseInstitutions(institutionsJson)
        val categoryList = parseCategoryIds(categoriesJson)
        val typeList = parseTypeIds(typesJson)
        val i18nStrings = parseI18nStrings(i18nJson)

        val categoryLabels = categoryList.associateWith { key ->
            i18nStrings.resolve(key = "category.$key.label", locale = "en", fallback = key)
        }
        val typeLabels = typeList.associateWith { key ->
            i18nStrings.resolve(key = "type.$key.label", locale = "en", fallback = key)
        }

        val relations = buildRelations(countries, institutions)

        AppCatalog(
            countries = relations.first.sortedBy { it.countryName },
            institutions = relations.second.sortedBy { it.name },
            i18nStrings = i18nStrings,
            categories = categoryLabels,
            institutionTypes = typeLabels
        )
    }

    fun parseI18nStrings(i18nJson: String): Map<String, Map<String, String>> {
        val root = json.parseToJsonElement(i18nJson).jsonObject
        val strings = root["strings"]?.jsonObject ?: error("Missing strings in i18n")
        return strings.mapValues { (_, value) ->
            value.jsonObject.mapValues { (_, localizedValue) -> localizedValue.jsonPrimitive.content }
        }
    }

    private fun parseCountries(raw: String): List<Country> {
        val root = json.parseToJsonElement(raw).jsonObject
        return root.entries.map { (key, value) ->
            val item = value.jsonObject
            val authority = item["financial_authority"]?.jsonObject ?: error("Missing financial_authority for $key")
            val authorityId = authority.string("authorityId") ?: error("Missing authorityId for $key")
            Country(
                countryKey = normalizeKey(key),
                countryName = item.string("country_name") ?: key,
                flag = item.string("flag") ?: "🏳️",
                isEU = item.bool("isEU") ?: false,
                protectionLevel = item.string("protectionLevel") ?: "Unknown",
                notes = item.string("notes"),
                authority = FinancialAuthority(
                    authorityId = authorityId,
                    name = authority.string("name") ?: authorityId,
                    abbreviation = authority.string("abbreviation"),
                    homepage = authority.string("homepage"),
                    fraudReportLink = authority.string("fraudReportLink"),
                    authorityEmail = authority.string("authorityEmail"),
                    relatedInternationalInstitutionIds = authority.stringArray("relatedInternationalInstitutionIds").toSet()
                )
            )
        }
    }

    private fun parseInstitutions(raw: String): List<Institution> {
        val root = json.parseToJsonElement(raw).jsonObject
        val institutions = root["institutions"]?.jsonArray ?: error("Missing institutions list")
        return institutions.map { it.jsonObject.toInstitution() }
    }

    private fun parseCategoryIds(raw: String): List<String> {
        val root = json.parseToJsonElement(raw).jsonObject
        return root["categories"]?.jsonArray?.mapNotNull { it.jsonObject.string("id") } ?: emptyList()
    }

    private fun parseTypeIds(raw: String): List<String> {
        val root = json.parseToJsonElement(raw).jsonObject
        return root["institutionTypes"]?.jsonArray?.mapNotNull { it.jsonObject.string("id") } ?: emptyList()
    }

    private fun JsonObject.toInstitution(): Institution {
        val powers = this["powers"]?.jsonObject
        return Institution(
            id = string("id") ?: error("Institution id missing"),
            name = string("name") ?: error("Institution name missing"),
            shortName = string("shortName"),
            abbreviation = string("abbreviation"),
            italianName = string("italianName"),
            category = string("category") ?: "",
            institutionType = string("institutionType") ?: "",
            level = string("level") ?: "",
            region = string("region") ?: "",
            country = string("country"),
            homepage = string("homepage"),
            mandateUrl = string("mandateUrl"),
            recommendationsUrl = string("recommendationsUrl"),
            listsUrl = string("listsUrl"),
            reportsUrl = string("reportsUrl"),
            alertsUrl = string("alertsUrl"),
            aboutUrl = string("aboutUrl"),
            contactsUrl = string("contactsUrl"),
            membersUrl = string("membersUrl"),
            descriptionKey = string("descriptionKey"),
            relatedInstitutionIds = stringArray("relatedInstitutionIds").toSet(),
            relatedAuthorityIds = stringArray("relatedAuthorityIds").toSet(),
            scope = stringArray("scope"),
            canPowers = powers?.stringArray("can") ?: emptyList(),
            cannotPowers = powers?.stringArray("cannot") ?: emptyList()
        )
    }

    private fun buildRelations(
        countries: List<Country>,
        institutions: List<Institution>
    ): Pair<List<Country>, List<Institution>> {
        val authorityToInstitution = mutableMapOf<String, MutableSet<String>>()
        val institutionToAuthority = mutableMapOf<String, MutableSet<String>>()

        countries.forEach { country ->
            val authorityId = country.authority.authorityId
            country.authority.relatedInternationalInstitutionIds.forEach { institutionId ->
                authorityToInstitution.getOrPut(authorityId) { mutableSetOf() }.add(institutionId)
                institutionToAuthority.getOrPut(institutionId) { mutableSetOf() }.add(authorityId)
            }
        }

        institutions.forEach { institution ->
            institution.relatedAuthorityIds.forEach { authorityId ->
                authorityToInstitution.getOrPut(authorityId) { mutableSetOf() }.add(institution.id)
                institutionToAuthority.getOrPut(institution.id) { mutableSetOf() }.add(authorityId)
            }
        }

        val byId = institutions.associateBy { it.id }
        val institutionRelations = institutions.associate { institution ->
            val linked = institution.relatedInstitutionIds.toMutableSet()
            institution.relatedInstitutionIds.forEach { linkedId ->
                if (byId.containsKey(linkedId)) {
                    linked.add(linkedId)
                }
            }
            institution.id to linked
        }

        val updatedCountries = countries.map { country ->
            val mergedIds = (country.authority.relatedInternationalInstitutionIds +
                (authorityToInstitution[country.authority.authorityId] ?: emptySet())).toSet()
            country.copy(authority = country.authority.copy(relatedInternationalInstitutionIds = mergedIds))
        }

        val updatedInstitutions = institutions.map { institution ->
            institution.copy(
                relatedAuthorityIds = (institution.relatedAuthorityIds + (institutionToAuthority[institution.id] ?: emptySet())).toSet(),
                relatedInstitutionIds = (institution.relatedInstitutionIds + (institutionRelations[institution.id] ?: emptySet())).toSet()
            )
        }

        return updatedCountries to updatedInstitutions
    }

    private fun normalizeKey(raw: String): String = raw.lowercase()
        .replace("'", "")
        .replace(" ", "_")
        .replace("-", "_")

    private fun JsonObject.string(key: String): String? = this[key]?.jsonPrimitive?.contentOrNull

    private fun JsonObject.bool(key: String): Boolean? = this[key]?.jsonPrimitive?.booleanOrNull

    private fun JsonObject.stringArray(key: String): List<String> {
        val element = this[key] ?: return emptyList()
        return when (element) {
            is JsonArray -> element.mapNotNull { it.jsonPrimitive.contentOrNull }
            else -> emptyList()
        }
    }
}

fun Map<String, Map<String, String>>.resolve(key: String, locale: String, fallback: String): String {
    val record = this[key] ?: return fallback
    return record[locale] ?: record["en"] ?: record["it"] ?: fallback
}
