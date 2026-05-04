package com.jorgealejojara.cuentasclaras.core.ui.organisms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jorgealejojara.cuentasclaras.core.ui.atoms.AmountText
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CategoryIcon
import com.jorgealejojara.cuentasclaras.core.ui.theme.CuentasClarasThemeExt

data class BudgetProgressItem(
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: Color,
    val spent: Double,
    val limit: Double
)

@Composable
fun BudgetProgressCard(
    item: BudgetProgressItem,
    modifier: Modifier = Modifier
) {
    val progress = if (item.limit > 0) (item.spent / item.limit).toFloat().coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600),
        label = "budget_progress"
    )
    val isOverBudget = item.spent > item.limit
    val progressColor = if (isOverBudget) {
        CuentasClarasThemeExt.colors.expense
    } else if (progress > 0.8f) {
        CuentasClarasThemeExt.colors.warning
    } else {
        item.categoryColor
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CategoryIcon(
                        iconName = item.categoryIcon,
                        color = item.categoryColor,
                        size = 36.dp,
                        iconSize = 18.dp
                    )
                    Text(
                        text = item.categoryName,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = progressColor
                )
            }

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AmountText(amount = item.spent, isIncome = false)
                Text(
                    text = "/ ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AmountText(amount = item.limit)
            }
        }
    }
}

