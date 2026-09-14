package com.narrapay.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "atribuicoes_transacao",
    foreignKeys = [
        ForeignKey(
            entity = TransacaoEntity::class,
            parentColumns = ["id"],
            childColumns = ["transacaoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PessoaEntity::class,
            parentColumns = ["id"],
            childColumns = ["pessoaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["transacaoId"]),
        Index(value = ["pessoaId"])
    ]
)
data class AtribuicaoTransacaoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transacaoId: String,
    val pessoaId: Long,
    val valorAtribuido: Double
)
