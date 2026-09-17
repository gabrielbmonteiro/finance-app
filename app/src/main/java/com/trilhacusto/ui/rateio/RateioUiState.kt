package com.trilhacusto.ui.rateio

import com.trilhacusto.data.local.entity.CategoriaEntity
import com.trilhacusto.data.local.entity.PessoaEntity

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
    val valoresInput: Map<Long, String> = emptyMap(),
    
    val pessoaIdSugestao: Long? = null,
    val valorSugestao: String? = null,
    val dropdownPessoaId: Long? = null,
    val dropdownValor: String = ""
)
