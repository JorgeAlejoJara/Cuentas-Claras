package com.jorgealejojara.cuentasclaras.feature.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
import com.jorgealejojara.cuentasclaras.core.domain.usecase.GetBudgetsUseCase
import com.jorgealejojara.cuentasclaras.core.domain.usecase.GetExpenseByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val getExpenseByCategoryUseCase: GetExpenseByCategoryUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<BudgetUiState> = _selectedYearMonth.flatMapLatest { yearMonth ->
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()

        combine(
            getBudgetsUseCase(yearMonth.monthValue, yearMonth.year),
            getExpenseByCategoryUseCase(start, end),
            categoryRepository.getAll()
        ) { budgets, expenseMap, categories ->
            val categoriesById = categories.associateBy { it.id }

            val budgetsWithProgress = budgets.mapNotNull { budget ->
                val category = categoriesById[budget.categoryId] ?: return@mapNotNull null
                BudgetWithProgress(
                    budget = budget,
                    category = category,
                    spent = expenseMap[budget.categoryId] ?: 0.0
                )
            }

            BudgetUiState.Success(
                budgets = budgetsWithProgress,
                selectedMonth = yearMonth.monthValue,
                selectedYear = yearMonth.year
            ) as BudgetUiState
        }
    }.catch { e ->
        emit(BudgetUiState.Error(e.message ?: "Error desconocido"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetUiState.Loading
    )

    fun onPreviousMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.minusMonths(1)
    }

    fun onNextMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.plusMonths(1)
    }
}

