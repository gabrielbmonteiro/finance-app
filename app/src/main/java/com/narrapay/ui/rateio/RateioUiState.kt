package com.narrapay.ui.rateio

import com.narrapay.data.local.entity.PessoaEntity
import com.narrapay.data.local.entity.CategoriaEntity

data class RateioUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transacaoId: String = "",
    val tituloInput: String = "",
    val valorTotalInput: String = "",
    val dataHora: Long = 0L,
    
    val categoriaIdSelecionada: Long? = null,
    val categorias: List<CategoriaEntity> = emptyList(),
    
    val pessoasNaDivida: List<PessoaEntity> = emptyList(),
    val todasAsPessoas: List<PessoaEntity> = emptyList(),
    val valoresInput: Map<Long, String> = emptyMap()
)
