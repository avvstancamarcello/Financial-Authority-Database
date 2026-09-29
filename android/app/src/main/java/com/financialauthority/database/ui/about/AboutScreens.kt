package com.financialauthority.database.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financialauthority.database.ui.components.ItemCard
import com.financialauthority.database.ui.components.UiText

@Composable
fun AboutScreen(
    entries: List<Pair<String, String>>,
    onOpenEntry: (String) -> Unit,
    modifier: Modifier = Modifier,
    language: String = "it"
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text(UiText.get(language, "about"), style = MaterialTheme.typography.headlineSmall) }
        item {
            Text(UiText.get(language, "about_subtitle"))
        }

        // Matrice Comparativa Acronimo AMEV in 9 Lingue UE (Scrollabile)
        item {
            AmevMatrixCard(language = language, onClose = null)
        }

        items(entries) { pair ->
            ItemCard(title = pair.second, subtitle = pair.first, onClick = { onOpenEntry(pair.first) })
        }
    }
}

@Composable
fun AmevMatrixCard(
    language: String = "it",
    onClose: (() -> Unit)? = null
) {
    val isIt = language == "it"
    val scrollState = rememberScrollState()

    // 1st: EN (Inglese) al primo posto, poi in ordine alfabetico per sigla internazionale (AT, DE, ES, FR, IT, NL, PT, RO)
    val matrixData = listOf(
        AmevRow("🇬🇧 EN · Inglese", "Authority", "Market", "Effective", "Verification"),
        AmevRow("🇦🇹 AT · Tedesco (AT)", "Autorität", "Markt", "Effektive", "Verifizierung"),
        AmevRow("🇩🇪 DE · Tedesco (Std)", "Autorität", "Markt", "Effektive", "Verifizierung"),
        AmevRow("🇪🇸 ES · Spagnolo", "Autoridad", "Mercado", "Efectiva", "Verificación"),
        AmevRow("🇫🇷 FR · Francese", "Autorité", "Marché", "Effective", "Vérification"),
        AmevRow("🇮🇹 IT · Italiano", "Authority", "Market", "Effettiva", "Verifica"),
        AmevRow("🇳🇱 NL · Olandese", "Autoriteit", "Markt", "Effectieve", "Verificatie"),
        AmevRow("🇵🇹 PT · Portoghese", "Autoridade", "Mercado", "Efetiva", "Verificação"),
        AmevRow("🇷🇴 RO · Rumeno", "Autoritate", "Market/Mercat", "Efectivă", "Verificare")
    )

    // Sequenza cromatica verticale: Rosso, Celeste, Verde, Giallo, ripetuta e ultima rossa (9 righe)
    val rowColors = listOf(
        Color(0xFFE53935), // 1. Rosso
        Color(0xFF00BCD4), // 2. Celeste
        Color(0xFF00A859), // 3. Verde
        Color(0xFFFFC107), // 4. Giallo
        Color(0xFFE53935), // 5. Rosso
        Color(0xFF00BCD4), // 6. Celeste
        Color(0xFF00A859), // 7. Verde
        Color(0xFFFFC107), // 8. Giallo
        Color(0xFFE53935)  // 9. Rosso
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 420.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header con titolo e pulsante X di chiusura
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isIt) "Matrice Comparativa Acronimo AMEV (9 Lingue UE)" else "AMEV Acronym Comparative Matrix (9 EU Languages)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                if (onClose != null) {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close AMEV matrix",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Text(
                text = if (isIt) "L'acronimo AMEV (Authority Market Effective Verification) mantiene perfetta congruenza linguistica in 9 lingue dell'Unione Europea:"
                else "The AMEV acronym (Authority Market Effective Verification) maintains perfect linguistic congruence across 9 European Union languages:",
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider()

            // Intestazione Tabella con A (Verde), M (Bianco), E (Bianco), V (Rosso) e dimensioni aumentate
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(if (isIt) "Lingua" else "Language", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1.5f))
                Text("A", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF00A859), modifier = Modifier.weight(0.9f))
                Text("M", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFFFFF), modifier = Modifier.weight(0.9f))
                Text("E", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFFFFF), modifier = Modifier.weight(0.9f))
                Text("V", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE53935), modifier = Modifier.weight(0.9f))
            }

            HorizontalDivider()

            // Righe con EN al primo posto e poi ordinate alfabeticamente per sigla internazionale (AT, DE, ES, FR, IT, NL, PT, RO)
            matrixData.forEachIndexed { index, row ->
                val rowColor = rowColors[index % rowColors.size]
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = row.langLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = rowColor,
                        modifier = Modifier.weight(1.5f)
                    )
                    Text(row.a, fontSize = 10.sp, color = rowColor, modifier = Modifier.weight(0.9f))
                    Text(row.m, fontSize = 10.sp, color = rowColor, modifier = Modifier.weight(0.9f))
                    Text(row.e, fontSize = 10.sp, color = rowColor, modifier = Modifier.weight(0.9f))
                    Text(row.v, fontSize = 10.sp, color = rowColor, modifier = Modifier.weight(0.9f))
                }
            }
        }
    }
}

private data class AmevRow(
    val langLabel: String,
    val a: String,
    val m: String,
    val e: String,
    val v: String
)

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
fun ContactScreen(
    modifier: Modifier = Modifier,
    language: String = "it"
) {
    LazyColumn(modifier = modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text(UiText.get(language, "contacts"), style = MaterialTheme.typography.headlineSmall) }
        item { Text(UiText.get(language, "contacts_subtitle")) }
        item { Text(UiText.get(language, "contacts_email")) }
    }
}
