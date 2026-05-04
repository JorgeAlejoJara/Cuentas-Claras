package com.jorgealejojara.cuentasclaras.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "limit_amount")
    val limitAmount: Double,
    val month: Int,
    val year: Int
)

