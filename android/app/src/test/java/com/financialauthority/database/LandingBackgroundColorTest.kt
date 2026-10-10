package com.financialauthority.database

import androidx.compose.ui.graphics.Color
import com.financialauthority.database.ui.theme.landingBackgroundColor
import org.junit.Assert.assertEquals
import org.junit.Test

class LandingBackgroundColorTest {
    @Test
    fun lightLandingUsesDesktopPurpleTintRegardlessOfThemeBackground() {
        for (background in listOf(Color(0xFFF5F8FD), Color.Green)) {
            assertEquals(Color(0xFFF1EDF6), landingBackgroundColor(false, background))
        }
    }

    @Test
    fun darkLandingPreservesThemeBackground() {
        val background = Color(0xFF101A2A)
        assertEquals(background, landingBackgroundColor(true, background))
    }
}
