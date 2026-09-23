package com.financialauthority.database.ui.components

object UiText {
    private val labels = mapOf(
        "it" to mapOf(
            "countries" to "Paesi",
            "international" to "Autorità Internazionali",
            "search" to "Cerca",
            "favorites" to "Preferiti",
            "about" to "Informazioni",
            "loading" to "Caricamento dataset locali...",
            "error" to "Errore",
            "retry" to "Riprova",
            "no_results" to "Nessun risultato",
            "related" to "Collegamenti internazionali",
            "powers_limits" to "Poteri e limiti",
            "official_links" to "Link ufficiali"
        ),
        "en" to mapOf(
            "countries" to "Countries",
            "international" to "International Authorities",
            "search" to "Search",
            "favorites" to "Favorites",
            "about" to "About",
            "loading" to "Loading local datasets...",
            "error" to "Error",
            "retry" to "Retry",
            "no_results" to "No results",
            "related" to "International links",
            "powers_limits" to "Powers and limits",
            "official_links" to "Official links"
        )
    )

    fun get(locale: String, key: String): String = labels[locale]?.get(key)
        ?: labels["en"]?.get(key)
        ?: labels["it"]?.get(key)
        ?: key
}
