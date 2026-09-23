package com.financialauthority.database.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financialauthority.database.domain.SearchResult
import com.financialauthority.database.ui.components.ItemCard

@Composable
fun SearchScreen(
    resultsProvider: (String) -> List<SearchResult>,
    onOpenResult: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) { resultsProvider(query) }

    Column(modifier = modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Cerca (FATF, UIF, Italia, money laundering, Asia)") },
            modifier = Modifier.fillMaxWidth()
        )

        if (results.isEmpty()) {
            Text("Nessun risultato", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(results) { item ->
                    ItemCard(title = item.title, subtitle = item.subtitle, onClick = { onOpenResult(item) })
                }
            }
        }
    }
}
