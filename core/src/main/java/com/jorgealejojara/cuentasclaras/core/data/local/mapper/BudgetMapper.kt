package com.jorgealejojara.cuentasclaras.core.data.local.mapper

import com.jorgealejojara.cuentasclaras.core.data.local.entity.BudgetEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Budget

fun BudgetEntity.toDomain(): Budget {
    return Budget(
        id = id,
        categoryId = categoryId,
        limitAmount = limitAmount,
        month = month,
        year = year
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        categoryId = categoryId,
        limitAmount = limitAmount,
        month = month,
        year = year
    )
}

