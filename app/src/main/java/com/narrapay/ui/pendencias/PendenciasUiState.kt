package com.narrapay.ui.pendencias

import com.narrapay.data.local.relation.TransacaoCompleta

data class PendenciasUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val filtroSelecionado: FiltroPendencia = FiltroPendencia.TODOS,
    val transacoesPendentes: List<TransacaoCompleta> = emptyList(),
    val termoBusca: String = ""
)

enum class FiltroPendencia(val label: String) {
    TODOS("Todos"),
    CARTAO_GABRIEL("Só Cartão Gabriel"),
    CARTAO_PAI("Só Cartão Pai")
}
