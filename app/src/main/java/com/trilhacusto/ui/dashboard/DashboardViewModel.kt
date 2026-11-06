package com.trilhacusto.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trilhacusto.data.local.dao.ConfiguracoesDao
import com.trilhacusto.data.local.dao.PessoaDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.remote.repository.PluggyNetworkRepository
import com.trilhacusto.data.repository.UserPreferencesRepository
import com.trilhacusto.domain.usecase.ProcessarTransacaoPluggyUseCase
import com.trilhacusto.domain.usecase.TransacaoPluggyDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.trilhacusto.util.InvoiceDateUtils

import com.trilhacusto.data.local.dao.FaturaDao
import com.trilhacusto.data.local.entity.FaturaEntity

class DashboardViewModel(
    private val transacaoDao: TransacaoDao,
    private val configuracoesDao: ConfiguracoesDao,
    private val pessoaDao: PessoaDao,
    private val processarTransacaoPluggyUseCase: ProcessarTransacaoPluggyUseCase,
    private val pluggyNetworkRepository: PluggyNetworkRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val faturaDao: FaturaDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val mesAnoFlow = MutableStateFlow(
        Pair(
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        )
    )

    init {
        carregarDashboard()
    }

    private fun carregarDashboard() {
        viewModelScope.launch {
            combine(
                configuracoesDao.getConfiguracoes(),
                faturaDao.getAllFaturasFlow(),
                mesAnoFlow,
                userPreferencesRepository.lastSyncTime
            ) { config, faturasDb, mesAno, lastSync ->
                data class DashboardInputs(
                    val config: com.trilhacusto.data.local.entity.ConfiguracoesEntity?,
                    val faturasDb: List<FaturaEntity>,
                    val mesAno: Pair<Int, Int>,
                    val lastSync: Long?
                )
                DashboardInputs(config, faturasDb, mesAno, lastSync)
            }.collectLatest { (config, faturasDb, mesAno, lastSync) ->
                val fechamentoPadrao = config?.diaFechamento ?: 25
                val vencimentoPadrao = config?.diaVencimento ?: 5
                val (mesSelecionado, anoSelecionado) = mesAno
                val agora = System.currentTimeMillis()

                // Fecha automaticamente qualquer fatura já cadastrada cujo período já encerrou
                faturasDb.filter { !it.isFechada && it.fimTimestamp <= agora }.forEach { faturaVencida ->
                    faturaDao.update(
                        faturaVencida.copy(
                            isFechada = true,
                            dataFechamentoEfetiva = faturaVencida.fimTimestamp
                        )
                    )
                }

                // Localiza se o mês anterior possui fatura fechada para encadear o início sem intervalos
                val calPrev = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.MONTH, mesSelecionado)
                    set(Calendar.YEAR, anoSelecionado)
                    add(Calendar.MONTH, -1)
                }
                val faturaAnterior = faturasDb.find {
                    it.mes == calPrev.get(Calendar.MONTH) && it.ano == calPrev.get(Calendar.YEAR)
                }
                val inicioCustomizado = if (faturaAnterior != null && faturaAnterior.isFechada) {
                    faturaAnterior.fimTimestamp + 1
                } else null

                val faturaDb = faturasDb.find { it.mes == mesSelecionado && it.ano == anoSelecionado }
                val limites: com.trilhacusto.util.LimitesFatura
                val isFechada: Boolean

                if (faturaDb != null && (faturaDb.isFechada || faturaDb.inicioTimestamp > 0)) {
                    val deveFechar = !faturaDb.isFechada && faturaDb.fimTimestamp <= agora
                    isFechada = faturaDb.isFechada || deveFechar
                    if (deveFechar) {
                        faturaDao.update(
                            faturaDb.copy(
                                isFechada = true,
                                dataFechamentoEfetiva = faturaDb.fimTimestamp
                            )
                        )
                    }
                    limites = InvoiceDateUtils.criarLimitesAPartirDeTimestamps(
                        inicioTimestamp = faturaDb.inicioTimestamp,
                        fimTimestamp = faturaDb.fimTimestamp,
                        mes = mesSelecionado,
                        ano = anoSelecionado
                    )
                } else {
                    val fechamento = faturaDb?.diaFechamento ?: fechamentoPadrao
                    val limitesCalculados = InvoiceDateUtils.calcularLimitesFatura(
                        diaFechamento = fechamento,
                        mesSelecionado = mesSelecionado,
                        anoSelecionado = anoSelecionado,
                        inicioTimestampCustomizado = inicioCustomizado
                    )
                    limites = limitesCalculados

                    if (limitesCalculados.fimTimestamp <= agora) {
                        isFechada = true
                        val faturaParaFechar = FaturaEntity(
                            mes = mesSelecionado,
                            ano = anoSelecionado,
                            diaFechamento = fechamento,
                            diaVencimento = faturaDb?.diaVencimento ?: vencimentoPadrao,
                            inicioTimestamp = limitesCalculados.inicioTimestamp,
                            fimTimestamp = limitesCalculados.fimTimestamp,
                            isFechada = true,
                            dataFechamentoEfetiva = limitesCalculados.fimTimestamp
                        )
                        faturaDao.insertOrUpdate(faturaParaFechar)
                    } else {
                        isFechada = false
                    }
                }

                val inicioMes = limites.inicioTimestamp
                val fimMes = limites.fimTimestamp
                val nomeMes = limites.nomeMes
                val periodoFaturaStr = limites.periodoFormatado

                combine(
                    pessoaDao.getAll(),
                    transacaoDao.getGastosAgrupadosPorPessoa(inicioMes, fimMes),
                    transacaoDao.getGastoTotalMes(inicioMes, fimMes),
                    transacaoDao.getTransacoesPendentes()
                ) { pessoas, gastosAgrupados, gastoTotalMes, pendentes ->
                    val totalRateado = gastosAgrupados.sumOf { it.totalGasto }

                    val gastosPessoas = pessoas.map { pessoa ->
                        val gastoDaPessoa =
                            gastosAgrupados.find { it.pessoaId == pessoa.id }?.totalGasto ?: 0.0
                        val proporcao = if (totalRateado == 0.0) {
                            if (pessoas.isNotEmpty()) 1f / pessoas.size else 1f
                        } else {
                            (gastoDaPessoa / totalRateado).toFloat()
                        }
                        GastoPessoa(pessoa, gastoDaPessoa, proporcao)
                    }.sortedByDescending { it.valor }

                    val currentState = _uiState.value
                    currentState.copy(
                        isLoading = false,
                        mesAtual = nomeMes,
                        mesSelecionado = mesSelecionado,
                        anoSelecionado = anoSelecionado,
                        periodoFatura = periodoFaturaStr,
                        faturaTotal = gastoTotalMes,
                        gastosPessoas = gastosPessoas,
                        ultimasPendencias = pendentes.filter { it.transacao.statusAtribuicao == "PENDENTE" }
                            .take(5),
                        diaFechamento = faturaDb?.diaFechamento ?: fechamentoPadrao,
                        diaVencimento = faturaDb?.diaVencimento ?: vencimentoPadrao,
                        isFaturaFechada = isFechada,
                        isPrivacyModeEnabled = config?.isPrivacyModeEnabled ?: false,
                        lastSyncTime = lastSync
                    )
                }.collect { newState ->
                    _uiState.value = newState
                }
            }
        }
    }

    fun togglePrivacyMode() {
        viewModelScope.launch {
            val config = configuracoesDao.getConfiguracoes().firstOrNull()
                ?: com.trilhacusto.data.local.entity.ConfiguracoesEntity()
            configuracoesDao.insertOrUpdate(config.copy(isPrivacyModeEnabled = !config.isPrivacyModeEnabled))
        }
    }

    fun mudarMes(offset: Int) {
        val (mes, ano) = mesAnoFlow.value
        val c = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, mes)
            set(Calendar.YEAR, ano)
            add(Calendar.MONTH, offset)
        }
        mesAnoFlow.value = Pair(c.get(Calendar.MONTH), c.get(Calendar.YEAR))
    }

    fun selecionarMesAno(mes: Int, ano: Int) {
        mesAnoFlow.value = Pair(mes, ano)
        setShowMesAnoDialog(false)
    }

    fun setShowMesAnoDialog(show: Boolean) {
        _uiState.update { it.copy(showMesAnoDialog = show) }
    }

    fun setShowConfigDialog(show: Boolean) {
        _uiState.update { it.copy(showFaturaConfigDialog = show) }
    }

    fun salvarConfiguracaoFatura(fechamento: Int, vencimento: Int) {
        viewModelScope.launch {
            configuracoesDao.insertOrUpdate(
                com.trilhacusto.data.local.entity.ConfiguracoesEntity(
                    id = 1, diaFechamento = fechamento, diaVencimento = vencimento
                )
            )
            // Atualiza apenas as faturas que NÃO estão fechadas
            val faturasAbertas = faturaDao.getAllFaturas().filter { !it.isFechada }
            for (faturaAberta in faturasAbertas) {
                val novosLimites = InvoiceDateUtils.calcularLimitesFatura(
                    diaFechamento = fechamento,
                    mesSelecionado = faturaAberta.mes,
                    anoSelecionado = faturaAberta.ano
                )
                faturaDao.insertOrUpdate(
                    faturaAberta.copy(
                        diaFechamento = fechamento,
                        diaVencimento = vencimento,
                        inicioTimestamp = novosLimites.inicioTimestamp,
                        fimTimestamp = novosLimites.fimTimestamp
                    )
                )
            }
            setShowConfigDialog(false)
        }
    }

    fun sincronizarPluggy() {
        viewModelScope.launch {
            _uiState.update { it.copy(showSyncLoading = true, syncStatus = null) }

            val accountId = userPreferencesRepository.pluggyAccountId.firstOrNull()

            if (accountId.isNullOrEmpty()) {
                _uiState.update {
                    it.copy(
                        showSyncLoading = false,
                        syncStatus = SyncStatus.ERROR,
                        error = "Account ID não configurado."
                    )
                }
                delay(3000)
                _uiState.update { it.copy(syncStatus = null) }
                return@launch
            }

            val result = pluggyNetworkRepository.fetchTransactions(accountId)

            if (result.isSuccess) {
                val config = configuracoesDao.getConfiguracoes().firstOrNull()
                val fechamento = config?.diaFechamento ?: 25
                val c = Calendar.getInstance()
                val limites = InvoiceDateUtils.calcularLimitesFatura(
                    fechamento,
                    c.get(Calendar.MONTH),
                    c.get(Calendar.YEAR)
                )
                val inicioFaturaAtual = limites.inicioTimestamp

                val transactions = result.getOrNull() ?: emptyList()
                val dtos = transactions.map {
                    TransacaoPluggyDto(
                        id = it.id,
                        amount = it.amount,
                        description = it.merchant?.name ?: it.merchant?.businessName
                        ?: it.description,
                        date = try {
                            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                            format.parse(it.date)?.time ?: System.currentTimeMillis()
                        } catch (e: Exception) {
                            System.currentTimeMillis()
                        },
                        numeroParcela = it.creditCardMetadata?.installmentNumber,
                        totalParcelas = it.creditCardMetadata?.totalInstallments
                    )
                }.filter { it.date >= inicioFaturaAtual }

                processarTransacaoPluggyUseCase(dtos)

                userPreferencesRepository.saveLastSyncTime(System.currentTimeMillis())
                _uiState.update {
                    it.copy(
                        showSyncLoading = false,
                        syncStatus = SyncStatus.SUCCESS
                    )
                }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update {
                    it.copy(
                        showSyncLoading = false,
                        syncStatus = SyncStatus.ERROR,
                        error = error?.message
                    )
                }
            }

            delay(3000)
            _uiState.update { it.copy(syncStatus = null) }
        }
    }

    fun injetarDadosDeTeste() {
        viewModelScope.launch {
            try {
                val hoje = System.currentTimeMillis()
                val ontem = hoje - 86400000

                val m1 = TransacaoPluggyDto(
                    id = "mock_uber_${System.currentTimeMillis()}",
                    description = "Viagem Uber",
                    amount = 25.50,
                    date = hoje
                )

                val m2 = TransacaoPluggyDto(
                    id = "mock_ifood_${System.currentTimeMillis()}",
                    description = "IFOOD *RESTAURANTE",
                    amount = 89.90,
                    date = ontem
                )

                val numParcelas = 3
                val parcelas = mutableListOf<TransacaoPluggyDto>()

                for (i in 1..numParcelas) {
                    val data = Calendar.getInstance().apply {
                        timeInMillis = hoje
                        add(Calendar.MONTH, i - 1)
                    }.timeInMillis

                    parcelas.add(
                        TransacaoPluggyDto(
                            id = "mock_posto_${System.currentTimeMillis()}_$i",
                            description = "AUTO POSTO SAO JOSE $i/$numParcelas",
                            amount = 150.00 / numParcelas,
                            date = data,
                            numeroParcela = i,
                            totalParcelas = numParcelas
                        )
                    )
                }

                processarTransacaoPluggyUseCase(listOf(m1, m2) + parcelas)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Erro ao injetar mocks: ${e.message}") }
            }
        }
    }
}
