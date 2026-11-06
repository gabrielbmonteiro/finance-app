package com.trilhacusto.ui.export

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.util.InvoiceDateUtils
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ExportFormat { PDF, JPG }

data class ExportUiState(
    val pessoas: List<PessoaEntity> = emptyList(),
    val pessoasSelecionadas: Set<Long> = emptySet(),
    val formato: ExportFormat = ExportFormat.PDF,
    val mesSelecionado: Int = 0,
    val anoSelecionado: Int = 0,
    val nomeMes: String = "",
    val periodoFatura: String = "",
    val periodoExtenso: String = "",
    val totalFatura: Double = 0.0,
    val totalTransacoes: Int = 0,
    val totalFaturaGeralMes: Double = 0.0,
    val totalTransacoesGeralMes: Int = 0,
    val transacoes: List<TransacaoCompleta> = emptyList(),
    val diaFechamento: Int = 25,
    val isFaturaFechada: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val exportSuccessUri: Uri? = null,
    val isShare: Boolean = false
)

class ExportViewModel(
    private val pessoaDao: PessoaDao,
    private val transacaoDao: TransacaoDao,
    private val configuracoesDao: ConfiguracoesDao,
    private val faturaDao: com.trilhacusto.data.local.dao.FaturaDao
) : ViewModel() {

    private val mesAnoFlow = MutableStateFlow<Pair<Int, Int>?>(null)
    private val pessoasSelecionadasFlow = MutableStateFlow<Set<Long>>(emptySet())

    private val _uiState = MutableStateFlow(ExportUiState(isLoading = true))
    val uiState: StateFlow<ExportUiState> = _uiState.asStateFlow()

    init {
        carregarDados()
    }

    fun setMesAno(mes: Int, ano: Int) {
        mesAnoFlow.value = Pair(mes, ano)
    }

    private fun carregarDados() {
        viewModelScope.launch {
            combine(
                pessoaDao.getAll(),
                mesAnoFlow,
                pessoasSelecionadasFlow,
                configuracoesDao.getConfiguracoes(),
                faturaDao.getAllFaturasFlow()
            ) { pessoas, mesAno, selecionadas, config, faturasDb ->
                if (mesAno == null) {
                    _uiState.update { it.copy(pessoas = pessoas, isLoading = true) }
                    return@combine
                }

                val fechamentoPadrao = config?.diaFechamento ?: 25
                val (mes, ano) = mesAno

                val faturaDb = faturasDb.find { it.mes == mes && it.ano == ano }
                val limites: com.trilhacusto.util.LimitesFatura
                val isFechada: Boolean
                val fechamentoEfetivo: Int

                if (faturaDb != null && (faturaDb.isFechada || faturaDb.inicioTimestamp > 0)) {
                    isFechada = faturaDb.isFechada || (faturaDb.fimTimestamp <= System.currentTimeMillis())
                    fechamentoEfetivo = faturaDb.diaFechamento
                    limites = InvoiceDateUtils.criarLimitesAPartirDeTimestamps(
                        inicioTimestamp = faturaDb.inicioTimestamp,
                        fimTimestamp = faturaDb.fimTimestamp,
                        mes = mes,
                        ano = ano
                    )
                } else {
                    val calPrev = Calendar.getInstance().apply {
                        set(Calendar.DAY_OF_MONTH, 1)
                        set(Calendar.MONTH, mes)
                        set(Calendar.YEAR, ano)
                        add(Calendar.MONTH, -1)
                    }
                    val faturaAnterior = faturasDb.find {
                        it.mes == calPrev.get(Calendar.MONTH) && it.ano == calPrev.get(Calendar.YEAR)
                    }
                    val inicioCustomizado = if (faturaAnterior != null && faturaAnterior.isFechada) {
                        faturaAnterior.fimTimestamp + 1
                    } else null

                    fechamentoEfetivo = faturaDb?.diaFechamento ?: fechamentoPadrao
                    val limitesCalculados = InvoiceDateUtils.calcularLimitesFatura(
                        diaFechamento = fechamentoEfetivo,
                        mesSelecionado = mes,
                        anoSelecionado = ano,
                        inicioTimestampCustomizado = inicioCustomizado
                    )
                    limites = limitesCalculados
                    isFechada = limitesCalculados.fimTimestamp <= System.currentTimeMillis()
                }

                // Busca todas as transações da fatura do mês para ter o total geral de referência
                val todasTransacoesDoMes = transacaoDao.getTransacoesCompletasDoMes(
                    limites.inicioTimestamp,
                    limites.fimTimestamp
                )
                val totalGeralMes = todasTransacoesDoMes.sumOf { it.transacao.valorTotal }
                val totalTransacoesGeralMes = todasTransacoesDoMes.size

                val transacoesFiltradas = if (selecionadas.isEmpty()) {
                    todasTransacoesDoMes
                } else {
                    todasTransacoesDoMes.filter { tc ->
                        tc.atribuicoes.any { it.pessoa.id in selecionadas && it.atribuicao.valorAtribuido != 0.0 }
                    }
                }

                val totalFiltrado = if (selecionadas.isNotEmpty()) {
                    transacoesFiltradas.flatMap { it.atribuicoes }
                        .filter { it.pessoa.id in selecionadas }
                        .sumOf { it.atribuicao.valorAtribuido }
                } else {
                    totalGeralMes
                }

                _uiState.update { current ->
                    current.copy(
                        pessoas = pessoas,
                        pessoasSelecionadas = selecionadas,
                        mesSelecionado = mes,
                        anoSelecionado = ano,
                        nomeMes = limites.nomeMes,
                        periodoFatura = limites.periodoFormatado,
                        periodoExtenso = limites.periodoExtenso,
                        totalFatura = totalFiltrado,
                        totalTransacoes = transacoesFiltradas.size,
                        totalFaturaGeralMes = totalGeralMes,
                        totalTransacoesGeralMes = totalTransacoesGeralMes,
                        transacoes = transacoesFiltradas,
                        diaFechamento = fechamentoEfetivo,
                        isFaturaFechada = isFechada,
                        isLoading = false
                    )
                }
            }.collectLatest { }
        }
    }

    fun togglePessoa(pessoaId: Long) {
        val current = pessoasSelecionadasFlow.value.toMutableSet()
        if (current.contains(pessoaId)) {
            current.remove(pessoaId)
        } else {
            current.add(pessoaId)
        }
        pessoasSelecionadasFlow.value = current
    }

    fun selecionarTodasPessoas() {
        pessoasSelecionadasFlow.value = emptySet()
    }

    fun setFormato(formato: ExportFormat) {
        _uiState.update { it.copy(formato = formato) }
    }

    fun limparMensagem() {
        _uiState.update { it.copy(errorMessage = null, exportSuccessUri = null) }
    }

    fun exportar(context: Context, isShare: Boolean, targetUri: Uri? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    exportSuccessUri = null,
                    isShare = isShare
                )
            }

            try {
                val state = _uiState.value
                val transacoes = state.transacoes

                if (transacoes.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Nenhuma transação encontrada para este mês."
                        )
                    }
                    return@launch
                }

                val uri = InvoiceExportHelper.gerarArquivoExportacao(
                    context = context,
                    transacoes = transacoes,
                    todasPessoas = state.pessoas,
                    formato = state.formato,
                    isShare = isShare,
                    pessoasSelecionadas = state.pessoasSelecionadas,
                    nomeMes = state.nomeMes,
                    periodoExtenso = state.periodoExtenso,
                    totalFaturaGeralMes = state.totalFaturaGeralMes,
                    targetUri = targetUri
                )

                if (uri != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            exportSuccessUri = uri
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Erro ao gerar arquivo."
                        )
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Erro interno: ${e.message}"
                    )
                }
            }
        }
    }
}
