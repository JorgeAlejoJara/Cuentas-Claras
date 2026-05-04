package com.jorgealejojara.cuentasclaras.core.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String,
    val color: Long,
    val type: TransactionType
)

