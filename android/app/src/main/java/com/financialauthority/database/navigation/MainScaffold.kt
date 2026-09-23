package com.financialauthority.database.navigation

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.financialauthority.database.ui.countries.CountriesScreen
import com.financialauthority.database.ui.countries.CountryDetailScreen
import com.financialauthority.database.ui.favorites.FavoritesScreen
import com.financialauthority.database.ui.international.FIUScreen
import com.financialauthority.database.ui.international.FSRBScreen
import com.financialauthority.database.ui.international.InstitutionDetailScreen
import com.financialauthority.database.ui.international.InternationalInstitutionsScreen
import com.financialauthority.database.ui.search.SearchScreen

private data class BottomItem(val route: String, val label: String, val icon: @Composable () -> Unit)

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
        BottomItem(Routes.COUNTRIES, if (language == "it") "Paesi" else "Countries", { Icon(Icons.Default.Public, contentDescription = "Paesi") }),
        BottomItem(Routes.INTERNATIONAL, if (language == "it") "Internazionali" else "International", { Icon(Icons.Default.Language, contentDescription = "Internazionali") }),
        BottomItem(Routes.SEARCH, if (language == "it") "Cerca" else "Search", { Icon(Icons.Default.Search, contentDescription = "Cerca") }),
        BottomItem(Routes.FAVORITES, if (language == "it") "Preferiti" else "Favorites", { Icon(Icons.Default.Favorite, contentDescription = "Preferiti") }),
        BottomItem(Routes.ABOUT, if (language == "it") "Informazioni" else "About", { Icon(Icons.Default.Info, contentDescription = "Informazioni") })
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
                    IconButton(onClick = { langMenuOpen = true }) {
                        Icon(Icons.Default.TravelExplore, contentDescription = "Selettore lingua")
                    }
                    DropdownMenu(expanded = langMenuOpen, onDismissRequest = { langMenuOpen = false }) {
                        DropdownMenuItem(text = { Text("Italiano") }, onClick = {
                            vm.setLanguage("it")
                            langMenuOpen = false
                        })
                        DropdownMenuItem(text = { Text("English") }, onClick = {
                            vm.setLanguage("en")
                            langMenuOpen = false
                        })
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
                CountriesScreen(
                    countries = catalog.countries,
                    favorites = favorites,
                    onOpen = { navController.navigate("${Routes.COUNTRIES}/${it.countryKey}") }
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
                FSRBScreen(catalog.institutions) { navController.navigate("${Routes.FSRBS}/${it.id}") }
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
                FIUScreen(catalog.institutions) { navController.navigate("${Routes.FIUS}/${it.id}") }
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
                AboutScreen(
                    entries = vm.aboutSections.value,
                    onOpenEntry = { entry -> navController.navigate("about/$entry") }
                )
            }
            composable(Routes.ABOUT_PRIVACY) {
                LegalMarkdownScreen(
                    title = "Privacy",
                    locale = language,
                    loadText = vm::loadLegal,
                    docId = "privacy"
                )
            }
            composable(Routes.ABOUT_TERMS) {
                LegalMarkdownScreen(
                    title = "Terms of Service",
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
                    title = "Sources and Methodology",
                    locale = language,
                    loadText = vm::loadLegal,
                    docId = "sources"
                )
            }
            composable(Routes.ABOUT_LICENSES) {
                LegalMarkdownScreen(
                    title = "Open Source Licenses",
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
