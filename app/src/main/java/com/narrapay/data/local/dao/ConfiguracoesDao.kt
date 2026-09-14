package com.narrapay.data.local.dao

import androidx.room.*
import com.narrapay.data.local.entity.ConfiguracoesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfiguracoesDao {
    @Query("SELECT * FROM configuracoes WHERE id = 1 LIMIT 1")
    fun getConfiguracoes(): Flow<ConfiguracoesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(configuracoes: ConfiguracoesEntity)
}
