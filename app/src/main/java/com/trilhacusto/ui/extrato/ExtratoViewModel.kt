package com.trilhacusto.ui.extrato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.TransacaoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExtratoViewModel(
    private val transacaoDao: TransacaoDao,
    private val pessoaDao: PessoaDao,
    private val configuracoesDao: ConfiguracoesDao
) : ViewModel() {

    private val _filtroBuscaFlow = MutableStateFlow("")
    private val _filtroResponsavelFlow = MutableStateFlow<Long?>(null)
    
    private var isFilterInitialized = false

    private val _uiState = MutableStateFlow(ExtratoUiState(isLoading = true))
    val uiState: StateFlow<ExtratoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val limiteInicio = 0L
            val agora = Long.MAX_VALUE
            
            combine(
                transacaoDao.getTransacoesFiltradas(limiteInicio, agora, ""),
                pessoaDao.getAll(),
                _filtroBuscaFlow,
                _filtroResponsavelFlow,
                configuracoesDao.getConfiguracoes()
            ) { listaDb, pessoasDb, busca, responsavel, config ->
                val filtradaPorBusca = if (busca.isNotBlank()) {
                    listaDb.filter { 
                        val desc = (it.transacao.descricaoCustomizada ?: it.transacao.descricaoOriginal).lowercase()
                        desc.contains(busca.lowercase()) 
                    }
                } else {
                    listaDb
                }
                
                val semPendentes = filtradaPorBusca.filter { it.transacao.statusAtribuicao != "PENDENTE" }
                
                val filtradaPorResp = when (responsavel) {
                    null -> semPendentes
                    else -> semPendentes.filter { transacaoCompleta ->
                        transacaoCompleta.atribuicoes.any { atribuicao -> 
                            atribuicao.pessoa.id == responsavel && atribuicao.atribuicao.valorAtribuido != 0.0
                        }
                    }
                }

                val ordenada = filtradaPorResp.sortedByDescending { it.transacao.dataHora }

                ExtratoUiState(
                    isLoading = false,
                    transacoes = ordenada,
                    pessoas = pessoasDb,
                    filtroBusca = busca,
                    filtroResponsavel = responsavel,
                    isPrivacyModeEnabled = config?.isPrivacyModeEnabled ?: false
                )
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun togglePrivacyMode() {
        viewModelScope.launch {
            val config = configuracoesDao.getConfiguracoesSync()
            if (config != null) {
                configuracoesDao.insertOrUpdate(config.copy(isPrivacyModeEnabled = !config.isPrivacyModeEnabled))
            }
        }
    }

    fun atualizarBusca(query: String) {
        _filtroBuscaFlow.value = query
    }

    fun atualizarResponsavel(pessoaId: Long?) {
        _filtroResponsavelFlow.value = pessoaId
    }

    fun initFilterFromRoute(pessoaId: Long?) {
        if (!isFilterInitialized) {
            _filtroResponsavelFlow.value = pessoaId
            isFilterInitialized = true
        }
    }

    fun excluirTransacao(id: String) {
        viewModelScope.launch {
            try {
                transacaoDao.deleteTransacaoById(id)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao excluir transação: ${e.message}") }
            }
        }
    }

    fun limparErro() {
        _uiState.update { it.copy(error = null) }
    }
}
