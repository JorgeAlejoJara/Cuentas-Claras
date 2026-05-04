package com.jorgealejojara.cuentasclaras.feature.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgealejojara.cuentasclaras.core.domain.model.Account
import com.jorgealejojara.cuentasclaras.core.domain.model.Category
import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.AccountRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
import com.jorgealejojara.cuentasclaras.core.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddUiState>(AddUiState.Loading)
    val uiState: StateFlow<AddUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val categories = categoryRepository.getAll().first()
                val accounts = accountRepository.getAll().first()
                val expenseCategories = categories.filter { it.type == TransactionType.EXPENSE }
                _uiState.value = AddUiState.Ready(
                    categories = expenseCategories,
                    selectedCategory = expenseCategories.firstOrNull(),
                    accounts = accounts,
                    selectedAccount = accounts.firstOrNull()
                )
            } catch (e: Exception) {
                _uiState.value = AddUiState.Error(e.message ?: "Error cargando datos")
            }
        }
    }

    fun onAmountChanged(amount: String) {
        updateReady { copy(amount = amount, amountError = null) }
    }

    fun onDescriptionChanged(description: String) {
        updateReady { copy(description = description, descriptionError = null) }
    }

    fun onTypeChanged(type: TransactionType) {
        viewModelScope.launch {
            val categories = categoryRepository.getAll().first()
                .filter { it.type == type }
            updateReady {
                copy(
                    type = type,
                    categories = categories,
                    selectedCategory = categories.firstOrNull()
                )
            }
        }
    }

    fun onCategorySelected(category: Category) {
        updateReady { copy(selectedCategory = category) }
    }

    fun onAccountSelected(account: Account) {
        updateReady { copy(selectedAccount = account) }
    }

    fun onSave() {
        val state = _uiState.value as? AddUiState.Ready ?: return

        // Validation
        val amountValue = state.amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            updateReady { copy(amountError = "Ingresa un monto válido") }
            return
        }
        if (state.description.isBlank()) {
            updateReady { copy(descriptionError = "Ingresa una descripción") }
            return
        }
        if (state.selectedCategory == null || state.selectedAccount == null) return

        updateReady { copy(isSaving = true) }

        viewModelScope.launch {
            try {
                val transaction = Transaction(
                    amount = amountValue,
                    description = state.description.trim(),
                    categoryId = state.selectedCategory.id,
                    type = state.type,
                    date = LocalDate.now(),
                    accountId = state.selectedAccount.id
                )
                addTransactionUseCase(transaction)
                updateReady { copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                updateReady { copy(isSaving = false) }
            }
        }
    }

    private fun updateReady(block: AddUiState.Ready.() -> AddUiState.Ready) {
        _uiState.update { state ->
            if (state is AddUiState.Ready) state.block() else state
        }
    }
}

