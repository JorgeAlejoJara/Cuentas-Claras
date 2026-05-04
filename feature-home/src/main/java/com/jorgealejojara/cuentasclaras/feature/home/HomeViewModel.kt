package com.jorgealejojara.cuentasclaras.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.AccountRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())
    private val _masked = MutableStateFlow(false)
    private val _period = MutableStateFlow(Period.MONTH)
    private val _activeAccountId = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        _selectedYearMonth,
        _masked,
        _period,
        _activeAccountId,
        categoryRepository.getAll()
    ) { yearMonth, masked, period, activeAccountId, categories ->
        UiParams(yearMonth, masked, period, activeAccountId, categories.associateBy { it.id })
    }.combine(accountRepository.getAll()) { params, accounts ->
        params to accounts
    }.combine(transactionRepository.getAll()) { (params, accounts), allTransactions ->
        val startDate = params.yearMonth.atDay(1)
        val endDate = params.yearMonth.atEndOfMonth()

        val monthTransactions = allTransactions.filter { it.date in startDate..endDate }

        val filtered = if (params.activeAccountId != null) {
            monthTransactions.filter { it.accountId == params.activeAccountId }
        } else {
            monthTransactions
        }

        val totalIncome = filtered
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val totalExpense = filtered
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        val recentTransactions = filtered
            .sortedByDescending { it.date }
            .take(15)
            .mapNotNull { tx ->
                val category = params.categoriesMap[tx.categoryId] ?: return@mapNotNull null
                TransactionWithCategory(tx, category)
            }

        HomeUiState.Success(
            totalBalance = accounts.sumOf { it.balance },
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            recentTransactions = recentTransactions,
            accounts = accounts,
            selectedMonth = params.yearMonth.monthValue,
            selectedYear = params.yearMonth.year,
            masked = params.masked,
            period = params.period,
            activeAccountId = params.activeAccountId
        ) as HomeUiState
    }.catch { e ->
        emit(HomeUiState.Error(e.message ?: "Error desconocido"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun toggleMasked() {
        _masked.value = !_masked.value
    }

    fun setPeriod(period: Period) {
        _period.value = period
    }

    fun toggleAccount(accountId: Long) {
        _activeAccountId.value = if (_activeAccountId.value == accountId) null else accountId
    }

    fun onPreviousMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.minusMonths(1)
    }

    fun onNextMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.plusMonths(1)
    }

    private data class UiParams(
        val yearMonth: YearMonth,
        val masked: Boolean,
        val period: Period,
        val activeAccountId: Long?,
        val categoriesMap: Map<Long, com.jorgealejojara.cuentasclaras.core.domain.model.Category>
    )
}
