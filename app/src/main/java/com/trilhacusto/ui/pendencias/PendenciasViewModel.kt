package com.trilhacusto.ui.pendencias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.TransacaoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PendenciasViewModel(
    private val transacaoDao: TransacaoDao,
    private val configuracoesDao: ConfiguracoesDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendenciasUiState(isLoading = true))
    val uiState: StateFlow<PendenciasUiState> = _uiState.asStateFlow()

    init {
        observarPendencias()
    }

    private fun observarPendencias() {
        viewModelScope.launch {
            try {
                combine(
                    transacaoDao.getTransacoesPendentes(),
                    configuracoesDao.getConfiguracoes()
                ) { listaPendentes, config ->
                    Pair(listaPendentes, config)
                }.collect { (listaPendentes, config) ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            transacoesPendentes = listaPendentes,
                            isPrivacyModeEnabled = config?.isPrivacyModeEnabled ?: false,
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
            try {
                transacaoDao.deleteTransacaoById(id)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao excluir: ${e.message}") }
            }
        }
    }

    fun reativarTransacao(id: String) {
        viewModelScope.launch {
            try {
                val transacao = transacaoDao.getTransacaoById(id)
                if (transacao != null) {
                    transacaoDao.updateTransacao(transacao.copy(statusAtribuicao = "PENDENTE"))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao reativar: ${e.message}") }
            }
        }
    }

    fun limparErro() {
        _uiState.update { it.copy(error = null) }
    }

    fun togglePrivacyMode() {
        viewModelScope.launch {
            val config = configuracoesDao.getConfiguracoesSync()
            if (config != null) {
                configuracoesDao.insertOrUpdate(config.copy(isPrivacyModeEnabled = !config.isPrivacyModeEnabled))
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
