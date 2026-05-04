package com.jorgealejojara.cuentasclaras.ui.theme

import androidx.compose.runtime.Composable
import com.jorgealejojara.cuentasclaras.core.ui.theme.CuentasClarasTheme as CoreTheme

@Composable
fun CuentasClarasTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CoreTheme(
        darkTheme = darkTheme,
        content = content
    )
}