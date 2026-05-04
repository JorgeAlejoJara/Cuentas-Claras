package com.jorgealejojara.cuentasclaras.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Material 3 Color Schemes ──

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    tertiary = TertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariant2Light,
    onSurface = OnSurfaceLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    tertiary = TertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariant2Dark,
    onSurface = OnSurfaceDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

// ── Extended colors (finanzas) ──

@Immutable
data class ExtendedColors(
    val income: Color,
    val expense: Color,
    val savings: Color,
    val warning: Color,
    val surfaceVariant1: Color,
    val surfaceVariant3: Color
)

private val LightExtendedColors = ExtendedColors(
    income = IncomeLight,
    expense = ExpenseLight,
    savings = SavingsLight,
    warning = WarningLight,
    surfaceVariant1 = SurfaceVariant1Light,
    surfaceVariant3 = SurfaceVariant3Light
)

private val DarkExtendedColors = ExtendedColors(
    income = IncomeDark,
    expense = ExpenseDark,
    savings = SavingsDark,
    warning = WarningDark,
    surfaceVariant1 = SurfaceVariant1Dark,
    surfaceVariant3 = SurfaceVariant3Dark
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

// ── Theme ──

@Composable
fun CuentasClarasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CuentasClarasTypography,
            shapes = CuentasClarasShapes,
            content = content
        )
    }
}

// ── Convenience accessor ──

object CuentasClarasThemeExt {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}

