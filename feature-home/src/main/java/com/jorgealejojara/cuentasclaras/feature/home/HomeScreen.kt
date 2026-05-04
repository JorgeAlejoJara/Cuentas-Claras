package com.jorgealejojara.cuentasclaras.feature.home

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcLoadingIndicator
import com.jorgealejojara.cuentasclaras.core.ui.molecules.BalanceCard
import com.jorgealejojara.cuentasclaras.core.ui.molecules.EmptyState
import com.jorgealejojara.cuentasclaras.core.ui.molecules.ErrorState
import com.jorgealejojara.cuentasclaras.core.ui.organisms.TransactionItem
import com.jorgealejojara.cuentasclaras.core.ui.organisms.TransactionList
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cuentas Claras",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar transacción"
                )
            }
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is HomeUiState.Loading -> {
                CcLoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is HomeUiState.Error -> {
                ErrorState(
                    message = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                )
            }

            is HomeUiState.Success -> {
                HomeContent(
                    state = state,
                    onPreviousMonth = viewModel::onPreviousMonth,
                    onNextMonth = viewModel::onNextMonth,
                    onNavigateToAdd = onNavigateToAdd,
                    onNavigateToTransactions = onNavigateToTransactions,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState.Success,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToTransactions: () -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(20.dp)
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

        // Balance card
        BalanceCard(
            totalBalance = state.totalBalance,
            totalIncome = state.totalIncome,
            totalExpense = state.totalExpense
        )

        // Recent transactions
        if (state.recentTransactions.isEmpty()) {
            EmptyState(
                title = "Sin transacciones",
                message = "No hay transacciones este mes.\n¡Registra tu primer ingreso o gasto!",
                actionLabel = "Agregar transacción",
                onAction = onNavigateToAdd,
                modifier = Modifier.padding(vertical = 32.dp)
            )
        } else {
            TransactionList(
                title = "Transacciones recientes",
                transactions = state.recentTransactions.map { twc ->
                    TransactionItem(
                        id = twc.transaction.id,
                        description = twc.transaction.description,
                        categoryName = twc.category.name,
                        categoryIcon = twc.category.icon,
                        categoryColor = Color(twc.category.color),
                        amount = twc.transaction.amount,
                        isIncome = twc.transaction.type == TransactionType.INCOME,
                        date = formatDate(twc.transaction.date)
                    )
                },
                showSeeAll = state.recentTransactions.size >= 10,
                onSeeAllClick = onNavigateToTransactions
            )
        }

        Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
    }
}

private fun formatDate(date: java.time.LocalDate): String {
    val day = date.dayOfMonth
    val month = date.month.getDisplayName(TextStyle.SHORT, Locale("es"))
        .replaceFirstChar { it.uppercase() }
    return "$day $month"
}

