package com.jorgealejojara.cuentasclaras.core.domain.usecase

import com.jorgealejojara.cuentasclaras.core.domain.model.Budget
import com.jorgealejojara.cuentasclaras.core.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetsUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<List<Budget>> {
        return repository.getByMonthYear(month, year)
    }
}

