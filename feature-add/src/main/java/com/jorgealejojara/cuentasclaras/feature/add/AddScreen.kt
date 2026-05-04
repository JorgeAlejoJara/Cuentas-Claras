package com.jorgealejojara.cuentasclaras.feature.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcLoadingIndicator
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcPrimaryButton
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcTextField
import com.jorgealejojara.cuentasclaras.core.ui.molecules.CategoryChip
import com.jorgealejojara.cuentasclaras.core.ui.molecules.ErrorState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navigate back on save
    LaunchedEffect(uiState) {
        if (uiState is AddUiState.Ready && (uiState as AddUiState.Ready).saved) {
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Agregar transacción") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is AddUiState.Loading -> {
                CcLoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is AddUiState.Error -> {
                ErrorState(
                    message = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                )
            }

            is AddUiState.Ready -> {
                AddContent(
                    state = state,
                    onAmountChanged = viewModel::onAmountChanged,
                    onDescriptionChanged = viewModel::onDescriptionChanged,
                    onTypeChanged = viewModel::onTypeChanged,
                    onCategorySelected = viewModel::onCategorySelected,
                    onAccountSelected = viewModel::onAccountSelected,
                    onSave = viewModel::onSave,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun AddContent(
    state: AddUiState.Ready,
    onAmountChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onTypeChanged: (TransactionType) -> Unit,
    onCategorySelected: (com.jorgealejojara.cuentasclaras.core.domain.model.Category) -> Unit,
    onAccountSelected: (com.jorgealejojara.cuentasclaras.core.domain.model.Account) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Type selector (Income / Expense)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = state.type == TransactionType.EXPENSE,
                onClick = { onTypeChanged(TransactionType.EXPENSE) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) {
                Text("Gasto")
            }
            SegmentedButton(
                selected = state.type == TransactionType.INCOME,
                onClick = { onTypeChanged(TransactionType.INCOME) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) {
                Text("Ingreso")
            }
        }

        // Amount
        CcTextField(
            value = state.amount,
            onValueChange = onAmountChanged,
            label = "Monto",
            placeholder = "0.00",
            keyboardType = KeyboardType.Decimal,
            leadingIcon = {
                Icon(Icons.Default.AttachMoney, contentDescription = null)
            },
            isError = state.amountError != null,
            errorMessage = state.amountError
        )

        // Description
        CcTextField(
            value = state.description,
            onValueChange = onDescriptionChanged,
            label = "Descripción",
            placeholder = "Ej: Almuerzo con amigos",
            isError = state.descriptionError != null,
            errorMessage = state.descriptionError
        )

        // Categories
        Text(
            text = "Categoría",
            style = MaterialTheme.typography.titleSmall
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.categories.forEach { category ->
                CategoryChip(
                    name = category.name,
                    iconName = category.icon,
                    color = Color(category.color),
                    isSelected = category == state.selectedCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        // Account selector
        Text(
            text = "Cuenta",
            style = MaterialTheme.typography.titleSmall
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.accounts.forEach { account ->
                FilterChip(
                    selected = account == state.selectedAccount,
                    onClick = { onAccountSelected(account) },
                    label = { Text(account.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Save button
        CcPrimaryButton(
            text = if (state.isSaving) "Guardando..." else "Guardar",
            onClick = onSave,
            enabled = !state.isSaving
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

