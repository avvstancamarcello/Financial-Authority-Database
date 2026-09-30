package com.financialauthority.database.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.financialauthority.database.MainViewModel
import com.financialauthority.database.data.resolve
import com.financialauthority.database.domain.AppCatalog
import com.financialauthority.database.domain.Country
import com.financialauthority.database.domain.SearchResult
import com.financialauthority.database.ui.about.AboutScreen
import com.financialauthority.database.ui.about.ContactScreen
import com.financialauthority.database.ui.about.LegalMarkdownScreen
import com.financialauthority.database.ui.components.UiText
import com.financialauthority.database.ui.countries.CountriesScreen
import com.financialauthority.database.ui.countries.CountryDetailScreen
import com.financialauthority.database.ui.favorites.FavoritesScreen
import com.financialauthority.database.ui.international.FIUScreen
import com.financialauthority.database.ui.international.FSRBScreen
import com.financialauthority.database.ui.international.InstitutionDetailScreen
import com.financialauthority.database.ui.international.InternationalInstitutionsScreen
import com.financialauthority.database.ui.search.SearchScreen

private data class BottomItem(val route: String, val label: String, val icon: @Composable () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    vm: MainViewModel,
    catalog: AppCatalog,
    favorites: Set<String>,
    language: String
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val context = LocalContext.current
    var langMenuOpen by remember { mutableStateOf(false) }

    val bottomItems = listOf(
        BottomItem(Routes.COUNTRIES, UiText.get(language, "countries"), { Icon(Icons.Default.Public, contentDescription = UiText.get(language, "countries")) }),
        BottomItem(Routes.INTERNATIONAL, UiText.get(language, "international"), { Icon(Icons.Default.Language, contentDescription = UiText.get(language, "international")) }),
        BottomItem(Routes.SEARCH, UiText.get(language, "search"), { Icon(Icons.Default.Search, contentDescription = UiText.get(language, "search")) }),
        BottomItem(Routes.FAVORITES, UiText.get(language, "favorites"), { Icon(Icons.Default.Favorite, contentDescription = UiText.get(language, "favorites")) }),
        BottomItem(Routes.ABOUT, UiText.get(language, "about"), { Icon(Icons.Default.Info, contentDescription = UiText.get(language, "about")) })
    )

    val openUrl: (String) -> Unit = { url ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Authority Database") },
                actions = {
                    TextButton(onClick = { langMenuOpen = true }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (language == "it") "🇮🇹 IT | 🇬🇧 EN" else "🇬🇧 EN | 🇮🇹 IT",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Icon(
                                imageVector = Icons.Default.TravelExplore,
                                contentDescription = UiText.get(language, "language_selector")
                            )
                        }
                    }
                    DropdownMenu(expanded = langMenuOpen, onDismissRequest = { langMenuOpen = false }) {
                        DropdownMenuItem(
                            text = { Text("🇮🇹 Italiano (IT)") },
                            onClick = {
                                vm.setLanguage("it")
                                langMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🇬🇧 English (EN)") },
                            onClick = {
                                vm.setLanguage("en")
                                langMenuOpen = false
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = { navController.navigate(item.route) },
                        icon = item.icon,
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(navController = navController, startDestination = Routes.COUNTRIES, modifier = Modifier.padding(padding)) {
            composable(Routes.COUNTRIES) {
                val starVotes by vm.starVotes.collectAsState()
                CountriesScreen(
                    countries = catalog.countries,
                    favorites = favorites,
                    starVotes = starVotes,
                    onVoteStar = vm::voteStar,
                    language = language,
                    onOpen = { navController.navigate("${Routes.COUNTRIES}/${it.countryKey}") },
                    onOpenUrl = openUrl
                )
            }
            composable(
                route = Routes.COUNTRY_DETAIL,
                arguments = listOf(navArgument("countryKey") { type = NavType.StringType })
            ) { backStack ->
                val key = backStack.arguments?.getString("countryKey").orEmpty()
                val country = vm.findCountry(catalog, key)
                if (country != null) {
                    val institutionNames = country.authority.relatedInternationalInstitutionIds.mapNotNull { id ->
                        catalog.institutions.firstOrNull { it.id == id }?.let { id to (it.shortName ?: it.name) }
                    }
                    CountryDetailScreen(
                        country = country,
                        institutionNames = institutionNames,
                        isFavorite = favorites.contains("country:${country.countryKey}"),
                        language = language,
                        onToggleFavorite = { vm.toggleFavorite("country:${country.countryKey}") },
                        onOpenInstitution = { id -> navController.navigate("${Routes.INTERNATIONAL}/$id") },
                        onOpenUrl = openUrl
                    )
                }
            }
            composable(Routes.INTERNATIONAL) {
                InternationalInstitutionsScreen(
                    catalog = catalog,
                    locale = language,
                    favorites = favorites,
                    onOpen = { navController.navigate("${Routes.INTERNATIONAL}/${it.id}") }
                )
            }
            composable(
                route = Routes.INSTITUTION_DETAIL,
                arguments = listOf(navArgument("institutionId") { type = NavType.StringType })
            ) { backStack ->
                val institution = vm.findInstitution(catalog, backStack.arguments?.getString("institutionId").orEmpty())
                if (institution != null) {
                    InstitutionDetailScreen(
                        institution = institution,
                        catalog = catalog,
                        locale = language,
                        isFavorite = favorites.contains("institution:${institution.id}"),
                        onToggleFavorite = { vm.toggleFavorite("institution:${institution.id}") },
                        onOpenUrl = openUrl,
                        onOpenInstitution = { id -> navController.navigate("${Routes.INTERNATIONAL}/$id") }
                    )
                }
            }
            composable(Routes.FSRBS) {
                FSRBScreen(
                    institutions = catalog.institutions,
                    onOpen = { navController.navigate("${Routes.FSRBS}/${it.id}") }
                )
            }
            composable(
                route = Routes.FSRB_DETAIL,
                arguments = listOf(navArgument("institutionId") { type = NavType.StringType })
            ) { backStack ->
                vm.findInstitution(catalog, backStack.arguments?.getString("institutionId").orEmpty())?.let {
                    InstitutionDetailScreen(
                        institution = it,
                        catalog = catalog,
                        locale = language,
                        isFavorite = favorites.contains("institution:${it.id}"),
                        onToggleFavorite = { vm.toggleFavorite("institution:${it.id}") },
                        onOpenUrl = openUrl,
                        onOpenInstitution = { id -> navController.navigate("${Routes.INTERNATIONAL}/$id") }
                    )
                }
            }
            composable(Routes.FIUS) {
                FIUScreen(
                    institutions = catalog.institutions,
                    onOpen = { navController.navigate("${Routes.FIUS}/${it.id}") }
                )
            }
            composable(
                route = Routes.FIU_DETAIL,
                arguments = listOf(navArgument("institutionId") { type = NavType.StringType })
            ) { backStack ->
                vm.findInstitution(catalog, backStack.arguments?.getString("institutionId").orEmpty())?.let {
                    InstitutionDetailScreen(
                        institution = it,
                        catalog = catalog,
                        locale = language,
                        isFavorite = favorites.contains("institution:${it.id}"),
                        onToggleFavorite = { vm.toggleFavorite("institution:${it.id}") },
                        onOpenUrl = openUrl,
                        onOpenInstitution = { id -> navController.navigate("${Routes.INTERNATIONAL}/$id") }
                    )
                }
            }
            composable(Routes.SEARCH) {
                SearchScreen(
                    resultsProvider = { vm.search(catalog, it, language) },
                    onOpenResult = { result -> navController.navigate(result.route) }
                )
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    favorites = resolveFavorites(favorites, catalog, language),
                    onOpen = { result -> navController.navigate(result.route) }
                )
            }
            composable(Routes.ABOUT) {
                val aboutEntries by vm.aboutSections.collectAsState()
                AboutScreen(
                    entries = aboutEntries,
                    onOpenEntry = { entry -> navController.navigate("about/$entry") }
                )
            }
            composable(Routes.ABOUT_PRIVACY) {
                LegalMarkdownScreen(
                    title = if (language == "it") "Privacy" else "Privacy",
                    locale = language,
                    loadText = vm::loadLegal,
                    docId = "privacy"
                )
            }
            composable(Routes.ABOUT_TERMS) {
                LegalMarkdownScreen(
                    title = if (language == "it") "Termini di Servizio" else "Terms of Service",
                    locale = language,
                    loadText = vm::loadLegal,
                    docId = "terms"
                )
            }
            composable(Routes.ABOUT_DISCLAIMER) {
                LegalMarkdownScreen(
                    title = "Disclaimer",
                    locale = language,
                    loadText = vm::loadLegal,
                    docId = "disclaimer"
                )
            }
            composable(Routes.ABOUT_SOURCES) {
                LegalMarkdownScreen(
                    title = if (language == "it") "Fonti e Metodologia" else "Sources and Methodology",
                    locale = language,
                    loadText = vm::loadLegal,
                    docId = "sources"
                )
            }
            composable(Routes.ABOUT_LICENSES) {
                LegalMarkdownScreen(
                    title = if (language == "it") "Licenze Open Source" else "Open Source Licenses",
                    locale = language,
                    loadText = { _, _ -> "Jetpack Compose, Material 3, Kotlin, AndroidX." },
                    docId = "licenses"
                )
            }
            composable(Routes.ABOUT_CONTACT) {
                ContactScreen()
            }
        }
    }
}

private fun resolveFavorites(favorites: Set<String>, catalog: AppCatalog, locale: String): List<SearchResult> {
    return favorites.mapNotNull { favoriteId ->
        when {
            favoriteId.startsWith("country:") -> {
                val key = favoriteId.removePrefix("country:")
                catalog.countries.firstOrNull { it.countryKey == key }?.let {
                    SearchResult(
                        id = favoriteId,
                        title = "${it.flag} ${it.countryName}",
                        subtitle = it.authority.name,
                        route = "${Routes.COUNTRIES}/${it.countryKey}",
                        favoriteId = favoriteId
                    )
                }
            }

            favoriteId.startsWith("authority:") -> {
                val id = favoriteId.removePrefix("authority:")
                catalog.countries.firstOrNull { it.authority.authorityId == id }?.let {
                    SearchResult(
                        id = favoriteId,
                        title = it.authority.name,
                        subtitle = it.countryName,
                        route = "${Routes.COUNTRIES}/${it.countryKey}",
                        favoriteId = favoriteId
                    )
                }
            }

            favoriteId.startsWith("institution:") -> {
                val id = favoriteId.removePrefix("institution:")
                catalog.institutions.firstOrNull { it.id == id }?.let {
                    SearchResult(
                        id = favoriteId,
                        title = it.shortName ?: it.name,
                        subtitle = it.descriptionKey?.let { key ->
                            catalog.i18nStrings.resolve(key, locale, it.name)
                        } ?: it.name,
                        route = "${Routes.INTERNATIONAL}/${it.id}",
                        favoriteId = favoriteId
                    )
                }
            }

            else -> null
        }
    }
}
