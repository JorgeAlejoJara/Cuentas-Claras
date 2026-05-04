package com.jorgealejojara.cuentasclaras.feature.budget

import com.jorgealejojara.cuentasclaras.core.domain.model.Budget
import com.jorgealejojara.cuentasclaras.core.domain.model.Category

sealed interface BudgetUiState {
    data object Loading : BudgetUiState

    data class Success(
        val budgets: List<BudgetWithProgress>,
        val selectedMonth: Int,
        val selectedYear: Int
    ) : BudgetUiState

    data class Error(val message: String) : BudgetUiState
}

data class BudgetWithProgress(
    val budget: Budget,
    val category: Category,
    val spent: Double
)

