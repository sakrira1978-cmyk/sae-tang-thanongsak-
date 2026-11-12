package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PhraLorColorScheme = darkColorScheme(
    primary = AntiqueGoldPrimary,
    onPrimary = ObsidianBg,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = AntiqueGoldBright,
    secondary = PatinaBronze,
    onSecondary = TextPrimaryGold,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = PatinaVerdigris,
    tertiary = MoldTerraCotta,
    onTertiary = TextPrimaryGold,
    background = ObsidianBg,
    onBackground = TextPrimaryGold,
    surface = DarkSurface,
    onSurface = TextPrimaryGold,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryMuted,
    outline = GoldBorder,
    outlineVariant = GoldBorderBright
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PhraLorColorScheme,
        typography = Typography,
        content = content
    )
}
