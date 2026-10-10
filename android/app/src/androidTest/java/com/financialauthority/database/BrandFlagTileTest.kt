package com.financialauthority.database

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.financialauthority.database.domain.Country
import com.financialauthority.database.domain.FinancialAuthority
import com.financialauthority.database.ui.countries.BrandFlagTile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

class BrandFlagTileTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun sixColumnsAt320Dp() = verifyGrid(320, 1f)

    @Test
    fun sixColumnsAt360Dp() = verifyGrid(360, 1f)

    @Test
    fun sixColumnsAt320DpWithLargerFonts() = verifyGrid(320, 1.3f)

    @Test
    fun sixColumnsAt360DpWithLargerFonts() = verifyGrid(360, 1.3f)

    private fun verifyGrid(width: Int, fontScale: Float) {
        val entries = listOf(
            Triple("usa___alabama", "AL", "ASC"),
            Triple("usa___california", "CA", "DFPI"),
            Triple("usa___new_york", "NY", "NYDFS"),
            Triple("usa___north_carolina", "NC", "NCS"),
            Triple("usa___west_virginia", "WV", "WVSC"),
            Triple("united_states_of_america", "USA", "SEC")
        )
        val countries = entries.map { (key, _, abbreviation) ->
            Country(
                countryKey = key,
                countryName = key,
                flag = "🇺🇸",
                isEU = false,
                protectionLevel = "Unknown",
                notes = null,
                authority = FinancialAuthority(
                    authorityId = "${key}__authority",
                    name = "Authority",
                    abbreviation = abbreviation,
                    homepage = null,
                    fraudReportLink = null,
                    authorityEmail = null,
                    relatedInternationalInstitutionIds = emptySet()
                )
            )
        }
        var openedKey: String? = null
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalDensity provides Density(LocalDensity.current.density, fontScale)
            ) {
                MaterialTheme {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(6),
                        modifier = Modifier.requiredWidth(width.dp).padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(countries) { country ->
                            BrandFlagTile(country) { openedKey = country.countryKey }
                        }
                    }
                }
            }
        }
        entries.forEach { (key, code, abbreviation) ->
            val layouts = mutableListOf<TextLayoutResult>()
            composeTestRule.onNodeWithText(code, useUnmergedTree = true)
                .assertIsDisplayed()
                .performSemanticsAction(SemanticsActions.GetTextLayoutResult) { it(layouts) }
            assertEquals(1, layouts.size)
            assertEquals(1, layouts.single().lineCount)
            assertFalse("$code must not be truncated", layouts.single().isLineEllipsized(0))
            composeTestRule.onNodeWithText(abbreviation, useUnmergedTree = true).assertExists()
            composeTestRule.onNodeWithContentDescription("$key $abbreviation").performClick()
            composeTestRule.runOnIdle { assertEquals(key, openedKey) }
        }
    }
}
