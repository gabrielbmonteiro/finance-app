package com.narrapay.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.CategoriaEntity
import com.narrapay.data.local.entity.TransacaoEntity

data class TransacaoCompleta(
    @Embedded val transacao: TransacaoEntity,
    
    @Relation(
        parentColumn = "categoriaId",
        entityColumn = "id"
    )
    val categoria: CategoriaEntity?,
    
    @Relation(
        entity = AtribuicaoTransacaoEntity::class,
        parentColumn = "id",
        entityColumn = "transacaoId"
    )
    val atribuicoes: List<AtribuicaoComPessoa>
)
