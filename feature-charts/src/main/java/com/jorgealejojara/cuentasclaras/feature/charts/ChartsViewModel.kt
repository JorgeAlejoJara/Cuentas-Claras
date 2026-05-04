package com.jorgealejojara.cuentasclaras.feature.charts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
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
class ChartsViewModel @Inject constructor(
    private val getExpenseByCategoryUseCase: GetExpenseByCategoryUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _selectedYearMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<ChartsUiState> = _selectedYearMonth.flatMapLatest { yearMonth ->
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()

        combine(
            getExpenseByCategoryUseCase(start, end),
            categoryRepository.getAll()
        ) { expenseMap, categories ->
            val categoriesById = categories.associateBy { it.id }
            val totalExpense = expenseMap.values.sum()

            val categoryExpenses = expenseMap.mapNotNull { (catId, amount) ->
                val category = categoriesById[catId] ?: return@mapNotNull null
                CategoryExpense(
                    category = category,
                    amount = amount,
                    percentage = if (totalExpense > 0) (amount / totalExpense * 100).toFloat() else 0f
                )
            }.sortedByDescending { it.amount }

            ChartsUiState.Success(
                expenseByCategory = categoryExpenses,
                totalExpense = totalExpense,
                selectedMonth = yearMonth.monthValue,
                selectedYear = yearMonth.year
            ) as ChartsUiState
        }
    }.catch { e ->
        emit(ChartsUiState.Error(e.message ?: "Error desconocido"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChartsUiState.Loading
    )

    fun onPreviousMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.minusMonths(1)
    }

    fun onNextMonth() {
        _selectedYearMonth.value = _selectedYearMonth.value.plusMonths(1)
    }
}

