package com.narrapay.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.narrapay.data.local.dao.CategoriaDao
import com.narrapay.data.local.dao.PessoaDao
import com.narrapay.data.local.dao.RegraAutomacaoDao
import com.narrapay.data.local.dao.TransacaoDao
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.CategoriaEntity
import com.narrapay.data.local.entity.PessoaEntity
import com.narrapay.data.local.entity.RegraAutomacaoEntity
import com.narrapay.data.local.entity.TransacaoEntity

@Database(
    entities = [
        PessoaEntity::class,
        CategoriaEntity::class,
        TransacaoEntity::class,
        AtribuicaoTransacaoEntity::class,
        RegraAutomacaoEntity::class,
        com.narrapay.data.local.entity.ConfiguracoesEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transacaoDao(): TransacaoDao
    abstract fun pessoaDao(): PessoaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun regraAutomacaoDao(): RegraAutomacaoDao
    abstract fun configuracoesDao(): com.narrapay.data.local.dao.ConfiguracoesDao
}
