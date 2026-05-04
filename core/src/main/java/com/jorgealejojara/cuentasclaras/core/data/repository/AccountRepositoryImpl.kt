package com.jorgealejojara.cuentasclaras.core.data.repository

import com.jorgealejojara.cuentasclaras.core.data.local.dao.AccountDao
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toDomain
import com.jorgealejojara.cuentasclaras.core.data.local.mapper.toEntity
import com.jorgealejojara.cuentasclaras.core.domain.model.Account
import com.jorgealejojara.cuentasclaras.core.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAll(): Flow<List<Account>> {
        return accountDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getById(id: Long): Flow<Account?> {
        return accountDao.getById(id).map { it?.toDomain() }
    }

    override suspend fun insert(account: Account) {
        accountDao.insert(account.toEntity())
    }

    override suspend fun update(account: Account) {
        accountDao.update(account.toEntity())
    }
}

