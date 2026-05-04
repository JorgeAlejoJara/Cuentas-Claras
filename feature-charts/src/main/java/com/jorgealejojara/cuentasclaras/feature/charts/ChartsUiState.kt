package com.jorgealejojara.cuentasclaras.feature.charts

import com.jorgealejojara.cuentasclaras.core.domain.model.Category

sealed interface ChartsUiState {
    data object Loading : ChartsUiState

    data class Success(
        val expenseByCategory: List<CategoryExpense>,
        val totalExpense: Double,
        val selectedMonth: Int,
        val selectedYear: Int
    ) : ChartsUiState

    data class Error(val message: String) : ChartsUiState
}

data class CategoryExpense(
    val category: Category,
    val amount: Double,
    val percentage: Float
)

