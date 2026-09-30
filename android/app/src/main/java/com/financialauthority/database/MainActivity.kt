package com.financialauthority.database

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.financialauthority.database.navigation.MainScaffold
import com.financialauthority.database.ui.components.UiText
import com.financialauthority.database.ui.theme.FinancialAuthorityTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancialAuthorityTheme {
                AppRoot(vm)
            }
        }
    }
}

@Composable
private fun AppRoot(vm: MainViewModel) {
    val state by vm.catalogState.collectAsState()
    val favorites by vm.favorites.collectAsState()
    val language by vm.language.collectAsState()

    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // 2 secondi fissi per imprimere il brand nella mente dell'utente
        showSplash = false
    }

    if (showSplash) {
        BrandSplashScreen(language = language)
    } else {
        when (val current = state) {
            CatalogUiState.Loading -> CenterMessage(UiText.get(language, "loading"))
            is CatalogUiState.Error -> CenterError(message = current.message, language = language, onRetry = vm::loadData)
            is CatalogUiState.Ready -> MainScaffold(
                vm = vm,
                catalog = current.catalog,
                favorites = favorites,
                language = language
            )
        }
    }
}

@Composable
fun BrandSplashScreen(language: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_shield_icon),
                contentDescription = "Brand Shield Icon",
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(28.dp))
            )
            Text(
                text = "Financial Authority Database",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 20.dp)
            )
            Text(
                text = if (language == "it") "Protezione & Sicurezza Finanziaria" else "Financial Protection & Security",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun CenterMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun CenterError(message: String, language: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(UiText.get(language, "error"), style = MaterialTheme.typography.titleLarge)
        Text(message, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
        Button(onClick = onRetry) { Text(UiText.get(language, "retry")) }
    }
}
