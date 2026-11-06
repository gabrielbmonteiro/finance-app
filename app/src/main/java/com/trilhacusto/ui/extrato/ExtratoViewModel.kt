package com.trilhacusto.ui.extrato

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.util.InvoiceDateUtils
import com.trilhacusto.util.MesAno
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import com.trilhacusto.data.local.dao.FaturaDao
import kotlinx.coroutines.launch

class ExtratoViewModel(
    private val transacaoDao: TransacaoDao,
    private val pessoaDao: PessoaDao,
    private val configuracoesDao: ConfiguracoesDao,
    private val faturaDao: FaturaDao
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
                combine(
                    transacaoDao.getTransacoesFiltradas(limiteInicio, agora, ""),
                    pessoaDao.getAll(),
                    _filtroBuscaFlow,
                    _filtroResponsavelFlow
                ) { listaDb, pessoasDb, busca, responsavel ->
                    data class ExtratoFiltroData(
                        val listaDb: List<com.trilhacusto.data.local.relation.TransacaoCompleta>,
                        val pessoasDb: List<com.trilhacusto.data.local.entity.PessoaEntity>,
                        val busca: String,
                        val responsavel: Long?
                    )
                    ExtratoFiltroData(listaDb, pessoasDb, busca, responsavel)
                },
                configuracoesDao.getConfiguracoes(),
                faturaDao.getAllFaturasFlow()
            ) { filtroData, config, faturasDb ->
                val listaDb = filtroData.listaDb
                val pessoasDb = filtroData.pessoasDb
                val busca = filtroData.busca
                val responsavel = filtroData.responsavel

                val filtradaPorBusca = if (busca.isNotBlank()) {
                    listaDb.filter {
                        val desc = (it.transacao.descricaoCustomizada
                            ?: it.transacao.descricaoOriginal).lowercase()
                        desc.contains(busca.lowercase())
                    }
                } else {
                    listaDb
                }

                val semPendentes =
                    filtradaPorBusca.filter { it.transacao.statusAtribuicao != "PENDENTE" }

                val filtradaPorResp = when (responsavel) {
                    null -> semPendentes
                    else -> semPendentes.filter { transacaoCompleta ->
                        transacaoCompleta.atribuicoes.any { atribuicao ->
                            atribuicao.pessoa.id == responsavel && atribuicao.atribuicao.valorAtribuido != 0.0
                        }
                    }
                }

                val ordenada = filtradaPorResp.sortedByDescending { it.transacao.dataHora }

                val fechamento = config?.diaFechamento ?: 25

                val faturasAgrupadas = ordenada
                    .groupBy { tc ->
                        val faturaDb = faturasDb.firstOrNull {
                            tc.transacao.dataHora in it.inicioTimestamp..it.fimTimestamp
                        }
                        if (faturaDb != null) {
                            MesAno(faturaDb.mes, faturaDb.ano)
                        } else {
                            InvoiceDateUtils.identificarFaturaDaTransacao(tc.transacao.dataHora, fechamento)
                        }
                    }
                    .map { (mesAno, transacoesDoMes) ->
                        val faturaDb = faturasDb.firstOrNull {
                            it.mes == mesAno.mes && it.ano == mesAno.ano
                        }
                        val limites = if (faturaDb != null && faturaDb.inicioTimestamp > 0) {
                            InvoiceDateUtils.criarLimitesAPartirDeTimestamps(
                                inicioTimestamp = faturaDb.inicioTimestamp,
                                fimTimestamp = faturaDb.fimTimestamp,
                                mes = mesAno.mes,
                                ano = mesAno.ano
                            )
                        } else {
                            InvoiceDateUtils.calcularLimitesFatura(fechamento, mesAno.mes, mesAno.ano)
                        }

                        val total = transacoesDoMes.sumOf { tc ->
                            if (responsavel != null) {
                                tc.atribuicoes.find { it.pessoa.id == responsavel }?.atribuicao?.valorAtribuido ?: 0.0
                            } else {
                                tc.transacao.valorTotal
                            }
                        }
                        FaturaExtratoGroup(
                            mesAno = mesAno,
                            nomeMes = limites.nomeMes,
                            periodoExtrato = limites.periodoExtrato,
                            totalFatura = total,
                            transacoes = transacoesDoMes
                        )
                    }
                    .sortedByDescending { it.mesAno }

                ExtratoUiState(
                    isLoading = false,
                    transacoes = ordenada,
                    faturasAgrupadas = faturasAgrupadas,
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
                transacaoDao.deleteAtribuicoesDaTransacao(id)
                transacaoDao.updateStatusParaPendente(listOf(id))
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao mover compra para pendências: ${e.message}") }
            }
        }
    }

    fun limparErro() {
        _uiState.update { it.copy(error = null) }
    }
}
