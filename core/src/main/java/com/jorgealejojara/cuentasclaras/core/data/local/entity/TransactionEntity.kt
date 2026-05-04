package com.jorgealejojara.cuentasclaras.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val description: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    val type: String,
    val date: Long,
    @ColumnInfo(name = "account_id")
    val accountId: Long
)

