package com.narrapay.ui.extrato

import com.narrapay.data.local.entity.PessoaEntity
import com.narrapay.data.local.relation.TransacaoCompleta

data class ExtratoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transacoes: List<TransacaoCompleta> = emptyList(),
    val pessoas: List<PessoaEntity> = emptyList(),
    val filtroBusca: String = "",
    val filtroResponsavel: Long? = null
)
