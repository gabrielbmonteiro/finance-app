package com.narrapay.data.local.dao

import androidx.room.*
import com.narrapay.data.local.entity.PessoaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PessoaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pessoa: PessoaEntity): Long

    @Update
    suspend fun update(pessoa: PessoaEntity)

    @Delete
    suspend fun delete(pessoa: PessoaEntity)

    @Query("SELECT * FROM pessoas")
    fun getAll(): Flow<List<PessoaEntity>>
}
