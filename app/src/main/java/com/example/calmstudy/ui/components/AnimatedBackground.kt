package coe.example.calmstudy.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFF8F0FF), // світло-фіолетовий
    secondaryColor: Color = Color(0xFF9575CD) // лавандовий
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(90000, easing = LinearEasing) // Дуже повільне обертання
        )
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2
        
        // Малюємо градієнтний фон
        drawRect(
            color = primaryColor,
            size = size
        )
        
        // Малюємо декілька шарів анімованих елементів
        translate(centerX, centerY) {
            // Перший шар - великі кола
            rotate(rotation * 0.5f) { // Повільніше обертання
                for (i in 0..5) {
                    val angle = i * 72f
                    val radius = 300f * scale
                    val x = radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                    val y = radius * sin(Math.toRadians(angle.toDouble())).toFloat()
                    
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.1f),
                        radius = 200f * scale,
                        center = Offset(x, y)
                    )
                }
            }
            
            // Другий шар - менші кола
            rotate(-rotation * 0.3f) { // Обертання в протилежному напрямку
                for (i in 0..7) {
                    val angle = i * 51.4f
                    val radius = 200f * scale
                    val x = radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                    val y = radius * sin(Math.toRadians(angle.toDouble())).toFloat()
                    
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.15f),
                        radius = 150f * scale,
                        center = Offset(x, y)
                    )
                }
            }
            
            // Третій шар - найменші кола
            rotate(rotation * 0.2f) { // Ще повільніше обертання
                for (i in 0..9) {
                    val angle = i * 40f
                    val radius = 150f * scale
                    val x = radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                    val y = radius * sin(Math.toRadians(angle.toDouble())).toFloat()
                    
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.2f),
                        radius = 100f * scale,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
} 