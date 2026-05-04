package com.jorgealejojara.cuentasclaras.core.domain.usecase

import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<Double> {
        return combine(
            repository.getTotalIncome(month, year),
            repository.getTotalExpense(month, year)
        ) { income, expense ->
            income - expense
        }
    }
}

