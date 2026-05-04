package com.jorgealejojara.cuentasclaras.core.data.repository

import com.jorgealejojara.cuentasclaras.core.data.local.dao.BudgetDao
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toDomain
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Budget
import com.jorgealejojara.cuentasclaras.core.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun getByMonthYear(month: Int, year: Int): Flow<List<Budget>> {
        return budgetDao.getByMonthYear(month, year).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insert(budget: Budget) {
        budgetDao.insert(budget.toEntity())
    }

    override suspend fun update(budget: Budget) {
        budgetDao.update(budget.toEntity())
    }
}

