package com.jorgealejojara.cuentasclaras.core.data.local.mapper

import com.jorgealejojara.cuentasclaras.core.data.local.entity.AccountEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Account

fun AccountEntity.toDomain(): Account {
    return Account(
        id = id,
        name = name,
        balance = balance
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        name = name,
        balance = balance
    )
}

