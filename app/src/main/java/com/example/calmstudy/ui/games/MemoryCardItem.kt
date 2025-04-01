package com.example.calmstudy.ui.games

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MemoryCardItem(
    card: Card,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "card_flip"
    )

    val scale by animateFloatAsState(
        targetValue = if (card.isAnimating) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "card_scale"
    )

    Card(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                scaleX = scale
                scaleY = scale
            }
            .clickable(enabled = !card.isMatched && !card.isFlipped) { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (card.isMatched) 0.dp else 4.dp
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Показуємо рубашку карти
                Text(
                    text = "?",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                // Показуємо емодзі
                Text(
                    text = card.emoji,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer {
                        rotationY = 180f
                    }
                )
            }
        }
    }
}