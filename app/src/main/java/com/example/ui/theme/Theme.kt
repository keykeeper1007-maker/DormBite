package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = BasilGreen,
    onPrimary = Color.White,
    primaryContainer = MintCream,
    onPrimaryContainer = DeepForest,
    secondary = TangerineAccent,
    onSecondary = Color.White,
    secondaryContainer = TangerineSoft,
    onSecondaryContainer = DeepBurntOrange,
    tertiary = GoldenTurmeric,
    onTertiary = Color.White,
    tertiaryContainer = GoldenSoft,
    onTertiaryContainer = Color(0xFF3E2600),
    background = WarmLinen,
    onBackground = EspressoInk,
    surface = WarmLinen,
    onSurface = EspressoInk,
    surfaceVariant = SoftStone,
    onSurfaceVariant = MutedSageInk,
    surfaceContainerLowest = PureCard,
    surfaceContainerLow = Color(0xFFF6F4EF),
    surfaceContainer = SoftStone,
    surfaceContainerHigh = Color(0xFFEAE6DE),
    outline = WarmOutline,
    outlineVariant = Color(0xFFE7E2D8),
)

private val DarkColorScheme = darkColorScheme(
    primary = BasilGreenLight,
    onPrimary = DeepForest,
    primaryContainer = Color(0xFF144A30),
    onPrimaryContainer = MintCream,
    secondary = TangerineDark,
    onSecondary = DeepBurntOrange,
    secondaryContainer = Color(0xFF6B2810),
    onSecondaryContainer = TangerineSoft,
    tertiary = Color(0xFFF4B955),
    onTertiary = Color(0xFF3E2600),
    tertiaryContainer = Color(0xFF563900),
    onTertiaryContainer = GoldenSoft,
    background = MidnightBg,
    onBackground = CloudText,
    surface = MidnightBg,
    onSurface = CloudText,
    surfaceVariant = MidnightSurfaceElevated,
    onSurfaceVariant = MutedCloudText,
    surfaceContainerLowest = Color(0xFF0D100E),
    surfaceContainerLow = MidnightSurface,
    surfaceContainer = MidnightSurfaceElevated,
    surfaceContainerHigh = Color(0xFF2C342F),
    outline = MidnightOutline,
    outlineVariant = Color(0xFF27302A),
)

val DormBiteShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun DormBiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = DormBiteShapes,
        content = content
    )
}
