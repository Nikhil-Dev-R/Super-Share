package com.rudraksha.supershare.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = SuperBlue,
    onPrimary = White,
    primaryContainer = BlueContainer,
    onPrimaryContainer = SuperDarkBlue,

    secondary = SuperYellow,
    onSecondary = Black,
    secondaryContainer = YellowContainer,
    onSecondaryContainer = SuperDarkBlue,

    tertiary = SuperRed,
    onTertiary = White,
    tertiaryContainer = RedContainer,
    onTertiaryContainer = SuperDarkBlue,

    background = White,
    onBackground = SuperDarkBlue,

    surface = LightGray,
    onSurface = SuperDarkBlue,

    surfaceVariant = BlueContainer,
    onSurfaceVariant = SuperDarkBlue,

    outline = SuperDarkBlue,
)

private val DarkColorScheme = darkColorScheme(
    primary = SuperBlue,
    onPrimary = Black,
    primaryContainer = BlueContainer,
    onPrimaryContainer = SuperDarkBlue,

    secondary = SuperYellow,
    onSecondary = Black,
    secondaryContainer = YellowContainer,
    onSecondaryContainer = Black,

    tertiary = SuperRed,
    onTertiary = White,
    tertiaryContainer = RedContainer,
    onTertiaryContainer = Black,

    background = DarkGray,
    onBackground = White,

    surface = SuperDarkBlue,
    onSurface = White,

    surfaceVariant = DarkGray,
    onSurfaceVariant = LightGray,

    outline = LightGray,
)

@Composable
fun SuperShareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
