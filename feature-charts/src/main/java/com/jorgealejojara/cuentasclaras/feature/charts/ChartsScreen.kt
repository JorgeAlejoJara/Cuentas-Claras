package com.jorgealejojara.cuentasclaras.feature.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jorgealejojara.cuentasclaras.core.ui.atoms.AmountText
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CategoryIcon
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcLoadingIndicator
import com.jorgealejojara.cuentasclaras.core.ui.molecules.EmptyState
import com.jorgealejojara.cuentasclaras.core.ui.molecules.ErrorState
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    modifier: Modifier = Modifier,
    viewModel: ChartsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Gráficas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is ChartsUiState.Loading -> {
                CcLoadingIndicator(modifier = Modifier.fillMaxSize().padding(paddingValues))
            }
            is ChartsUiState.Error -> {
                ErrorState(message = state.message, modifier = Modifier.fillMaxSize().padding(paddingValues).padding(32.dp))
            }
            is ChartsUiState.Success -> {
                ChartsContent(
                    state = state,
                    onPreviousMonth = viewModel::onPreviousMonth,
                    onNextMonth = viewModel::onNextMonth,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ChartsContent(
    state: ChartsUiState.Success,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthName = Month.of(state.selectedMonth)
        .getDisplayName(TextStyle.FULL, Locale("es"))
        .replaceFirstChar { it.uppercase() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Month selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Mes anterior")
            }
            Text(
                text = "$monthName ${state.selectedYear}",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            IconButton(onClick = onNextMonth) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Mes siguiente")
            }
        }

        // Total expense card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total gastos del mes", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                AmountText(amount = state.totalExpense, isIncome = false, large = true)
            }
        }

        if (state.expenseByCategory.isEmpty()) {
            EmptyState(
                title = "Sin gastos",
                message = "No hay gastos registrados este mes.",
                modifier = Modifier.padding(vertical = 32.dp)
            )
        } else {
            Text("Gastos por categoría", style = MaterialTheme.typography.titleMedium)

            state.expenseByCategory.forEach { item ->
                CategoryExpenseRow(item = item)
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun CategoryExpenseRow(item: CategoryExpense) {
    val color = Color(item.category.color)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    CategoryIcon(iconName = item.category.icon, color = color, size = 36.dp, iconSize = 18.dp)
                    Text(item.category.name, style = MaterialTheme.typography.bodyLarge)
                }
                Column(horizontalAlignment = Alignment.End) {
                    AmountText(amount = item.amount, isIncome = false)
                    Text("${item.percentage.toInt()}%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            LinearProgressIndicator(
                progress = { item.percentage / 100f },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(MaterialTheme.shapes.extraSmall),
                color = color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

