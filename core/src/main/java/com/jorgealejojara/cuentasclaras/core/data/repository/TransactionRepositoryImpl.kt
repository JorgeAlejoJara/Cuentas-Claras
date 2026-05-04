package com.jorgealejojara.cuentasclaras.core.data.repository

import com.jorgealejojara.cuentasclaras.core.data.local.dao.TransactionDao
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toDomain
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getAll(): Flow<List<Transaction>> {
        return transactionDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getById(id: Long): Flow<Transaction?> {
        return transactionDao.getById(id).map { it?.toDomain() }
    }

    override fun getByDateRange(start: LocalDate, end: LocalDate): Flow<List<Transaction>> {
        return transactionDao.getByDateRange(start.toEpochDay(), end.toEpochDay()).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getByCategory(categoryId: Long): Flow<List<Transaction>> {
        return transactionDao.getByCategory(categoryId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTotalIncome(month: Int, year: Int): Flow<Double> {
        val yearMonth = YearMonth.of(year, month)
        val startDate = yearMonth.atDay(1).toEpochDay()
        val endDate = yearMonth.atEndOfMonth().toEpochDay()
        return transactionDao.getTotalByType(TransactionType.INCOME.name, startDate, endDate)
    }

    override fun getTotalExpense(month: Int, year: Int): Flow<Double> {
        val yearMonth = YearMonth.of(year, month)
        val startDate = yearMonth.atDay(1).toEpochDay()
        val endDate = yearMonth.atEndOfMonth().toEpochDay()
        return transactionDao.getTotalByType(TransactionType.EXPENSE.name, startDate, endDate)
    }

    override suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction.toEntity())
    }

    override suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction.toEntity())
    }

    override suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction.toEntity())
    }
}

