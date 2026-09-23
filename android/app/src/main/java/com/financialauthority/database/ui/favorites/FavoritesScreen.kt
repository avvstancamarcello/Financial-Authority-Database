package com.financialauthority.database.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financialauthority.database.domain.SearchResult
import com.financialauthority.database.ui.components.ItemCard

@Composable
fun FavoritesScreen(
    favorites: List<SearchResult>,
    onOpen: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("Preferiti", style = MaterialTheme.typography.headlineSmall) }
        if (favorites.isEmpty()) {
            item { Text("Nessun preferito salvato") }
        }
        items(favorites) { fav ->
            ItemCard(title = fav.title, subtitle = fav.subtitle, onClick = { onOpen(fav) }, favoriteMark = true)
        }
    }
}
