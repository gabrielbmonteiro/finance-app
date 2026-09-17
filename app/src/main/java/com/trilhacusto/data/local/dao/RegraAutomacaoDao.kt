package com.trilhacusto.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trilhacusto.data.local.entity.RegraAutomacaoEntity
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
