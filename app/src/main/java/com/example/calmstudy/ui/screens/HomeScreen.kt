package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.calmstudy.R
import com.example.calmstudy.ui.theme.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CalmStudy") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = { themeViewModel.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Перемкнути на світлу тему" else "Перемкнути на темну тему"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // GIF слона
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.elephant1312)
                    .size(Size.ORIGINAL)
                    .build(),
                contentDescription = "Анімація слона",
                imageLoader = imageLoader,
                modifier = Modifier.size(180.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Ласкаво просимо до CalmStudy!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            ElevatedCard(
                onClick = { navController.navigate("breathing") },
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = { Text("Дихальні вправи") },
                    supportingContent = { Text("Заспокійливі дихальні техніки") },
                    leadingContent = { Icon(Icons.Default.Air, contentDescription = null) }
                )
            }

            ElevatedCard(
                onClick = { navController.navigate("minigames") },
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = { Text("Міні-ігри") },
                    supportingContent = { Text("Ігри для відволікання від стресу") },
                    leadingContent = { Icon(Icons.Default.Games, contentDescription = null) }
                )
            }

            ElevatedCard(
                onClick = { navController.navigate("quotes") },
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = { Text("Позитивні цитати") },
                    supportingContent = { Text("Мотиваційні та заспокійливі цитати") },
                    leadingContent = { Icon(Icons.Default.FormatQuote, contentDescription = null) }
                )
            }

            ElevatedCard(
                onClick = { navController.navigate("relax") },
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    headlineContent = { Text("Релакс медіа") },
                    supportingContent = { Text("Музика, звуки природи та медитації") },
                    leadingContent = { Icon(Icons.Default.Spa, contentDescription = null) }
                )
            }
        }
    }
}