package com.financialauthority.database.ui.international

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financialauthority.database.data.resolve
import com.financialauthority.database.domain.AppCatalog
import com.financialauthority.database.domain.Institution
import com.financialauthority.database.ui.components.BadgeRow
import com.financialauthority.database.ui.components.ItemCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InternationalInstitutionsScreen(
    catalog: AppCatalog,
    locale: String,
    favorites: Set<String>,
    onOpen: (Institution) -> Unit,
    modifier: Modifier = Modifier
) {
    var level by remember { mutableStateOf("all") }
    var region by remember { mutableStateOf("all") }
    val filtered = catalog.institutions.filter {
        (level == "all" || it.level == level) &&
            (region == "all" || it.region == region)
    }

    Column(modifier = modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Autorità Internazionali", style = MaterialTheme.typography.headlineSmall)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("all", "global", "supranational", "regional", "national").forEach {
                AssistChip(onClick = { level = it }, label = { Text(it) })
            }
            listOf("all", "worldwide", "europe", "americas", "africa", "asia_pacific").forEach {
                AssistChip(onClick = { region = it }, label = { Text(it) })
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { institution ->
                val categoryLabel = catalog.i18nStrings.resolve("category.${institution.category}.label", locale, institution.category)
                ItemCard(
                    title = institution.shortName ?: institution.name,
                    subtitle = "$categoryLabel • ${institution.region}",
                    favoriteMark = favorites.contains("institution:${institution.id}"),
                    onClick = { onOpen(institution) }
                )
            }
        }
    }
}

@Composable
fun InstitutionDetailScreen(
    institution: Institution,
    catalog: AppCatalog,
    locale: String,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenUrl: (String) -> Unit,
    onOpenInstitution: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val description = institution.descriptionKey?.let {
        catalog.i18nStrings.resolve(it, locale, institution.name)
    } ?: institution.name

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { Text(institution.shortName ?: institution.name, style = MaterialTheme.typography.headlineSmall) }
        item { Text(institution.name, style = MaterialTheme.typography.titleMedium) }
        item { Text(description) }
        item {
            BadgeRow(
                values = listOf(institution.level, institution.region, institution.category, institution.institutionType),
                contentDescription = "Badge livello regione categoria tipo"
            )
        }
        item {
            AssistChip(
                onClick = onToggleFavorite,
                label = { Text(if (isFavorite) "Rimuovi preferito" else "Aggiungi preferito") }
            )
        }
        item {
            Text("Poteri e limiti", style = MaterialTheme.typography.titleMedium)
            institution.canPowers.forEach { Text("• $it") }
            institution.cannotPowers.forEach { Text("• NON: $it") }
        }
        if (institution.id == "fatf_gafi") {
            item {
                Text(
                    "FATF/GAFI: standard-setting e valutazioni reciproche; nessun potere investigativo, di sequestro o legislativo diretto.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        if (institution.id == "egmont_group") {
            item { Text("Egmont Group: rete globale di cooperazione tra FIU.") }
        }
        if (institution.id == "it_uif") {
            item { Text("UIF: FIU nazionale italiana, non autorità globale unica.") }
        }

        item { Text("Link ufficiali", style = MaterialTheme.typography.titleMedium) }
        listOfNotNull(
            institution.homepage,
            institution.mandateUrl,
            institution.recommendationsUrl,
            institution.listsUrl,
            institution.reportsUrl,
            institution.alertsUrl,
            institution.aboutUrl,
            institution.contactsUrl,
            institution.membersUrl
        ).distinct().forEach { url ->
            item { AssistChip(onClick = { onOpenUrl(url) }, label = { Text(url) }) }
        }

        if (institution.relatedInstitutionIds.isNotEmpty()) {
            item { Text("Istituzioni collegate", style = MaterialTheme.typography.titleMedium) }
            items(institution.relatedInstitutionIds.toList()) { relatedId ->
                AssistChip(onClick = { onOpenInstitution(relatedId) }, label = { Text(relatedId) })
            }
        }
    }
}

@Composable
fun FSRBScreen(
    institutions: List<Institution>,
    onOpen: (Institution) -> Unit,
    modifier: Modifier = Modifier
) {
    val fsrbs = institutions.filter { it.institutionType == "regional_fatf_style_body" }
    SimpleInstitutionList(title = "FSRB", institutions = fsrbs, onOpen = onOpen, modifier = modifier)
}

@Composable
fun FIUScreen(
    institutions: List<Institution>,
    onOpen: (Institution) -> Unit,
    modifier: Modifier = Modifier
) {
    val fius = institutions.filter { it.institutionType.contains("fiu") || it.id == "egmont_group" }
    SimpleInstitutionList(title = "FIU", institutions = fius, onOpen = onOpen, modifier = modifier)
}

@Composable
private fun SimpleInstitutionList(
    title: String,
    institutions: List<Institution>,
    onOpen: (Institution) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text(title, style = MaterialTheme.typography.headlineSmall) }
        items(institutions) { institution ->
            ItemCard(
                title = institution.shortName ?: institution.name,
                subtitle = institution.name,
                onClick = { onOpen(institution) }
            )
        }
    }
}
