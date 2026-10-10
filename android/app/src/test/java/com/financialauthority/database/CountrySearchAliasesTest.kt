package com.financialauthority.database

import com.financialauthority.database.data.SearchEngine
import com.financialauthority.database.domain.AppCatalog
import com.financialauthority.database.domain.Country
import com.financialauthority.database.domain.FinancialAuthority
import com.financialauthority.database.ui.countries.deriveTileCountryCode
import com.financialauthority.database.ui.countries.matchesCountrySearchQuery
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CountrySearchAliasesTest {
    private val states = """
        alabama alaska arizona arkansas california colorado connecticut delaware florida georgia
        hawaii idaho illinois indiana iowa kansas kentucky louisiana maine maryland
        massachusetts michigan minnesota mississippi missouri montana nebraska nevada
        new_hampshire new_jersey new_mexico new_york north_carolina north_dakota ohio
        oklahoma oregon pennsylvania rhode_island south_carolina south_dakota tennessee
        texas utah vermont virginia washington west_virginia wisconsin wyoming
    """.trimIndent().split(Regex("\\s+"))

    @Test
    fun quickSearchAliasesFindAllStatesAndSecIndependentOfDisplayNames() {
        listOf("usa_", "usa___").forEach { prefix ->
            val usCountries = states.map { country("$prefix$it", it.replace('_', ' ')) } +
                country("united_states_of_america", "United States of America")
            assertEquals(51, usCountries.size)
            val countries = usCountries + country("italy", "Italy")
            listOf("us", "usa", "United States", " USA ").forEach { query ->
                assertEquals(query, usCountries, countries.filter { matchesCountrySearchQuery(it, query) })
            }
            assertEquals("CA", deriveTileCountryCode(usCountries.first { it.countryKey.endsWith("california") }))
        }
    }

    @Test
    fun globalSearchAliasesFindAllStatesAndSec() {
        val usCountries = states.map { country("usa___$it", it.replace('_', ' ')) } +
            country("united_states_of_america", "United States of America")
        val catalog = AppCatalog(
            countries = usCountries + country("italy", "Italy"),
            institutions = emptyList(),
            i18nStrings = emptyMap(),
            categories = emptyMap(),
            institutionTypes = emptyMap()
        )
        listOf("us", "usa", "United States", " US ").forEach { query ->
            val results = SearchEngine.search(catalog, query, "en")
            assertEquals(query, usCountries.map { "country:${it.countryKey}" }.toSet(),
                results.filter { it.id.startsWith("country:") }.map { it.id }.toSet())
            assertEquals(query, usCountries.map { "authority:${it.authority.authorityId}" }.toSet(),
                results.filter { it.id.startsWith("authority:") }.map { it.id }.toSet())
        }
    }

    @Test
    fun nonUsSearchAndTileFallbackStayUnchanged() {
        val italy = country("italy", "Italy")
        val canada = country("canada", "Canada")
        assertTrue(matchesCountrySearchQuery(italy, "it"))
        assertFalse(matchesCountrySearchQuery(italy, "usa"))
        assertFalse(matchesCountrySearchQuery(italy, "i"))
        assertTrue(matchesCountrySearchQuery(canada, "ca"))
        assertFalse(matchesCountrySearchQuery(country("usa_california", "California"), "ca"))
        assertTrue(matchesCountrySearchQuery(country("usa_california", "California"), "cal"))
        assertEquals("ITA", deriveTileCountryCode(italy))
        assertEquals("CAN", deriveTileCountryCode(canada))
    }

    private fun country(key: String, name: String) = Country(
        countryKey = key,
        countryName = name,
        flag = "",
        isEU = false,
        protectionLevel = "",
        notes = null,
        authority = FinancialAuthority(
            authorityId = "${key}__authority",
            name = "Authority",
            abbreviation = null,
            homepage = null,
            fraudReportLink = null,
            authorityEmail = null,
            relatedInternationalInstitutionIds = emptySet()
        )
    )
}
