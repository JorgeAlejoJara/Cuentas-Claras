package com.jorgealejojara.cuentasclaras.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jorgealejojara.cuentasclaras.core.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getById(id: Long): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE category_id = :categoryId ORDER BY date DESC")
    fun getByCategory(categoryId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE account_id = :accountId ORDER BY date DESC")
    fun getByAccount(accountId: Long): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT COALESCE(SUM(amount), 0.0) FROM transactions 
        WHERE type = :type 
        AND date BETWEEN :startDate AND :endDate
        """
    )
    fun getTotalByType(type: String, startDate: Long, endDate: Long): Flow<Double>
}

