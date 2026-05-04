package com.jorgealejojara.cuentasclaras.core.domain.model

import java.time.LocalDate

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val categoryId: Long,
    val type: TransactionType,
    val date: LocalDate,
    val accountId: Long
)

