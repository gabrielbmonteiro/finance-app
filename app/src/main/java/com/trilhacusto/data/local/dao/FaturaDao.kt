package com.trilhacusto.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trilhacusto.data.local.entity.FaturaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FaturaDao {
    @Query("SELECT * FROM faturas WHERE mes = :mes AND ano = :ano LIMIT 1")
    suspend fun getFatura(mes: Int, ano: Int): FaturaEntity?

    @Query("SELECT * FROM faturas WHERE mes = :mes AND ano = :ano LIMIT 1")
    fun getFaturaFlow(mes: Int, ano: Int): Flow<FaturaEntity?>

    @Query("SELECT * FROM faturas")
    suspend fun getAllFaturas(): List<FaturaEntity>

    @Query("SELECT * FROM faturas")
    fun getAllFaturasFlow(): Flow<List<FaturaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(fatura: FaturaEntity)

    @Update
    suspend fun update(fatura: FaturaEntity)
}
