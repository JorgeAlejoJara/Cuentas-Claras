package com.jorgealejojara.cuentasclaras.feature.add

import com.jorgealejojara.cuentasclaras.core.domain.model.Account
import com.jorgealejojara.cuentasclaras.core.domain.model.Category
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType

sealed interface AddUiState {
    data object Loading : AddUiState

    data class Ready(
        val amount: String = "",
        val description: String = "",
        val type: TransactionType = TransactionType.EXPENSE,
        val categories: List<Category> = emptyList(),
        val selectedCategory: Category? = null,
        val accounts: List<Account> = emptyList(),
        val selectedAccount: Account? = null,
        val isSaving: Boolean = false,
        val amountError: String? = null,
        val descriptionError: String? = null,
        val saved: Boolean = false
    ) : AddUiState

    data class Error(val message: String) : AddUiState
}

