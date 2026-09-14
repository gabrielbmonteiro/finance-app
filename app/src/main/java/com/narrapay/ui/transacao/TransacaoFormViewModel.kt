package com.narrapay.ui.transacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.TransacaoEntity
import com.narrapay.data.local.entity.CategoriaEntity
import com.narrapay.domain.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TransacaoFormViewModel(
    private val repository: TransacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransacaoFormUiState())
    val uiState: StateFlow<TransacaoFormUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.buscarCategorias().collect { categorias ->
                _uiState.update { it.copy(categorias = categorias) }
            }
        }
    }

    fun carregarTransacao(id: String?) {
        if (id == null) {
            _uiState.update { it.copy(isEditing = false, transacaoId = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val transacao = repository.getTransacaoById(id)
                if (transacao != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isEditing = true,
                            transacaoId = transacao.id,
                            descricaoInput = transacao.descricaoCustomizada ?: transacao.descricaoOriginal,
                            valorInput = transacao.valorTotal.toString(),
                            dataHora = transacao.dataHora,
                            categoriaIdSelecionada = transacao.categoriaId
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Transação não encontrada") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Erro ao carregar: ${e.message}") }
            }
        }
    }

    fun atualizarDescricao(descricao: String) {
        _uiState.update { it.copy(descricaoInput = descricao) }
    }

    fun atualizarValor(valor: String) {
        _uiState.update { it.copy(valorInput = valor) }
    }

    fun atualizarCategoria(categoriaId: Long?) {
        _uiState.update { it.copy(categoriaIdSelecionada = categoriaId) }
    }

    fun atualizarParcelas(parcelas: String) {
        _uiState.update { it.copy(parcelasInput = parcelas) }
    }

    fun salvarTransacao() {
        val state = _uiState.value
        val valorNumerico = state.valorInput.replace(",", ".").toDoubleOrNull()
        val numParcelas = state.parcelasInput.toIntOrNull() ?: 1
        
        if (state.descricaoInput.isBlank() || valorNumerico == null || valorNumerico <= 0 || numParcelas < 1) {
            _uiState.update { it.copy(error = "Preencha a descrição, valor e parcelas válidos.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                if (state.isEditing && state.transacaoId != null) {
                    val existente = repository.getTransacaoById(state.transacaoId)
                    if (existente != null) {
                        repository.atualizarTransacao(
                            existente.copy(
                                descricaoCustomizada = state.descricaoInput,
                                valorTotal = valorNumerico,
                                categoriaId = state.categoriaIdSelecionada
                            )
                        )
                    }
                } else {
                    val valorPorParcela = valorNumerico / numParcelas
                    
                    for (i in 1..numParcelas) {
                        val c = java.util.Calendar.getInstance()
                        c.timeInMillis = state.dataHora
                        c.add(java.util.Calendar.MONTH, i - 1)
                        
                        val novaTransacao = TransacaoEntity(
                            id = UUID.randomUUID().toString(),
                            descricaoOriginal = if (numParcelas > 1) "${state.descricaoInput} $i/$numParcelas" else state.descricaoInput,
                            valorTotal = valorPorParcela,
                            dataHora = c.timeInMillis,
                            categoriaId = state.categoriaIdSelecionada,
                            statusAtribuicao = "PENDENTE",
                            numeroParcela = if (numParcelas > 1) i else null,
                            totalParcelas = if (numParcelas > 1) numParcelas else null
                        )
                        repository.salvarTransacao(novaTransacao)
                    }
                }
                
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Erro ao salvar: ${e.message}") }
            }
        }
    }
    
    fun excluirTransacao() {
        val state = _uiState.value
        if (!state.isEditing || state.transacaoId == null) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.deletarTransacaoPorId(state.transacaoId)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Erro ao excluir: ${e.message}") }
            }
        }
    }
    
    fun limparErro() {
        _uiState.update { it.copy(error = null) }
    }
}
