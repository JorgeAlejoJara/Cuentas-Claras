package com.jorgealejojara.cuentasclaras.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jorgealejojara.cuentasclaras.core.data.local.CuentasClarasDatabase
import com.jorgealejojara.cuentasclaras.core.data.local.dao.AccountDao
import com.jorgealejojara.cuentasclaras.core.data.local.dao.BudgetDao
import com.jorgealejojara.cuentasclaras.core.data.local.dao.CategoryDao
import com.jorgealejojara.cuentasclaras.core.data.local.dao.TransactionDao
import com.jorgealejojara.cuentasclaras.core.data.local.entity.CategoryEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        categoryDaoProvider: Provider<CategoryDao>
    ): CuentasClarasDatabase {
        return Room.databaseBuilder(
            context,
            CuentasClarasDatabase::class.java,
            "cuentas_claras.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        categoryDaoProvider.get().insertAll(getDefaultCategories())
                    }
                }
            })
            .build()
    }

    @Provides
    fun provideTransactionDao(database: CuentasClarasDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideCategoryDao(database: CuentasClarasDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideBudgetDao(database: CuentasClarasDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    fun provideAccountDao(database: CuentasClarasDatabase): AccountDao {
        return database.accountDao()
    }

    private fun getDefaultCategories(): List<CategoryEntity> {
        return listOf(
            // Expense categories (colors from design)
            CategoryEntity(name = "Comida", icon = "Restaurant", color = 0xFFE07A5F, type = "EXPENSE"),
            CategoryEntity(name = "Transporte", icon = "DirectionsCar", color = 0xFF5E81AC, type = "EXPENSE"),
            CategoryEntity(name = "Entretenimiento", icon = "SportsEsports", color = 0xFFF2A65A, type = "EXPENSE"),
            CategoryEntity(name = "Salud", icon = "LocalHospital", color = 0xFFE76F8E, type = "EXPENSE"),
            CategoryEntity(name = "Educación", icon = "School", color = 0xFF4FB286, type = "EXPENSE"),
            CategoryEntity(name = "Hogar", icon = "Home", color = 0xFF8B5CF6, type = "EXPENSE"),
            CategoryEntity(name = "Ropa", icon = "Checkroom", color = 0xFFD08770, type = "EXPENSE"),
            CategoryEntity(name = "Otros", icon = "MoreHoriz", color = 0xFF9AA5A0, type = "EXPENSE"),
            // Income categories
            CategoryEntity(name = "Salario", icon = "Work", color = 0xFF00875A, type = "INCOME"),
            CategoryEntity(name = "Freelance", icon = "Laptop", color = 0xFF3D6473, type = "INCOME"),
            CategoryEntity(name = "Inversiones", icon = "TrendingUp", color = 0xFF4FB286, type = "INCOME"),
            CategoryEntity(name = "Otros ingresos", icon = "AttachMoney", color = 0xFF5E81AC, type = "INCOME"),
        )
    }
}

