package com.trilhacusto.ui.transacao

data class TransacaoFormUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val transacaoId: String? = null,
    val descricaoInput: String = "",
    val valorInput: String = "",
    val dataHora: Long = System.currentTimeMillis(),
    val isEditing: Boolean = false,
    val categorias: List<com.trilhacusto.data.local.entity.CategoriaEntity> = emptyList(),
    val categoriaIdSelecionada: Long? = null,
    val parcelasInput: String = "1",
    val todasAsPessoas: List<com.trilhacusto.data.local.entity.PessoaEntity> = emptyList(),
    val pessoasNaDivida: List<com.trilhacusto.data.local.entity.PessoaEntity> = emptyList(),
    val valoresInput: Map<Long, String> = emptyMap(),
    val pessoaIdSugestao: Long? = null,
    val valorSugestao: String = ""
)
