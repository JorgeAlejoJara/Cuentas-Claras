package com.jorgealejojara.cuentasclaras.core.domain.usecase

import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    fun getAll(): Flow<List<Transaction>> = repository.getAll()

    fun getByDateRange(start: LocalDate, end: LocalDate): Flow<List<Transaction>> =
        repository.getByDateRange(start, end)

    fun getByCategory(categoryId: Long): Flow<List<Transaction>> =
        repository.getByCategory(categoryId)
}

