package com.jorgealejojara.cuentasclaras.core.domain.repository

import com.jorgealejojara.cuentasclaras.core.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAll(): Flow<List<Account>>
    fun getById(id: Long): Flow<Account?>
    suspend fun insert(account: Account)
    suspend fun update(account: Account)
}

