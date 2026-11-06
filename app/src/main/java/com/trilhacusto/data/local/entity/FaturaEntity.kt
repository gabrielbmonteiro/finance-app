package com.trilhacusto.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "faturas",
    primaryKeys = ["mes", "ano"]
)
data class FaturaEntity(
    val mes: Int,
    val ano: Int,
    val diaFechamento: Int,
    val diaVencimento: Int,
    val inicioTimestamp: Long,
    val fimTimestamp: Long,
    val isFechada: Boolean = false,
    val dataFechamentoEfetiva: Long? = null
)
