package com.narrapay.data.local.dao

import androidx.room.*
import com.narrapay.data.local.entity.RegraAutomacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegraAutomacaoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(regra: RegraAutomacaoEntity): Long

    @Update
    suspend fun update(regra: RegraAutomacaoEntity)

    @Delete
    suspend fun delete(regra: RegraAutomacaoEntity)

    @Query("SELECT * FROM regras_automacao")
    fun getAll(): Flow<List<RegraAutomacaoEntity>>

    @Query("SELECT * FROM regras_automacao")
    fun getAllRegrasSync(): List<RegraAutomacaoEntity>
}
