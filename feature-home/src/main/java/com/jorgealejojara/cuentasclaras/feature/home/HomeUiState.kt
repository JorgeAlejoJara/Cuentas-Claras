package com.jorgealejojara.cuentasclaras.feature.home

import com.jorgealejojara.cuentasclaras.core.domain.model.Account
import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.model.Category

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val totalBalance: Double,
        val totalIncome: Double,
        val totalExpense: Double,
        val recentTransactions: List<TransactionWithCategory>,
        val accounts: List<Account>,
        val selectedMonth: Int,
        val selectedYear: Int
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}

data class TransactionWithCategory(
    val transaction: Transaction,
    val category: Category
)

