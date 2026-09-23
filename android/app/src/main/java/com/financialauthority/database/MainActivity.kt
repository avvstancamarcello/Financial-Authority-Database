package com.financialauthority.database

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financialauthority.database.navigation.MainScaffold
import com.financialauthority.database.ui.components.UiText
import com.financialauthority.database.ui.theme.FinancialAuthorityTheme

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
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

@Composable
private fun CenterMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
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
