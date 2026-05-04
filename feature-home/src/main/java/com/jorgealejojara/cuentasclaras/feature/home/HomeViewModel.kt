package com.jorgealejojara.cuentasclaras.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgealejojara.cuentasclaras.core.domain.model.Category
import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.AccountRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import com.jorgealejojara.cuentasclaras.core.domain.usecase.GetBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    private val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<HomeUiState> = combine(
        _selectedYearMonth,
        categoryRepository.getAll(),
        accountRepository.getAll()
    ) { yearMonth, categories, accounts ->
        Triple(yearMonth, categories, accounts)
    }.combine(
        _selectedYearMonth
    ) { triple, yearMonth ->
        val (_, categories, accounts) = triple
        val categoriesMap = categories.associateBy { it.id }

        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()

        Triple(yearMonth, categoriesMap, accounts) to (startDate to endDate)
    }.combine(
        transactionRepository.getAll()
    ) { (context, dateRange), allTransactions ->
        val (yearMonth, categoriesMap, accounts) = context
        val (startDate, endDate) = dateRange

        val monthTransactions = allTransactions.filter { tx ->
            tx.date in startDate..endDate
        }

        val totalIncome = monthTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }

        val totalExpense = monthTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        val recentTransactions = monthTransactions
            .sortedByDescending { it.date }
            .take(10)
            .mapNotNull { tx ->
                val category = categoriesMap[tx.categoryId] ?: return@mapNotNull null
                TransactionWithCategory(tx, category)
            }

        HomeUiState.Success(
            totalBalance = totalIncome - totalExpense,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            recentTransactions = recentTransactions,
            accounts = accounts,
            selectedMonth = yearMonth.monthValue,
            selectedYear = yearMonth.year
        ) as HomeUiState
    }.catch { e ->
        emit(HomeUiState.Error(e.message ?: "Error desconocido"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun onMonthChanged(month: Int, year: Int) {
        _selectedYearMonth.value = YearMonth.of(year, month)
    }

    fun onPreviousMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.minusMonths(1)
    }

    fun onNextMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.plusMonths(1)
    }
}

