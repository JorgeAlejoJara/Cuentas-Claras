package com.jorgealejojara.cuentasclaras.core.domain.repository

import com.jorgealejojara.cuentasclaras.core.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getByMonthYear(month: Int, year: Int): Flow<List<Budget>>
    suspend fun insert(budget: Budget)
    suspend fun update(budget: Budget)
}

