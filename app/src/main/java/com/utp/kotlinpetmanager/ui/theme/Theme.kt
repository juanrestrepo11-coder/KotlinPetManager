package com.utp.kotlinpetmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta para modo claro
private val ColoresClaros = lightColorScheme(
    primary = Color(0xFF00796B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB2DFDB),
    onPrimaryContainer = Color(0xFF00201C),
    secondary = Color(0xFFFF8A65),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDBCF),
    onSecondaryContainer = Color(0xFF3A0B00),
    background = Color(0xFFF5F7F6),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE3ECEA),
    error = Color(0xFFD32F2F),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002)
)

// Paleta para modo oscuro
private val ColoresOscuros = darkColorScheme(
    primary = Color(0xFF4DB6AC),
    onPrimary = Color(0xFF003731),
    primaryContainer = Color(0xFF005048),
    onPrimaryContainer = Color(0xFFB2DFDB),
    secondary = Color(0xFFFFB59D),
    onSecondary = Color(0xFF5D1900),
    secondaryContainer = Color(0xFF7A2E12),
    onSecondaryContainer = Color(0xFFFFDBCF),
    background = Color(0xFF101413),
    surface = Color(0xFF161C1B),
    surfaceVariant = Color(0xFF2A3331),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun KotlinPetManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) ColoresOscuros else ColoresClaros,
        typography = Typography,
        content = content
    )
}