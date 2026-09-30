package com.financialauthority.database.ui.countries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financialauthority.database.domain.Country
import com.financialauthority.database.domain.FinancialAuthority
import com.financialauthority.database.ui.about.AmevMatrixCard
import com.financialauthority.database.ui.components.ItemCard
import com.financialauthority.database.ui.components.UiText

private val stopWords = setOf(
    // Inglese
    "and", "of", "the", "for", "in", "on", "at", "to", "by", "with", "from",
    // Italiano
    "e", "ed", "del", "della", "delle", "dello", "dei", "degli", "dell",
    "de", "di", "da", "do", "du", "des", "per", "su", "con", "tra", "fra",
    // Tedesco, Spagnolo, Francese, Olandese
    "und", "der", "die", "das", "von", "vom", "zu", "zur", "la", "las", "los", "el", "les", "van", "het", "en"
)

private val genericWords = setOf(
    // Termini istituzionali generici
    "authority", "commission", "national", "department", "office", "board",
    "bureau", "division", "autorita", "commissione", "service", "services",
    "servizi", "servicios", "general", "generale", "central", "centrale",
    "federal", "federale", "public", "pubblica", "financial", "finanziaria",
    "financier", "financieros", "supervisory", "supervision", "supervisione",
    "regulatory", "regolamentazione", "regulator", "republic", "repubblica"
)

