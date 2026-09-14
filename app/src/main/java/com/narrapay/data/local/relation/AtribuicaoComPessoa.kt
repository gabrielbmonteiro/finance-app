package com.narrapay.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.PessoaEntity

data class AtribuicaoComPessoa(
    @Embedded val atribuicao: AtribuicaoTransacaoEntity,
    
    @Relation(
        parentColumn = "pessoaId",
        entityColumn = "id"
    )
    val pessoa: PessoaEntity
)
