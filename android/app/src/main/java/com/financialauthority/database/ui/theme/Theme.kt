package com.financialauthority.database.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFF7AA2FF),
    secondary = Color(0xFF9CCBFF),
    background = Color(0xFF101A2A),
    surface = Color(0xFF14253B)
)

private val LightScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF0B4EA2),
    secondary = Color(0xFF1D6ED6),
    background = Color(0xFFF5F8FD),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun FinancialAuthorityTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkScheme else LightScheme,
        content = content
    )
}
