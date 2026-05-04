package com.jorgealejojara.cuentasclaras.core.di

import com.jorgealejojara.cuentasclaras.core.data.repository.AccountRepositoryImpl
import com.jorgealejojara.cuentasclaras.core.data.repository.BudgetRepositoryImpl
import com.jorgealejojara.cuentasclaras.core.data.repository.CategoryRepositoryImpl
import com.jorgealejojara.cuentasclaras.core.data.repository.TransactionRepositoryImpl
import com.jorgealejojara.cuentasclaras.core.domain.repository.AccountRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.BudgetRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.CategoryRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository
}

