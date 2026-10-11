package com.financialauthority.database

import com.financialauthority.database.data.JsonParsers
import com.financialauthority.database.domain.Country
import com.financialauthority.database.domain.FinancialAuthority
import com.financialauthority.database.ui.countries.deriveTileCountryCode
import org.junit.Assert.assertEquals
import org.junit.Test

class TileCountryCodeTest {
    @Test
    fun federalAuthorityKeepsUsa() {
        assertEquals("USA", deriveTileCountryCode(country("united_states_of_america")))
    }

    @Test
    fun allFiftyStatesUseOfficialPostalCodes() {
        val states = """
            alabama AL, alaska AK, arizona AZ, arkansas AR, california CA,
            colorado CO, connecticut CT, delaware DE, florida FL, georgia GA,
            hawaii HI, idaho ID, illinois IL, indiana IN, iowa IA,
            kansas KS, kentucky KY, louisiana LA, maine ME, maryland MD,
            massachusetts MA, michigan MI, minnesota MN, mississippi MS, missouri MO,
            montana MT, nebraska NE, nevada NV, new_hampshire NH, new_jersey NJ,
            new_mexico NM, new_york NY, north_carolina NC, north_dakota ND, ohio OH,
            oklahoma OK, oregon OR, pennsylvania PA, rhode_island RI, south_carolina SC,
            south_dakota SD, tennessee TN, texas TX, utah UT, vermont VT,
            virginia VA, washington WA, west_virginia WV, wisconsin WI, wyoming WY
        """.trimIndent().split(",").map { it.trim().split(" ") }

        assertEquals(50, states.size)
        states.forEach { (state, code) ->
            assertEquals(state, code, deriveTileCountryCode(country("usa_$state")))
            assertEquals(state, code, deriveTileCountryCode(country("usa___$state")))
        }
    }

    @Test
    fun parsedDatasetStateNamesUseStateCodes() {
        val countriesJson = """
            {
              "USA - California": {
                "financial_authority": {
                  "authorityId": "usa_california__dfpi",
                  "abbreviation": "DFPI"
                }
              }
            }
        """.trimIndent()
        val catalog = JsonParsers.parseCatalog(
            countriesJson,
            """{"institutions":[]}""",
            """{"categories":[]}""",
            """{"institutionTypes":[]}""",
            """{"strings":{}}"""
        ).getOrThrow()
        val california = catalog.countries.single()

        assertEquals("usa___california", california.countryKey)
        assertEquals("CA", deriveTileCountryCode(california))
        assertEquals("DFPI", california.authority.abbreviation)
    }

    @Test
    fun otherCountriesKeepExistingCodes() {
        mapOf(
            "italy" to "ITA",
            "united_kingdom" to "UK",
            "canada" to "CAN",
            "france" to "FRA",
            "a_b" to "AB",
            "" to ""
        ).forEach { (key, code) ->
            assertEquals(key, code, deriveTileCountryCode(country(key)))
        }
    }

    @Test
    fun unknownUsaKeysKeepSafeFallback() {
        listOf("usa_unknown", "usa_", "usa", "usa_new_york_extra").forEach { key ->
            assertEquals(key, "USA", deriveTileCountryCode(country(key)))
        }
    }

    private fun country(key: String) = Country(
        countryKey = key,
        countryName = key,
        flag = "🇺🇸",
        isEU = false,
        protectionLevel = "Unknown",
        notes = null,
        authority = FinancialAuthority(
            authorityId = "${key}__authority",
            name = "Authority",
            abbreviation = "SEC",
            homepage = null,
            fraudReportLink = null,
            authorityEmail = null,
            relatedInternationalInstitutionIds = emptySet()
        )
    )
}
