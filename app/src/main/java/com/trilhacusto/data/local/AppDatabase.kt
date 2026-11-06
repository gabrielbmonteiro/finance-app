package com.trilhacusto.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trilhacusto.data.local.dao.CategoriaDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.RegraAutomacaoDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.local.dao.FaturaDao
import com.trilhacusto.data.local.entity.AtribuicaoTransacaoEntity
import com.trilhacusto.data.local.entity.CategoriaEntity
import com.trilhacusto.data.local.entity.FaturaEntity
import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.data.local.entity.RegraAutomacaoEntity
import com.trilhacusto.data.local.entity.TransacaoEntity

@Database(
    entities = [
        PessoaEntity::class,
        CategoriaEntity::class,
        TransacaoEntity::class,
        AtribuicaoTransacaoEntity::class,
        RegraAutomacaoEntity::class,
        com.trilhacusto.data.local.entity.ConfiguracoesEntity::class,
        FaturaEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transacaoDao(): TransacaoDao
    abstract fun pessoaDao(): PessoaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun regraAutomacaoDao(): RegraAutomacaoDao
    abstract fun configuracoesDao(): com.trilhacusto.data.local.dao.ConfiguracoesDao
    abstract fun faturaDao(): FaturaDao
}
