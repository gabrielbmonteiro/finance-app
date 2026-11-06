package com.trilhacusto.ui.extrato

import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.util.MesAno

data class FaturaExtratoGroup(
    val mesAno: MesAno,
    val nomeMes: String,
    val periodoExtrato: String,
    val totalFatura: Double,
    val transacoes: List<TransacaoCompleta>
)

data class ExtratoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transacoes: List<TransacaoCompleta> = emptyList(),
    val faturasAgrupadas: List<FaturaExtratoGroup> = emptyList(),
    val pessoas: List<PessoaEntity> = emptyList(),
    val filtroBusca: String = "",
    val filtroResponsavel: Long? = null,
    val isPrivacyModeEnabled: Boolean = false
)
