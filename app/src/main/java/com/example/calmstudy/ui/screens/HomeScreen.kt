package com.example.calmstudy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import coe.example.calmstudy.ui.components.AnimatedBackground

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

    Box(modifier = Modifier.fillMaxSize()) {
        // Анімований фон першим, щоб був позаду
        AnimatedBackground(
            modifier = Modifier.fillMaxSize(),
            primaryColor = MaterialTheme.colorScheme.background,
            secondaryColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
        
        // Основний контент поверх фону
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TopAppBar
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

            // Основний контент
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // GIF слона
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.elephant1312)
                        .size(Size.ORIGINAL)
                        .build(),
                    contentDescription = "Анімація слона",
                    imageLoader = imageLoader,
                    modifier = Modifier.size(150.dp)
                )
                
                Text(
                    text = "Ласкаво просимо до CalmStudy!",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                // Картки з функціоналом
                ElevatedCard(
                    onClick = { navController.navigate("breathing") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = { Text("Дихальні вправи") },
                        supportingContent = { Text("Заспокійливі дихальні техніки") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }

                // Картка для міні-ігор
                ListItem(
                    headlineContent = { Text("Міні-ігри") },
                    supportingContent = { Text("Розважальні міні-ігри для відпочинку") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Games,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier.clickable { navController.navigate("minigames") }
                )

                ElevatedCard(
                    onClick = { navController.navigate("quotes") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = { Text("Позитивні цитати") },
                        supportingContent = { Text("Мотиваційні та заспокійливі цитати") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }

                ElevatedCard(
                    onClick = { navController.navigate("relax") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = { Text("Релакс медіа") },
                        supportingContent = { Text("Музика, звуки природи та медитації") },
                        leadingContent = { 
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }

                ElevatedCard(
                    onClick = { navController.navigate("timer") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = { Text("Таймер сну/медитації") },
                        supportingContent = { Text("Встановіть таймер для сну або медитації") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }

                // Додаємо відступ внизу для кращого вигляду при прокрутці
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}