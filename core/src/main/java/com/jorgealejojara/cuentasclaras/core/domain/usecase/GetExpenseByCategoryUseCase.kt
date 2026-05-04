package com.jorgealejojara.cuentasclaras.core.domain.usecase

import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetExpenseByCategoryUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(start: LocalDate, end: LocalDate): Flow<Map<Long, Double>> {
        return repository.getByDateRange(start, end).map { transactions ->
            transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.categoryId }
                .mapValues { (_, txList) -> txList.sumOf { it.amount } }
        }
    }
}

