package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.ui.theme.AntiqueGoldBright
import com.example.ui.theme.AntiqueGoldPrimary
import com.example.ui.theme.PatinaVerdigris

@Composable
fun ScannerHudOverlay(
    isScanning: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_animation")
    
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanLine"
    )

    val radarRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarRotation"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val bracketSize = 28f
        val strokeWidth = 3.5f

        // 1. Four Corner Targeting Brackets
        val cornerColor = AntiqueGoldPrimary.copy(alpha = 0.85f)
        
        // Top-Left
        drawLine(cornerColor, Offset(16f, 16f), Offset(16f + bracketSize, 16f), strokeWidth)
        drawLine(cornerColor, Offset(16f, 16f), Offset(16f, 16f + bracketSize), strokeWidth)

        // Top-Right
        drawLine(cornerColor, Offset(width - 16f, 16f), Offset(width - 16f - bracketSize, 16f), strokeWidth)
        drawLine(cornerColor, Offset(width - 16f, 16f), Offset(width - 16f, 16f + bracketSize), strokeWidth)

        // Bottom-Left
        drawLine(cornerColor, Offset(16f, height - 16f), Offset(16f + bracketSize, height - 16f), strokeWidth)
        drawLine(cornerColor, Offset(16f, height - 16f), Offset(16f, height - 16f - bracketSize), strokeWidth)

        // Bottom-Right
        drawLine(cornerColor, Offset(width - 16f, height - 16f), Offset(width - 16f - bracketSize, height - 16f), strokeWidth)
        drawLine(cornerColor, Offset(width - 16f, height - 16f), Offset(width - 16f, height - 16f - bracketSize), strokeWidth)

        // 2. Center Targeting Reticle (Crosshair & Circle)
        val center = Offset(width / 2f, height / 2f)
        val reticleRadius = minOf(width, height) * 0.35f

        // Outer dashed circular guide
        drawCircle(
            color = AntiqueGoldPrimary.copy(alpha = 0.3f),
            radius = reticleRadius,
            center = center,
            style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f))
        )

        // Inner solid circle
        drawCircle(
            color = AntiqueGoldPrimary.copy(alpha = 0.5f),
            radius = reticleRadius * 0.55f,
            center = center,
            style = Stroke(width = 1f)
        )

        // Center crosshair ticks
        val tickLen = 14f
        drawLine(AntiqueGoldBright.copy(alpha = 0.6f), Offset(center.x - tickLen, center.y), Offset(center.x + tickLen, center.y), 2f)
        drawLine(AntiqueGoldBright.copy(alpha = 0.6f), Offset(center.x, center.y - tickLen), Offset(center.x, center.y + tickLen), 2f)

        // 3. Active Laser Scan Beam (when isScanning is true)
        if (isScanning) {
            val beamY = height * scanLineY
            
            // Laser glow gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        PatinaVerdigris.copy(alpha = 0.25f),
                        AntiqueGoldBright.copy(alpha = 0.7f),
                        Color.Transparent
                    ),
                    startY = beamY - 45f,
                    endY = beamY + 45f
                ),
                topLeft = Offset(0f, beamY - 45f),
                size = Size(width, 90f)
            )

            // Sharp center laser line
            drawLine(
                color = AntiqueGoldBright,
                start = Offset(16f, beamY),
                end = Offset(width - 16f, beamY),
                strokeWidth = 2.5f
            )
        }
    }
}
