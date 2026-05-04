package com.jorgealejojara.cuentasclaras.core.ui.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.jorgealejojara.cuentasclaras.core.ui.theme.CuentasClarasThemeExt
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AmountText(
    amount: Double,
    modifier: Modifier = Modifier,
    isIncome: Boolean? = null,
    large: Boolean = false
) {
    val color = when (isIncome) {
        true -> CuentasClarasThemeExt.colors.income
        false -> CuentasClarasThemeExt.colors.expense
        null -> MaterialTheme.colorScheme.onSurface
    }
    val style = if (large) {
        MaterialTheme.typography.headlineLarge
    } else {
        MaterialTheme.typography.titleMedium
    }
    val prefix = when (isIncome) {
        true -> "+ "
        false -> "- "
        null -> ""
    }
    val formatted = NumberFormat.getCurrencyInstance(Locale("es", "CO")).format(
        if (isIncome == false) kotlin.math.abs(amount) else amount
    )

    Text(
        text = "$prefix$formatted",
        style = style,
        fontWeight = FontWeight.SemiBold,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

