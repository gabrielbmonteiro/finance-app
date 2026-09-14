package com.narrapay.ui.pendencias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrapay.data.local.dao.TransacaoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PendenciasViewModel(
    private val transacaoDao: TransacaoDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendenciasUiState(isLoading = true))
    val uiState: StateFlow<PendenciasUiState> = _uiState.asStateFlow()

    init {
        observarPendencias()
    }

    private fun observarPendencias() {
        viewModelScope.launch {
            try {
                transacaoDao.getTransacoesPendentes().collect { listaPendentes ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            transacoesPendentes = listaPendentes,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun setFiltro(filtro: FiltroPendencia) {
        _uiState.update { it.copy(filtroSelecionado = filtro) }
    }

    fun atualizarBusca(texto: String) {
        _uiState.update { it.copy(termoBusca = texto) }
    }

    fun excluirTransacao(id: String) {
        viewModelScope.launch {
            transacaoDao.deleteTransacaoById(id)
        }
    }

    fun reativarTransacao(id: String) {
        viewModelScope.launch {
            val transacao = transacaoDao.getTransacaoById(id)
            if (transacao != null) {
                transacaoDao.updateTransacao(transacao.copy(statusAtribuicao = "PENDENTE"))
            }
        }
    }

    fun ignorarTransacao(id: String) {
        viewModelScope.launch {
            val transacao = transacaoDao.getTransacaoById(id)
            if (transacao != null) {
                transacaoDao.updateTransacao(transacao.copy(statusAtribuicao = "IGNORADO"))
            }
        }
    }
}
