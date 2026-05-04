package com.jorgealejojara.cuentasclaras.core.data.local.mapper

import com.jorgealejojara.cuentasclaras.core.data.local.entity.CategoryEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Category
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        color = color,
        type = TransactionType.valueOf(type)
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        color = color,
        type = type.name
    )
}

