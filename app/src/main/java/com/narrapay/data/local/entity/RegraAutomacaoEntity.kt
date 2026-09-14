package com.narrapay.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regras_automacao")
data class RegraAutomacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val termoBusca: String,
    val categoriaIdPadrao: Long? = null,
    val pessoaIdPadrao: Long? = null
)
