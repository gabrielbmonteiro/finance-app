package com.trilhacusto.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.trilhacusto.data.local.entity.AtribuicaoTransacaoEntity
import com.trilhacusto.data.local.entity.PessoaEntity

data class AtribuicaoComPessoa(
    @Embedded val atribuicao: AtribuicaoTransacaoEntity,
    
    @Relation(
        parentColumn = "pessoaId",
        entityColumn = "id"
    )
    val pessoa: PessoaEntity
)
