package com.jorgealejojara.cuentasclaras.core.data.repository

import com.jorgealejojara.cuentasclaras.core.data.local.dao.CategoryDao
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toDomain
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Category
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAll(): Flow<List<Category>> {
        return categoryDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insert(category: Category) {
        categoryDao.insert(category.toEntity())
    }
}

