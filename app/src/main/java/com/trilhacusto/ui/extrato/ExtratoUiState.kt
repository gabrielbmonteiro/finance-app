package com.trilhacusto.ui.extrato

import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta

data class ExtratoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transacoes: List<TransacaoCompleta> = emptyList(),
    val pessoas: List<PessoaEntity> = emptyList(),
    val filtroBusca: String = "",
    val filtroResponsavel: Long? = null,
    val isPrivacyModeEnabled: Boolean = false
)