private fun getSubstantiveWords(text: String): List<String> {
    return text.lowercase()
        .split(" ", "-", "_", "/", "(", ")", ",", ".")
        .map { it.trim() }
        .filter { it.isNotBlank() && it !in stopWords }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CountriesScreen(
    countries: List<Country>,
    favorites: Set<String>,
    starVotes: Map<Int, Int>,
    onVoteStar: (Int) -> Unit,
    modifier: Modifier = Modifier,
    language: String = "it",
    onOpen: (Country) -> Unit,
    onOpenUrl: (String) -> Unit
) {
    var countryQuery by remember { mutableStateOf("") }
    var authorityQuery by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var showCountryDropdown by remember { mutableStateOf(false) }
    var showAuthorityDropdown by remember { mutableStateOf(false) }

    var regionFilter by remember { mutableStateOf("All") }
    var showRatingDialog by remember { mutableStateOf(false) }
    var showAmevMatrix by remember { mutableStateOf(false) }
    var grid by remember { mutableStateOf(true) }

    // Rating Dialog with counters
    if (showRatingDialog) {
        AlertDialog(
            onDismissRequest = { showRatingDialog = false },
            title = { Text(if (language == "it") "⭐ Vota con Like" else "⭐ Rate with Like") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(if (language == "it") "Valuta l'utilità di questa App (da 1 a 5 stelle):" else "Rate the usefulness of this App (1 to 5 stars):")
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..5).forEach { star ->
                            val count = starVotes[star] ?: 0
                            Button(
                                onClick = { onVoteStar(star) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("⭐".repeat(star))
                                    Text("($count)")
                                }
                            }
                        }
                    }
                    val totalVotes = starVotes.values.sum()
                    Text(
                        text = if (language == "it") "Totale voti registrati: $totalVotes" else "Total votes registered: $totalVotes",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showRatingDialog = false }) {
                    Text(if (language == "it") "Chiudi" else "Close")
                }
            }
        )
    }

    val matchingCountries = remember(countryQuery, countries) {
        val q = countryQuery.trim().lowercase()
        if (q.length < 2 || q in stopWords) emptyList()
        else countries.filter { country ->
            val name = country.countryName.lowercase()
            val key = country.countryKey.lowercase()
            if (q == "uk" || q == "gb") key.contains("united_kingdom") || name.contains("united kingdom")
            else if (q == "ua" || q == "uc") key.contains("ukraine") || name.contains("ucraina")
            else if (q == "us" || q == "usa") key.startsWith("usa") || name.startsWith("usa")
            else if (q == "ca" || q == "can" || q == "canada") key.startsWith("canada") || name.startsWith("canada")
            else {
                name.startsWith(q) || key.startsWith(q) ||
                    getSubstantiveWords(country.countryName).any { it.startsWith(q) }
            }
        }.sortedBy { it.countryName }
    }

    val matchingAuthorities = remember(authorityQuery, countries) {
        val q = authorityQuery.trim().lowercase()
        if (q.length < 3 || q in stopWords) emptyList()
        else countries.filter { country ->
            val authName = country.authority.name.lowercase()
            val abbr = country.authority.abbreviation.orEmpty().lowercase()
            val cName = country.countryName.lowercase()
            abbr == q || abbr.startsWith(q) || abbr.contains(q) ||
                authName.startsWith(q) || authName.contains(q) ||
                cName.startsWith(q) || cName.contains(q) ||
                getSubstantiveWords(country.authority.name).any { word ->
                    word !in genericWords && word.contains(q)
                }
        }.sortedWith(
            compareByDescending<Country> { country ->
                val abbr = country.authority.abbreviation.orEmpty().lowercase()
                val authName = country.authority.name.lowercase()
                if (abbr == q || abbr.startsWith(q)) 1000
                else if (authName.startsWith(q)) 800
                else if (authName.contains(q)) 600
                else 300
            }.thenBy { it.countryName }
        )
    }

    val filtered = remember(selectedCountry, countryQuery, authorityQuery, regionFilter, countries) {
        val baseList = when {
            selectedCountry != null -> listOf(selectedCountry!!)
            countryQuery.trim().length >= 2 && authorityQuery.isBlank() -> matchingCountries
            authorityQuery.trim().length >= 3 && countryQuery.isBlank() -> matchingAuthorities
            countryQuery.trim().length >= 2 && authorityQuery.trim().length >= 3 -> {
                countries.filter { country ->
                    val cName = country.countryName.lowercase()
                    val aName = country.authority.name.lowercase()
                    val aAbbr = country.authority.abbreviation.orEmpty().lowercase()
                    cName.contains(countryQuery.trim(), true) && (aName.contains(authorityQuery.trim(), true) || aAbbr.contains(authorityQuery.trim(), true))
                }
            }
            else -> countries
        }

        baseList.filter { country ->
            when (regionFilter) {
                "EU" -> country.isEU
                "Non-EU" -> !country.isEU
                else -> true
            }
        }
    }

    fun clearAllSearches() {
        countryQuery = ""
        authorityQuery = ""
        selectedCountry = null
        showCountryDropdown = false
        showAuthorityDropdown = false
    }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
        // CTA Subtitle & AMEV Matrix Toggle
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = if (language == "it") "Trova e contatta qualsiasi Financial Authority!" else "Find and contact any Financial Authority!",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            AssistChip(
                onClick = { showAmevMatrix = !showAmevMatrix },
                label = {
                    val amevText = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF00FF66), fontWeight = FontWeight.ExtraBold)) {
                            append("AMEV")
                        }
                        append(if (language == "it") " Matrice Acronimo (9 Lingue UE)" else " Acronym Matrix (9 EU Languages)")
                    }
                    Text(amevText)
                }
            )
            if (showAmevMatrix) {
                AmevMatrixCard(
                    language = language,
                    onClose = { showAmevMatrix = false }
                )
            }
        }

        // Search Boxes Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Casella 1: Ricerca Paese
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = countryQuery,
                    onValueChange = { newValue ->
                        countryQuery = newValue
                        selectedCountry = null
                        showCountryDropdown = newValue.trim().length >= 2 && newValue.trim().lowercase() !in stopWords
                    },
                    label = { Text(if (language == "it") "Paese" else "Country") },
                    trailingIcon = {
                        if (countryQuery.isNotBlank()) {
                            IconButton(onClick = {
                                countryQuery = ""
                                if (authorityQuery.isBlank()) selectedCountry = null
                                showCountryDropdown = false
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear country")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showCountryDropdown && matchingCountries.isNotEmpty() && countryQuery.trim().length >= 2) {
                    DropdownMenu(
                        expanded = showCountryDropdown,
                        onDismissRequest = { showCountryDropdown = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(max = 420.dp)
                    ) {
                        matchingCountries.take(60).forEach { country ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(country.flag, style = MaterialTheme.typography.titleMedium)
                                        Text(country.countryName, fontWeight = FontWeight.Bold)
                                    }
                                },
                                onClick = {
                                    selectedCountry = country
                                    countryQuery = country.countryName
                                    authorityQuery = country.authority.abbreviation?.let { "$it - " }.orEmpty() + country.authority.name
                                    showCountryDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Casella 2: Ricerca Authority
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = authorityQuery,
                    onValueChange = { newValue ->
                        authorityQuery = newValue
                        selectedCountry = null
                        showAuthorityDropdown = newValue.trim().length >= 3 && newValue.trim().lowercase() !in stopWords
                    },
                    label = { Text("Authority") },
                    trailingIcon = {
                        if (authorityQuery.isNotBlank()) {
                            IconButton(onClick = {
                                authorityQuery = ""
                                if (countryQuery.isBlank()) selectedCountry = null
                                showAuthorityDropdown = false
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear authority")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showAuthorityDropdown && matchingAuthorities.isNotEmpty() && authorityQuery.trim().length >= 3) {
                    DropdownMenu(
                        expanded = showAuthorityDropdown,
                        onDismissRequest = { showAuthorityDropdown = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(max = 420.dp)
                    ) {
                        matchingAuthorities.take(60).forEach { country ->
                            val siglaText = country.authority.abbreviation?.takeIf { it.isNotBlank() }
                                ?: country.authority.name.take(18)
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(country.flag, style = MaterialTheme.typography.titleMedium)
                                        Text(
                                            text = siglaText,
                                            fontWeight = FontWeight.ExtraBold,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "(${country.countryName})",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    selectedCountry = country
                                    countryQuery = country.countryName
                                    authorityQuery = country.authority.abbreviation?.let { "$it - " }.orEmpty() + country.authority.name
                                    showAuthorityDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }

        if (countryQuery.isNotBlank() || authorityQuery.isNotBlank()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AssistChip(
                    onClick = { clearAllSearches() },
                    label = { Text(if (language == "it") "Reset Ricerca ✕" else "Reset Search ✕") }
                )
            }
        }

        // Scrolling Prompt & Indicator (Right below Authority box)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == "it") "Elenco Bandiere in ordine alfabetico" else "Alphabetical Flag List",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "⬇️ Scroll",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // RIGA 1: Filtri Regione
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = regionFilter == "All",
                onClick = { regionFilter = "All" },
                label = { Text(if (language == "it") "Tutti" else "All") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF3B82F6).copy(alpha = 0.3f))
            )
            FilterChip(
                selected = regionFilter == "EU",
                onClick = { regionFilter = "EU" },
                label = { Text("EU") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF10B981).copy(alpha = 0.3f))
            )
            FilterChip(
                selected = regionFilter == "Non-EU",
                onClick = { regionFilter = "Non-EU" },
                label = { Text("Non-EU") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFF59E0B).copy(alpha = 0.3f))
            )
        }

        // RIGA 2: Vota con Like & NEWS by Authorities
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssistChip(
                onClick = { showRatingDialog = true },
                label = { Text("⭐ Vota con Like") }
            )
            AssistChip(
                onClick = { onOpenUrl("https://www.amevfirenze.it/news.html") },
                label = { Text("✨ NEWS by Authorities") }
            )
        }

        // RIGA 3: Change View & Scroll
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable { grid = !grid }
                ) {
                    Text(
                        text = "change view",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { grid = !grid }) {
                        Icon(
                            imageVector = if (grid) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                            contentDescription = UiText.get(language, if (grid) "list_view" else "grid_view"),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    text = "⬇️ Scroll",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (grid) {
            // Grid a 6 Caselle Quadrate con Codice Paese SOPRA la Flag e Sigla SOTTO
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                contentPadding = PaddingValues(2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filtered) { country ->
                    BrandFlagTile(
                        country = country,
                        onClick = { onOpen(country) }
                    )
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered) { country ->
                    val subtitleText = country.authority.abbreviation?.let { "$it - " }.orEmpty() + country.authority.name + " • " + country.protectionLevel
                    ItemCard(
                        title = "${country.flag} ${country.countryName}",
                        subtitle = subtitleText,
                        favoriteMark = favorites.contains("country:${country.countryKey}"),
                        onClick = { onOpen(country) }
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            Text(UiText.get(language, "no_countries"), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun BrandFlagTile(
    country: Country,
    onClick: () -> Unit
) {
    val countryCode = when {
        country.countryKey.contains("united_kingdom") -> "UK"
        country.countryKey.contains("united_states") || country.countryKey.startsWith("usa") -> "USA"
        country.countryKey.contains("canada") -> "CAN"
        else -> country.countryKey.take(3).uppercase().replace("_", "")
    }

    val siglaText = country.authority.abbreviation?.takeIf { it.isNotBlank() }
        ?: country.countryName.take(3).uppercase()

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .semantics { contentDescription = "${country.countryName} $siglaText" },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = countryCode,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = country.flag,
                fontSize = 20.sp
            )
            Text(
                text = siglaText,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CountryDetailScreen(
    country: Country,
    institutionNames: List<Pair<String, String>>,
    isFavorite: Boolean,
    language: String = "it",
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
        item { AuthorityTitleHeader(country.authority) }
        item { Text("${UiText.get(language, "protection_level")}: ${country.protectionLevel}") }
        country.authority.homepage?.takeIf { it.isNotBlank() }?.let { home ->
            item { AssistChip(onClick = { onOpenUrl(home) }, label = { Text(UiText.get(language, "official_homepage")) }) }
        }
        country.authority.fraudReportLink?.takeIf { it.isNotBlank() }?.let { report ->
            item { AssistChip(onClick = { onOpenUrl(report) }, label = { Text(UiText.get(language, "complaints_reports")) }) }
        }
        country.authority.playStoreUrl?.takeIf { it.isNotBlank() }?.let { playUrl ->
            item { AssistChip(onClick = { onOpenUrl(playUrl) }, label = { Text("📱 " + UiText.get(language, "download_app")) }) }
        }
        country.authority.webAppUrl?.takeIf { it.isNotBlank() }?.let { webAppUrl ->
            item { AssistChip(onClick = { onOpenUrl(webAppUrl) }, label = { Text("🌐 " + UiText.get(language, "open_orbital_webapp")) }) }
        }
        country.authority.mapsUrl?.takeIf { it.isNotBlank() }?.let { mapsUrl ->
            item { AssistChip(onClick = { onOpenUrl(mapsUrl) }, label = { Text("📍 " + mapsUrl) }) }
        }
        country.authority.authorityEmail?.takeIf { it.isNotBlank() }?.let { email ->
            item { Text("${UiText.get(language, "email")}: $email") }
        }
        item {
            AssistChip(
                onClick = onToggleFavorite,
                label = { Text(UiText.get(language, if (isFavorite) "remove_favorite" else "add_favorite")) }
            )
        }

        val socialKeys = setOf("facebook", "linkedin", "instagram", "tiktok", "twitter", "youtube")
        val (socialMediaLinks, resourceLinks) = country.authority.socialLinks.entries.partition { it.key.lowercase() in socialKeys }

        if (resourceLinks.isNotEmpty()) {
            item { Text(UiText.get(language, "additional_resources"), style = MaterialTheme.typography.titleMedium) }
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    resourceLinks.forEach { (key, url) ->
                        val labelKey = "link_$key"
                        val labelText = UiText.get(language, labelKey).takeIf { it != labelKey }
                            ?: key.replace("_", " ").split(" ").joinToString(" ") { char -> char.replaceFirstChar { c -> c.uppercase() } }
                        AssistChip(
                            onClick = { onOpenUrl(url) },
                            label = { Text(labelText) }
                        )
                    }
                }
            }
        }

        if (socialMediaLinks.isNotEmpty()) {
            item { Text(UiText.get(language, "social_networks"), style = MaterialTheme.typography.titleMedium) }
            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    socialMediaLinks.forEach { (key, url) ->
                        val labelKey = "link_$key"
                        val labelText = UiText.get(language, labelKey).takeIf { it != labelKey }
                            ?: key.replace("_", " ").split(" ").joinToString(" ") { char -> char.replaceFirstChar { c -> c.uppercase() } }
                        AssistChip(
                            onClick = { onOpenUrl(url) },
                            label = { Text("🔗 $labelText") }
                        )
                    }
                }
            }
        }

        country.notes?.takeIf { it.isNotBlank() }?.let { rawNotes ->
            val cleanNotes = rawNotes.replace(Regex("https?://\\S+"), "")
                .lines()
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .joinToString("\n")
            if (cleanNotes.isNotBlank()) {
                item { Text("${UiText.get(language, "notes")}:\n$cleanNotes") }
            }
        }
        if (institutionNames.isNotEmpty()) {
            item { Text(UiText.get(language, "international_links"), style = MaterialTheme.typography.titleMedium) }
            items(institutionNames.size) { index ->
                val pair = institutionNames[index]
                AssistChip(onClick = { onOpenInstitution(pair.first) }, label = { Text(pair.second) })
            }
        }
    }
}

@Composable
private fun AuthorityTitleHeader(authority: FinancialAuthority) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        authority.abbreviation?.takeIf { it.isNotBlank() }?.let { abbr ->
            if (abbr.equals("CONSOB", ignoreCase = true)) {
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    val consobText = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF00A859))) { append("C") }
                        withStyle(SpanStyle(color = Color(0xFFFFFFFF))) { append("O") }
                        withStyle(SpanStyle(color = Color(0xFFE53935))) { append("N") }
                        withStyle(SpanStyle(color = Color(0xFF00A859))) { append("S") }
                        withStyle(SpanStyle(color = Color(0xFFFFFFFF))) { append("O") }
                        withStyle(SpanStyle(color = Color(0xFFE53935))) { append("B") }
                    }
                    Text(
                        text = consobText,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 3.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            } else {
                Text(
                    text = abbr,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Text(
            text = authority.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
