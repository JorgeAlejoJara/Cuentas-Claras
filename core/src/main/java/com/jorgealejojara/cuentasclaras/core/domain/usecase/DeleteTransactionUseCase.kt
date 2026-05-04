package com.jorgealejojara.cuentasclaras.core.domain.usecase

import com.jorgealejojara.cuentasclaras.core.domain.model.Transaction
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.domain.repository.AccountRepository
import com.jorgealejojara.cuentasclaras.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        transactionRepository.delete(transaction)

        val account = accountRepository.getById(transaction.accountId).first() ?: return
        val revertedBalance = when (transaction.type) {
            TransactionType.INCOME -> account.balance - transaction.amount
            TransactionType.EXPENSE -> account.balance + transaction.amount
        }
        accountRepository.update(account.copy(balance = revertedBalance))
    }
}

