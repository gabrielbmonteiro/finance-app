package com.trilhacusto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configuracoes")
data class ConfiguracoesEntity(
    @PrimaryKey
    val id: Int = 1,
    val diaFechamento: Int = 25,
    val diaVencimento: Int = 5,
    val isPrivacyModeEnabled: Boolean = false
)
