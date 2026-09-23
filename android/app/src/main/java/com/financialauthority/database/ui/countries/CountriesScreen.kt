package com.financialauthority.database.ui.countries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financialauthority.database.domain.Country
import com.financialauthority.database.ui.components.ItemCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CountriesScreen(
    countries: List<Country>,
    favorites: Set<String>,
    onOpen: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var regionFilter by remember { mutableStateOf("All") }
    var protectionFilter by remember { mutableStateOf("All") }
    var grid by remember { mutableStateOf(false) }

    val protections = remember(countries) { listOf("All") + countries.map { it.protectionLevel }.distinct().sorted() }
    val filtered = countries.filter {
        (query.isBlank() || it.countryName.contains(query, true) || it.authority.name.contains(query, true)) &&
            (regionFilter == "All" || (regionFilter == "EU" && it.isEU) || (regionFilter == "Non-EU" && !it.isEU)) &&
            (protectionFilter == "All" || it.protectionLevel == protectionFilter)
    }

    Column(modifier = modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Ricerca paese/autoritá") },
            modifier = Modifier.fillMaxWidth()
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("All", "EU", "Non-EU").forEach { region ->
                AssistChip(onClick = { regionFilter = region }, label = { Text(region) })
            }
            protections.forEach { level ->
                AssistChip(onClick = { protectionFilter = level }, label = { Text(level) })
            }
            IconButton(onClick = { grid = !grid }) {
                Icon(
                    imageVector = if (grid) Icons.Default.List else Icons.Default.GridView,
                    contentDescription = if (grid) "Vista lista" else "Vista griglia"
                )
            }
        }
        if (grid) {
            LazyVerticalGrid(columns = GridCells.Adaptive(220.dp), contentPadding = PaddingValues(4.dp)) {
                items(filtered) { country ->
                    ItemCard(
                        title = "${country.flag} ${country.countryName}",
                        subtitle = country.authority.name,
                        favoriteMark = favorites.contains("country:${country.countryKey}"),
                        onClick = { onOpen(country) }
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered) { country ->
                    ItemCard(
                        title = "${country.flag} ${country.countryName}",
                        subtitle = "${country.authority.name} • ${country.protectionLevel}",
                        favoriteMark = favorites.contains("country:${country.countryKey}"),
                        onClick = { onOpen(country) }
                    )
                }
            }
        }
        if (filtered.isEmpty()) {
            Text("Nessun paese trovato", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CountryDetailScreen(
    country: Country,
    institutionNames: List<Pair<String, String>>,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenInstitution: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("${country.flag} ${country.countryName}", style = MaterialTheme.typography.headlineSmall) }
        item { Text(country.authority.name, style = MaterialTheme.typography.titleLarge) }
        item { Text("Protezione: ${country.protectionLevel}") }
        country.authority.homepage?.takeIf { it.isNotBlank() }?.let { home ->
            item { AssistChip(onClick = { onOpenUrl(home) }, label = { Text("Homepage ufficiale") }) }
        }
        country.authority.fraudReportLink?.takeIf { it.isNotBlank() }?.let { report ->
            item { AssistChip(onClick = { onOpenUrl(report) }, label = { Text("Reclami/Segnalazioni") }) }
        }
        country.authority.authorityEmail?.takeIf { it.isNotBlank() }?.let { email ->
            item { Text("Email: $email") }
        }
        item {
            AssistChip(
                onClick = onToggleFavorite,
                label = { Text(if (isFavorite) "Rimuovi preferito" else "Aggiungi preferito") }
            )
        }
        country.notes?.takeIf { it.isNotBlank() }?.let { notes ->
            item { Text("Note: $notes") }
        }
        if (institutionNames.isNotEmpty()) {
            item { Text("Collegamenti internazionali", style = MaterialTheme.typography.titleMedium) }
            items(institutionNames.size) { index ->
                val pair = institutionNames[index]
                AssistChip(onClick = { onOpenInstitution(pair.first) }, label = { Text(pair.second) })
            }
        }
    }
}
