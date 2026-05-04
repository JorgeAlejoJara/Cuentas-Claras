package com.jorgealejojara.cuentasclaras.core.domain.repository

import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    fun getById(id: Long): Flow<Transaction?>
    fun getByDateRange(start: LocalDate, end: LocalDate): Flow<List<Transaction>>
    fun getByCategory(categoryId: Long): Flow<List<Transaction>>
    fun getTotalIncome(month: Int, year: Int): Flow<Double>
    fun getTotalExpense(month: Int, year: Int): Flow<Double>
    suspend fun insert(transaction: Transaction)
    suspend fun update(transaction: Transaction)
    suspend fun delete(transaction: Transaction)
}

