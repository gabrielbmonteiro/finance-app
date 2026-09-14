package com.narrapay.ui.extrato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.narrapay.data.local.dao.PessoaDao
import com.narrapay.data.local.dao.TransacaoDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExtratoViewModel(
    private val transacaoDao: TransacaoDao,
    private val pessoaDao: PessoaDao
) : ViewModel() {

    private val _filtroBuscaFlow = MutableStateFlow("")
    private val _filtroResponsavelFlow = MutableStateFlow<Long?>(null)

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
                _filtroResponsavelFlow
            ) { listaDb, pessoasDb, busca, responsavel ->
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
                            atribuicao.pessoa.id == responsavel && atribuicao.atribuicao.valorAtribuido > 0.0
                        }
                    }
                }

                val ordenada = filtradaPorResp.sortedByDescending { it.transacao.dataHora }

                ExtratoUiState(
                    isLoading = false,
                    transacoes = ordenada,
                    pessoas = pessoasDb,
                    filtroBusca = busca,
                    filtroResponsavel = responsavel
                )
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun atualizarBusca(query: String) {
        _filtroBuscaFlow.value = query
    }

    fun atualizarResponsavel(pessoaId: Long?) {
        _filtroResponsavelFlow.value = pessoaId
    }

    fun excluirTransacao(id: String) {
        viewModelScope.launch {
            transacaoDao.deleteTransacaoById(id)
        }
    }
}
