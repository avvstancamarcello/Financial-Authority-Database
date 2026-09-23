package com.financialauthority.database

import com.financialauthority.database.data.JsonParsers
import com.financialauthority.database.data.SearchEngine
import com.financialauthority.database.data.resolve
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogParsingTest {
    private val countriesJson = """
        {
          "Italy": {
            "country_name": "Italy",
            "flag": "🇮🇹",
            "isEU": true,
            "protectionLevel": "Altissimo",
            "financial_authority": {
              "name": "CONSOB",
              "abbreviation": "CONSOB",
              "authorityId": "italy__consob",
              "relatedInternationalInstitutionIds": ["fatf_gafi"]
            },
            "notes": "UIF nazionale"
          }
        }
    """.trimIndent()

    private val institutionsJson = """
        {
          "institutions": [
            {
              "id": "fatf_gafi",
              "name": "Financial Action Task Force",
              "shortName": "FATF",
              "category": "aml_cft_policy",
              "institutionType": "intergovernmental_standard_setter",
              "level": "global",
              "region": "worldwide",
              "descriptionKey": "institution.fatf_gafi.description",
              "relatedInstitutionIds": ["moneyval"],
              "relatedAuthorityIds": [],
              "scope": ["money laundering"],
              "powers": { "can": ["set standards"], "cannot": ["seize assets"] }
            },
            {
              "id": "moneyval",
              "name": "MONEYVAL",
              "category": "aml_cft_policy",
              "institutionType": "regional_fatf_style_body",
              "level": "regional",
              "region": "europe",
              "descriptionKey": "institution.moneyval.description",
              "relatedInstitutionIds": [],
              "relatedAuthorityIds": ["italy__consob"],
              "scope": ["europe"]
            }
          ]
        }
    """.trimIndent()

    private val categoriesJson = """
        { "categories": [ {"id":"aml_cft_policy"} ] }
    """.trimIndent()

    private val typesJson = """
        { "institutionTypes": [ {"id":"intergovernmental_standard_setter"}, {"id":"regional_fatf_style_body"} ] }
    """.trimIndent()

    private val i18nJson = """
        {
          "strings": {
            "category.aml_cft_policy.label": { "it":"AML IT", "en":"AML EN" },
            "type.intergovernmental_standard_setter.label": { "it":"Tipo IT", "en":"Type EN" },
            "type.regional_fatf_style_body.label": { "en":"FSRB" },
            "institution.fatf_gafi.description": { "en":"FATF desc" }
          }
        }
    """.trimIndent()

    @Test
    fun parseCatalog_buildsRelationsAndSearch() {
        val catalog = JsonParsers.parseCatalog(
            countriesJson,
            institutionsJson,
            categoriesJson,
            typesJson,
            i18nJson
        ).getOrThrow()

        val italy = catalog.countries.first()
        assertTrue(italy.authority.relatedInternationalInstitutionIds.contains("moneyval"))

        val moneyval = catalog.institutions.first { it.id == "moneyval" }
        assertTrue(moneyval.relatedAuthorityIds.contains("italy__consob"))

        val results = SearchEngine.search(catalog, "money laundering", "en")
        assertTrue(results.any { it.id == "institution:fatf_gafi" })
    }

    @Test
    fun i18nFallback_prefersRequestedThenEnglishThenItalian() {
        val strings = JsonParsers.parseI18nStrings(i18nJson)
        assertEquals("FATF desc", strings.resolve("institution.fatf_gafi.description", "fr", "fallback"))
        assertEquals("fallback", strings.resolve("missing.key", "en", "fallback"))
    }
}
