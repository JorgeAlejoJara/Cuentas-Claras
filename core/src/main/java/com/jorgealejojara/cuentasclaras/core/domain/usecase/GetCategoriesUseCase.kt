package com.jorgealejojara.cuentasclaras.core.domain.usecase

import com.jorgealejojara.cuentasclaras.core.domain.model.Category
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    fun getAll(): Flow<List<Category>> = repository.getAll()

    fun getByType(type: TransactionType): Flow<List<Category>> = repository.getByType(type)
}

