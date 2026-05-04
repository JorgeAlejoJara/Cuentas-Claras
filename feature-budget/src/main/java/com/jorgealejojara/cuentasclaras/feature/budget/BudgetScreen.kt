package com.jorgealejojara.cuentasclaras.feature.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcLoadingIndicator
import com.jorgealejojara.cuentasclaras.core.ui.molecules.EmptyState
import com.jorgealejojara.cuentasclaras.core.ui.molecules.ErrorState
import com.jorgealejojara.cuentasclaras.core.ui.organisms.BudgetProgressCard
import com.jorgealejojara.cuentasclaras.core.ui.organisms.BudgetProgressItem
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    modifier: Modifier = Modifier,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Presupuestos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is BudgetUiState.Loading -> {
                CcLoadingIndicator(modifier = Modifier.fillMaxSize().padding(paddingValues))
            }
            is BudgetUiState.Error -> {
                ErrorState(message = state.message, modifier = Modifier.fillMaxSize().padding(paddingValues).padding(32.dp))
            }
            is BudgetUiState.Success -> {
                BudgetContent(
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
private fun BudgetContent(
    state: BudgetUiState.Success,
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
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

        if (state.budgets.isEmpty()) {
            EmptyState(
                title = "Sin presupuestos",
                message = "No tienes presupuestos configurados para este mes.",
                modifier = Modifier.padding(vertical = 32.dp)
            )
        } else {
            state.budgets.forEach { bwp ->
                BudgetProgressCard(
                    item = BudgetProgressItem(
                        categoryName = bwp.category.name,
                        categoryIcon = bwp.category.icon,
                        categoryColor = Color(bwp.category.color),
                        spent = bwp.spent,
                        limit = bwp.budget.limitAmount
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

