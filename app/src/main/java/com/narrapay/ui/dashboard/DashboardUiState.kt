package com.narrapay.ui.dashboard

import com.narrapay.data.local.entity.PessoaEntity
import com.narrapay.data.local.relation.TransacaoCompleta

data class GastoPessoa(
    val pessoa: PessoaEntity,
    val valor: Double,
    val proporcao: Float
)

data class DashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mesAtual: String = "",
    val mesSelecionado: Int = 0,
    val anoSelecionado: Int = 0,
    val periodoFatura: String = "",
    val faturaTotal: Double = 0.0,
    val gastosPessoas: List<GastoPessoa> = emptyList(),
    val ultimasPendencias: List<TransacaoCompleta> = emptyList(),
    
    val showSyncLoading: Boolean = false,
    val syncStatus: SyncStatus? = null,

    val showFaturaConfigDialog: Boolean = false,
    val showMesAnoDialog: Boolean = false,
    
    val diaFechamento: Int = 25,
    val diaVencimento: Int = 5
)

enum class SyncStatus {
    SUCCESS, ERROR
}
