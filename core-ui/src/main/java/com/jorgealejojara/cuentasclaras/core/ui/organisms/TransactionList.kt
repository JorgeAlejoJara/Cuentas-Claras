package com.jorgealejojara.cuentasclaras.core.ui.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcTextButton
import com.jorgealejojara.cuentasclaras.core.ui.molecules.TransactionCard

data class TransactionItem(
    val id: Long,
    val description: String,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: Color,
    val amount: Double,
    val isIncome: Boolean,
    val date: String
)

@Composable
fun TransactionList(
    title: String,
    transactions: List<TransactionItem>,
    modifier: Modifier = Modifier,
    showSeeAll: Boolean = false,
    onSeeAllClick: (() -> Unit)? = null,
    onTransactionClick: ((Long) -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            if (showSeeAll && onSeeAllClick != null) {
                CcTextButton(
                    text = "Ver todo",
                    onClick = onSeeAllClick
                )
            }
        }

        transactions.forEach { tx ->
            TransactionCard(
                description = tx.description,
                categoryName = tx.categoryName,
                categoryIcon = tx.categoryIcon,
                categoryColor = tx.categoryColor,
                amount = tx.amount,
                isIncome = tx.isIncome,
                date = tx.date,
                onClick = onTransactionClick?.let { { it(tx.id) } }
            )
        }
    }
}

