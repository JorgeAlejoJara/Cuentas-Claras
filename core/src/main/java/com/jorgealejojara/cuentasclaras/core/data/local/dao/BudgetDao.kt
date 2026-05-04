package com.jorgealejojara.cuentasclaras.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jorgealejojara.cuentasclaras.core.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity)

    @Update
    suspend fun update(budget: BudgetEntity)

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    fun getByMonthYear(month: Int, year: Int): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE category_id = :categoryId AND month = :month AND year = :year")
    fun getByCategoryAndMonth(categoryId: Long, month: Int, year: Int): Flow<BudgetEntity?>
}

