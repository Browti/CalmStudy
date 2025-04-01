package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PositiveQuotesScreen(navController: NavController) {
    var currentQuoteIndex by remember { mutableStateOf(0) }

    val quotes = listOf(
        "Кожен день - це нова можливість стати кращим",
        "Ти сильніший, ніж думаєш",
        "Віра в себе - це половина успіху",
        "Кожна перешкода - це крок до успіху",
        "Ти можеш все, якщо тільки захочеш",
        "Навчання - це ключ до майбутнього",
        "Твої зусилля не будуть марними",
        "Кожен крок вперед - це перемога",
        "Ти маєш талант до успіху",
        "Віра в себе - це найкращий мотиватор",
        "Твоя посмішка змінює світ навколо",
        "Кожна мрія починається з першого кроку",
        "У тебе є все необхідне для успіху",
        "Сьогодні - твій день для великих справ",
        "Ти джерело натхнення для інших",
        "Твої думки створюють твою реальність",
        "Кожна помилка - це урок мудрості",
        "Ти сильніший за свої страхи",
        "Твій потенціал безмежний",
        "Щастя живе в простих моментах"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Позитивні поради") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = quotes[currentQuoteIndex],
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        currentQuoteIndex = (currentQuoteIndex + 1) % quotes.size
                    }
                ) {
                    Text("Наступна фраза")
                }
            }
        }
    }
}