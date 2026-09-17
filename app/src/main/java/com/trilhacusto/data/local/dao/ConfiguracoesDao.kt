package com.trilhacusto.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trilhacusto.data.local.entity.ConfiguracoesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfiguracoesDao {
    @Query("SELECT * FROM configuracoes WHERE id = 1 LIMIT 1")
    fun getConfiguracoes(): Flow<ConfiguracoesEntity?>

    @Query("SELECT * FROM configuracoes WHERE id = 1 LIMIT 1")
    suspend fun getConfiguracoesSync(): ConfiguracoesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(configuracoes: ConfiguracoesEntity)
}
