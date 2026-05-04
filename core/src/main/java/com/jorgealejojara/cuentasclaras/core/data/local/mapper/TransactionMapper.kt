package com.jorgealejojara.cuentasclaras.core.data.local.mapper

import com.jorgealejojara.cuentasclaras.core.data.local.entity.TransactionEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import java.time.LocalDate

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        description = description,
        categoryId = categoryId,
        type = TransactionType.valueOf(type),
        date = LocalDate.ofEpochDay(date),
        accountId = accountId
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        description = description,
        categoryId = categoryId,
        type = type.name,
        date = date.toEpochDay(),
        accountId = accountId
    )
}

