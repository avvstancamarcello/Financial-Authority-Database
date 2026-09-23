package com.financialauthority.database.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financialauthority.database.ui.components.ItemCard

@Composable
fun AboutScreen(
    entries: List<Pair<String, String>>,
    onOpenEntry: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("Informazioni", style = MaterialTheme.typography.headlineSmall) }
        item {
            Text("Servizio informativo offline-first su autorità finanziarie nazionali e internazionali.")
        }
        items(entries) { pair ->
            ItemCard(title = pair.second, subtitle = pair.first, onClick = { onOpenEntry(pair.first) })
        }
    }
}

@Composable
fun LegalMarkdownScreen(
    title: String,
    locale: String,
    loadText: suspend (docId: String, locale: String) -> String,
    docId: String,
    modifier: Modifier = Modifier
) {
    var content by remember { mutableStateOf("Loading...") }

    LaunchedEffect(locale, docId) {
        content = loadText(docId, locale)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text(title, style = MaterialTheme.typography.headlineSmall) }
        item { Text(content) }
    }
}

@Composable
fun ContactScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text("Contatti", style = MaterialTheme.typography.headlineSmall) }
        item { Text("Per aggiornamenti dataset e segnalazioni ufficiali usare sempre i siti istituzionali elencati.") }
        item { Text("Email progetto web: vedi repository ufficiale") }
    }
}
