package com.jorgealejojara.cuentasclaras.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jorgealejojara.cuentasclaras.core.data.local.dao.AccountDao
import com.jorgealejojara.cuentasclaras.core.data.local.dao.BudgetDao
import com.jorgealejojara.cuentasclaras.core.data.local.dao.CategoryDao
import com.jorgealejojara.cuentasclaras.core.data.local.dao.TransactionDao
import com.jorgealejojara.cuentasclaras.core.data.local.entity.AccountEntity
import com.jorgealejojara.cuentasclaras.core.data.local.entity.BudgetEntity
import com.jorgealejojara.cuentasclaras.core.data.local.entity.CategoryEntity
import com.jorgealejojara.cuentasclaras.core.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class,
        AccountEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CuentasClarasDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun accountDao(): AccountDao
}

