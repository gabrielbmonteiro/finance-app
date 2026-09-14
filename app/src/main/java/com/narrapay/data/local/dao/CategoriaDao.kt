package com.narrapay.data.local.dao

import androidx.room.*
import com.narrapay.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoria: CategoriaEntity): Long

    @Update
    suspend fun update(categoria: CategoriaEntity)

    @Delete
    suspend fun delete(categoria: CategoriaEntity)

    @Query("SELECT * FROM categorias")
    fun getAll(): Flow<List<CategoriaEntity>>
}
