package com.jorgealejojara.cuentasclaras.core.domain.repository

import com.jorgealejojara.cuentasclaras.core.domain.model.Category
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<Category>>
    fun getByType(type: TransactionType): Flow<List<Category>>
    suspend fun insert(category: Category)
}

