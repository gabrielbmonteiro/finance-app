package com.narrapay.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transacoes",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["categoriaId"])
    ]
)
data class TransacaoEntity(
    @PrimaryKey
    val id: String,
    val valorTotal: Double,
    val descricaoOriginal: String,
    val descricaoCustomizada: String? = null,
    val dataHora: Long,
    val categoriaId: Long? = null,
    val nota: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val statusAtribuicao: String = "PENDENTE",
    val numeroParcela: Int? = null,
    val totalParcelas: Int? = null
)
