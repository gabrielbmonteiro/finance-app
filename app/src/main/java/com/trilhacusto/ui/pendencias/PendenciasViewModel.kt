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
                ) { listaTotal, config ->
                    Pair(listaTotal, config)
                }.collect { (listaTotal, config) ->
                    val pendentes = listaTotal.filter { it.transacao.statusAtribuicao == "PENDENTE" }
                    val excluidas = listaTotal.filter { it.transacao.statusAtribuicao == "IGNORADO" }
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            transacoesPendentes = pendentes,
                            transacoesExcluidas = excluidas,
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

    fun atualizarBuscaExcluidas(texto: String) {
        _uiState.update { it.copy(termoBuscaExcluidas = texto) }
    }

    fun excluirTransacao(id: String) {
        // Move para a lista de excluídas (statusAtribuicao = "IGNORADO")
        viewModelScope.launch {
            try {
                val transacao = transacaoDao.getTransacaoById(id)
                if (transacao != null) {
                    transacaoDao.updateTransacao(transacao.copy(statusAtribuicao = "IGNORADO"))
                    _uiState.update { it.copy(mensagemSucesso = "Pendência movida para excluídas") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao excluir: ${e.message}") }
            }
        }
    }

    fun reativarTransacao(id: String) {
        // Retira da exclusão e devolve para as pendências ativas
        viewModelScope.launch {
            try {
                val transacao = transacaoDao.getTransacaoById(id)
                if (transacao != null) {
                    transacaoDao.updateTransacao(transacao.copy(statusAtribuicao = "PENDENTE"))
                    _uiState.update { it.copy(mensagemSucesso = "Pendência retirada da exclusão!") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao reativar: ${e.message}") }
            }
        }
    }

    fun excluirDefinitivamente(id: String) {
        // Exclui permanentemente do banco de dados
        viewModelScope.launch {
            try {
                transacaoDao.deleteTransacaoById(id)
                _uiState.update { it.copy(mensagemSucesso = "Pendência excluída permanentemente") }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao excluir definitivamente: ${e.message}") }
            }
        }
    }

    fun limparErro() {
        _uiState.update { it.copy(error = null) }
    }

    fun limparMensagemSucesso() {
        _uiState.update { it.copy(mensagemSucesso = null) }
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
        excluirTransacao(id)
    }
}
