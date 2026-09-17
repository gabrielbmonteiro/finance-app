package com.trilhacusto.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trilhacusto.data.local.entity.PessoaEntity
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
